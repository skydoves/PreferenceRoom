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

import static javax.tools.Diagnostic.Kind.ERROR;

import com.google.auto.service.AutoService;
import com.google.common.base.VerifyException;
import com.skydoves.preferenceroom.DefaultPreference;
import com.skydoves.preferenceroom.InjectPreference;
import com.skydoves.preferenceroom.KeyName;
import com.skydoves.preferenceroom.PreferenceComponent;
import com.skydoves.preferenceroom.PreferenceEntity;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.TypeSpec;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;

@SuppressWarnings("unused")
@AutoService(Processor.class)
public class PreferenceRoomProcessor extends AbstractProcessor {

  private Map<String, String> annotatedEntityNameMap;
  private Map<String, PreferenceEntityAnnotatedClass> annotatedEntityMap;
  private List<PreferenceComponentAnnotatedClass> annotatedComponentList;
  private Messager messager;

  @Override
  public synchronized void init(ProcessingEnvironment processingEnv) {
    super.init(processingEnv);
    annotatedEntityMap = new HashMap<>();
    annotatedEntityNameMap = new HashMap<>();
    annotatedComponentList = new ArrayList<>();
    messager = processingEnv.getMessager();
  }

  @Override
  public Set<String> getSupportedAnnotationTypes() {
    Set<String> supportedTypes = new HashSet<>();
    supportedTypes.add(PreferenceComponent.class.getCanonicalName());
    supportedTypes.add(PreferenceEntity.class.getCanonicalName());
    supportedTypes.add(DefaultPreference.class.getCanonicalName());
    supportedTypes.add(KeyName.class.getCanonicalName());
    supportedTypes.add(InjectPreference.class.getCanonicalName());
    return supportedTypes;
  }

  @Override
  public SourceVersion getSupportedSourceVersion() {
    return SourceVersion.latest();
  }

  @Override
  public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
    if (annotations.isEmpty()) {
      return true;
    }

    roundEnv
        .getElementsAnnotatedWith(PreferenceEntity.class)
        .stream()
        .map(annotatedType -> (TypeElement) annotatedType)
        .forEach(
            annotatedType -> {
              try {
                checkValidEntityType(annotatedType);
                processEntity(annotatedType);
              } catch (IllegalAccessException e) {
                showErrorLog(e.getMessage(), annotatedType);
              }
            });

    roundEnv
        .getElementsAnnotatedWith(PreferenceComponent.class)
        .stream()
        .map(annotatedType -> (TypeElement) annotatedType)
        .forEach(
            annotatedType -> {
              try {
                checkValidComponentType(annotatedType);
                processComponent(annotatedType);
              } catch (IllegalAccessException e) {
                showErrorLog(e.getMessage(), annotatedType);
              }
            });

    roundEnv
        .getElementsAnnotatedWith(InjectPreference.class)
        .stream()
        .filter(variable -> variable instanceof VariableElement)
        .map(variable -> (VariableElement) variable)
        .forEach(
            variable -> {
              try {
                if (!variable.getModifiers().contains(Modifier.PUBLIC)) {
                  throw new IllegalAccessException(
                      "annotated with @InjectPreference field's modifier should be public");
                }
              } catch (IllegalAccessException e) {
                showErrorLog(e.getMessage(), variable);
              }
            });

    annotatedComponentList.forEach(this::processInjector);

