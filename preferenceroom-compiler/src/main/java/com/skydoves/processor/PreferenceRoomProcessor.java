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
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;

import static javax.tools.Diagnostic.Kind.ERROR;
import static javax.tools.Diagnostic.Kind.NOTE;

@SupportedSourceVersion(SourceVersion.RELEASE_8)
@SupportedAnnotationTypes({
        "com.skydoves.preferenceroom.Preference",
        "com.skydoves.preferenceroom.KeyName",
        "com.skydoves.preferenceroom.PreferenceComponent"})
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
                        messager.printMessage(ERROR, e.getMessage(), annotatedType);
                    }
                });

        roundEnv.getElementsAnnotatedWith(PreferenceComponent.class).stream()
                .map(annotatedType -> (TypeElement) annotatedType)
                .forEach(annotatedType -> {
                    try {
                        checkValidComponentType(annotatedType);
                        processComponent(annotatedType);
                    } catch (IllegalAccessException e) {
                        messager.printMessage(ERROR, e.getMessage(), annotatedType);
                    }
                });

        return true;
    }

    private void processEntity(TypeElement annotatedType) throws VerifyException {
        try {
            PreferenceEntityAnnotatedClass annotatedClazz = new PreferenceEntityAnnotatedClass(annotatedType, processingEnv.getElementUtils());
            checkDuplicatedPreferenceEntity(annotatedClazz);
            generateProcessEntity(annotatedClazz);
        } catch (VerifyException e) {
            messager.printMessage(ERROR, e.getMessage(), annotatedType);
            e.printStackTrace();
        }
    }

    private void processComponent(TypeElement annotatedType) throws VerifyException {
        try {
            PreferenceComponentAnnotatedClass annotatedClazz = new PreferenceComponentAnnotatedClass(annotatedType, processingEnv.getElementUtils(), annotatedEntityTypeMap);
            checkDuplicatedPreferenceComponent(annotatedClazz);
            generateProcessComponent(annotatedClazz);
        } catch (VerifyException e) {
            messager.printMessage(ERROR, e.getMessage(), annotatedType);
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

    private void checkValidEntityType(TypeElement annotatedType) throws IllegalAccessException {
        if(!annotatedType.getKind().isClass()) {
            throw new IllegalAccessException("Only classes can be annotated with @PreferenceRoom");
        } else if(annotatedType.getModifiers().contains(Modifier.FINAL)) {
            messager.printMessage(ERROR, "class modifier should not be final", annotatedType);
        } else if(annotatedType.getModifiers().contains(Modifier.PRIVATE)) {
            messager.printMessage(ERROR, "class modifier should not be final", annotatedType);
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
}
