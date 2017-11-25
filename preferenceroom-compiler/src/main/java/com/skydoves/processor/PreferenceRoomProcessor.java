/*
 * Copyright (C) 2017 skydoves
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.skydoves.processor;

import com.google.auto.service.AutoService;
import com.google.common.base.VerifyException;
import com.skydoves.preferenceroom.InjectPreference;
import com.skydoves.preferenceroom.PreferenceComponent;
import com.skydoves.preferenceroom.PreferenceEntity;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.TypeSpec;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;

import static javax.tools.Diagnostic.Kind.ERROR;
import static javax.tools.Diagnostic.Kind.NOTE;

@SupportedSourceVersion(SourceVersion.RELEASE_8)
@SupportedAnnotationTypes({
        "com.skydoves.preferenceroom.Preference",
        "com.skydoves.preferenceroom.KeyName",
        "com.skydoves.preferenceroom.PreferenceComponent",
        "com.skydoves.preferenceroom.InjectPreference"})
@AutoService(Processor.class)
public class PreferenceRoomProcessor extends AbstractProcessor {

    private Map<String, String> annotatedEntityTypeMap;
    private Map<String, PreferenceEntityAnnotatedClass> annotatedEntityMap;
    private List<PreferenceComponentAnnotatedClass> annotatedComponentList;
    private Messager messager;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        annotatedEntityMap = new HashMap<>();
        annotatedEntityTypeMap = new HashMap<>();
        annotatedComponentList = new ArrayList<>();
        messager = processingEnv.getMessager();
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        messager.printMessage(NOTE, "start PreferenceRoom-Processor");
        if(annotations.isEmpty()) {
            return true;
        }

        roundEnv.getElementsAnnotatedWith(PreferenceEntity.class).stream()
                .map(annotatedType -> (TypeElement) annotatedType)
                .forEach(annotatedType -> {
                    try {
                        checkValidEntityType(annotatedType);
                        processEntity(annotatedType);
                    } catch (IllegalAccessException e) {
                        showErrorLog(e.getMessage(), annotatedType);
                    }
                });

        roundEnv.getElementsAnnotatedWith(PreferenceComponent.class).stream()
                .map(annotatedType -> (TypeElement) annotatedType)
                .forEach(annotatedType -> {
                    try {
                        checkValidComponentType(annotatedType);
                        processComponent(annotatedType);
                    } catch (IllegalAccessException e) {
                        showErrorLog(e.getMessage(), annotatedType);
                    }
                });

        roundEnv.getElementsAnnotatedWith(InjectPreference.class).forEach(field -> {
            try {
                if (!field.getModifiers().contains(Modifier.PUBLIC)) {
                    throw new IllegalAccessException("annotated with @InjectPreference field's modifier should be public");
                }
            } catch (IllegalAccessException e) {
                showErrorLog(e.getMessage(), field);
            }
        });

        annotatedComponentList.forEach(this::processInjector);

        return true;
    }

    private void processEntity(TypeElement annotatedType) throws VerifyException {
        try {
            PreferenceEntityAnnotatedClass annotatedClazz = new PreferenceEntityAnnotatedClass(annotatedType, processingEnv.getElementUtils());
            checkDuplicatedPreferenceEntity(annotatedClazz);
            generateProcessEntity(annotatedClazz);
        } catch (VerifyException e) {
            showErrorLog(e.getMessage(), annotatedType);
            e.printStackTrace();
        }
    }

    private void processComponent(TypeElement annotatedType) throws VerifyException {
        try {
            PreferenceComponentAnnotatedClass annotatedClazz = new PreferenceComponentAnnotatedClass(annotatedType, processingEnv.getElementUtils(), annotatedEntityTypeMap);
            checkDuplicatedPreferenceComponent(annotatedClazz);
            generateProcessComponent(annotatedClazz);
        } catch (VerifyException e) {
            showErrorLog(e.getMessage(), annotatedType);
            e.printStackTrace();
        }
    }

    private void processInjector(PreferenceComponentAnnotatedClass annotatedClass) throws VerifyException {
        try {
            annotatedClass.annotatedElement.getEnclosedElements().forEach(method -> {
                        MethodSpec methodSpec = MethodSpec.overriding((ExecutableElement) method).build();
                        ParameterSpec parameterSpec = methodSpec.parameters.get(0);
                        TypeElement injectedElement = processingEnv.getElementUtils().getTypeElement(parameterSpec.type.toString());
                        generateProcessInjector(annotatedClass, injectedElement);
                    });
        } catch (VerifyException e) {
            showErrorLog(e.getMessage(), annotatedClass.annotatedElement);
            e.printStackTrace();
        }
    }

    private void generateProcessEntity(PreferenceEntityAnnotatedClass annotatedClass) {
        try {
            TypeSpec annotatedClazz = (new PreferenceEntityGenerator(annotatedClass)).generate();
            JavaFile.builder(annotatedClass.packageName, annotatedClazz).build().writeTo(processingEnv.getFiler());
        } catch (IOException e) {
            // ignore ;)
        }
    }

    private void generateProcessComponent(PreferenceComponentAnnotatedClass annotatedClass) {
        try {
            TypeSpec annotatedClazz = (new PreferenceComponentGenerator(annotatedClass, annotatedEntityMap)).generate();
            JavaFile.builder(annotatedClass.packageName, annotatedClazz).build().writeTo(processingEnv.getFiler());
        } catch (IOException e) {
            // ignore >.<
        }
    }

    private void generateProcessInjector(PreferenceComponentAnnotatedClass annotatedClass, TypeElement injectedElement) {
        try {
            TypeSpec injectorSpec = new InjectorGenerator(annotatedClass, injectedElement).generate();
            JavaFile.builder(annotatedClass.packageName, injectorSpec).build().writeTo(processingEnv.getFiler());
        } catch (IOException e) {
            // ignore ^v^
        }
    }

    private void checkValidEntityType(TypeElement annotatedType) throws IllegalAccessException {
        if(!annotatedType.getKind().isClass()) {
            throw new IllegalAccessException("Only classes can be annotated with @PreferenceRoom");
        } else if(annotatedType.getModifiers().contains(Modifier.FINAL)) {
            showErrorLog("class modifier should not be final", annotatedType);
        } else if(annotatedType.getModifiers().contains(Modifier.PRIVATE)) {
            showErrorLog("class modifier should not be final", annotatedType);
        }
    }

    private void checkValidComponentType(TypeElement annotatedType) throws IllegalAccessException {
        if(!annotatedType.getKind().isInterface()) {
            throw new IllegalAccessException("Only interfaces can be annotated with @PreferenceComponent");
        }
    }

    private void checkDuplicatedPreferenceEntity(PreferenceEntityAnnotatedClass annotatedClazz) throws VerifyException {
        if(annotatedEntityMap.containsKey(annotatedClazz.preferenceName)) {
            throw new VerifyException("@PreferenceRoom key name is duplicated.");
        } else {
            annotatedEntityMap.put(annotatedClazz.preferenceName, annotatedClazz);
            annotatedEntityTypeMap.put(annotatedClazz.typeName + ".class", annotatedClazz.preferenceName);
        }
    }

    private void checkDuplicatedPreferenceComponent(PreferenceComponentAnnotatedClass annotatedClazz) {
        if(annotatedComponentList.contains(annotatedClazz))
            throw new VerifyException("@PreferenceComponent is duplicated.");
        else {
            annotatedComponentList.add(annotatedClazz);
        }
    }

    private void showErrorLog(String message, Element element) {
        messager.printMessage(ERROR, StringUtils.getErrorMessagePrefix() + message, element);
    }
}