    return true;
  }

  private void processEntity(TypeElement annotatedType) throws VerifyException {
    try {
      PreferenceEntityAnnotatedClass annotatedClazz =
          new PreferenceEntityAnnotatedClass(annotatedType, processingEnv.getElementUtils());
      checkDuplicatedPreferenceEntity(annotatedClazz);
      generateProcessEntity(annotatedClazz);
    } catch (VerifyException e) {
      showErrorLog(e.getMessage(), annotatedType);
      e.printStackTrace();
    }
  }

  private void processComponent(TypeElement annotatedType) throws VerifyException {
    try {
      PreferenceComponentAnnotatedClass annotatedClazz =
          new PreferenceComponentAnnotatedClass(
              annotatedType, processingEnv.getElementUtils(), annotatedEntityNameMap);
      checkDuplicatedPreferenceComponent(annotatedClazz);
      generateProcessComponent(annotatedClazz);
    } catch (VerifyException e) {
      showErrorLog(e.getMessage(), annotatedType);
      e.printStackTrace();
    }
  }

  private void processInjector(PreferenceComponentAnnotatedClass annotatedClass)
      throws VerifyException {
    try {
      annotatedClass
          .annotatedElement
          .getEnclosedElements()
          .stream()
          .filter(element -> element instanceof ExecutableElement)
          .map(element -> (ExecutableElement) element)
          .forEach(
              method -> {
                MethodSpec methodSpec = MethodSpec.overriding(method).build();
                ParameterSpec parameterSpec = methodSpec.parameters.get(0);
                TypeElement injectedElement =
                    processingEnv.getElementUtils().getTypeElement(parameterSpec.type.toString());
                generateProcessInjector(annotatedClass, injectedElement);
              });
    } catch (VerifyException e) {
      showErrorLog(e.getMessage(), annotatedClass.annotatedElement);
      e.printStackTrace();
    }
  }

  private void generateProcessEntity(PreferenceEntityAnnotatedClass annotatedClass) {
    try {
      TypeSpec annotatedClazz =
          (new PreferenceEntityGenerator(annotatedClass, processingEnv.getElementUtils()))
              .generate();
      JavaFile.builder(annotatedClass.packageName, annotatedClazz)
          .build()
          .writeTo(processingEnv.getFiler());
    } catch (IOException e) {
      // ignore ;)
    }
  }

  private void generateProcessComponent(PreferenceComponentAnnotatedClass annotatedClass) {
    try {
      TypeSpec annotatedClazz =
          (new PreferenceComponentGenerator(
                  annotatedClass, annotatedEntityMap, processingEnv.getElementUtils()))
              .generate();
      JavaFile.builder(annotatedClass.packageName, annotatedClazz)
          .build()
          .writeTo(processingEnv.getFiler());
    } catch (IOException e) {
      // ignore >.<
    }
  }

  private void generateProcessInjector(
      PreferenceComponentAnnotatedClass annotatedClass, TypeElement injectedElement) {
    try {
      InjectorGenerator injectorGenerator =
          new InjectorGenerator(annotatedClass, injectedElement, processingEnv.getElementUtils());
      TypeSpec injectorSpec = injectorGenerator.generate();
      JavaFile.builder(injectorGenerator.packageName, injectorSpec)
          .build()
          .writeTo(processingEnv.getFiler());
    } catch (IOException e) {
      // ignore ^v^
    }
  }

  private void checkValidEntityType(TypeElement annotatedType) throws IllegalAccessException {
    if (!annotatedType.getKind().isClass()) {
      throw new IllegalAccessException("Only classes can be annotated with @PreferenceRoom");
    } else if (annotatedType.getModifiers().contains(Modifier.FINAL)) {
      showErrorLog("class modifier should not be final", annotatedType);
    } else if (annotatedType.getModifiers().contains(Modifier.PRIVATE)) {
      showErrorLog("class modifier should not be final", annotatedType);
    }
  }

  private void checkValidComponentType(TypeElement annotatedType) throws IllegalAccessException {
    if (!annotatedType.getKind().isInterface()) {
      throw new IllegalAccessException(
          "Only interfaces can be annotated with @PreferenceComponent");
    }
  }

  private void checkDuplicatedPreferenceEntity(PreferenceEntityAnnotatedClass annotatedClazz)
      throws VerifyException {
    if (annotatedEntityMap.containsKey(annotatedClazz.entityName)) {
      throw new VerifyException("@PreferenceRoom key value is duplicated.");
    } else {
      annotatedEntityMap.put(annotatedClazz.entityName, annotatedClazz);
      annotatedEntityNameMap.put(annotatedClazz.typeName + ".class", annotatedClazz.entityName);
    }
  }

  private void checkDuplicatedPreferenceComponent(
      PreferenceComponentAnnotatedClass annotatedClazz) {
    if (annotatedComponentList.contains(annotatedClazz))
      throw new VerifyException("@PreferenceComponent is duplicated.");
    else {
      annotatedComponentList.add(annotatedClazz);
    }
  }

  private void showErrorLog(String message, Element element) {
    messager.printMessage(ERROR, StringUtils.getErrorMessagePrefix() + message, element);
  }
}
