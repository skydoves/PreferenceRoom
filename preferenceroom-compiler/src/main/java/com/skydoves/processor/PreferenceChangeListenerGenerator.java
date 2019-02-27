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

import static javax.lang.model.element.Modifier.PUBLIC;

import androidx.annotation.Nullable;
import com.skydoves.preferenceroom.PreferenceChangedListener;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeSpec;
import java.util.List;
import javax.lang.model.element.Modifier;

@SuppressWarnings("WeakerAccess")
public class PreferenceChangeListenerGenerator {

  private final PreferenceKeyField keyField;

  public static final String CHANGED_LISTENER_POSTFIX = "OnChangedListener";
  public static final String CHANGED_ABSTRACT_METHOD = "onChanged";

  public PreferenceChangeListenerGenerator(PreferenceKeyField keyField) {
    this.keyField = keyField;
  }

  public TypeSpec generateInterface() {
    TypeSpec.Builder builder =
        TypeSpec.interfaceBuilder(getClazzName())
            .addModifiers(PUBLIC)
            .addSuperinterface(PreferenceChangedListener.class)
            .addMethod(getOnChangedSpec());
    return builder.build();
  }

  public FieldSpec generateField(String className) {
    return FieldSpec.builder(
            ParameterizedTypeName.get(ClassName.get(List.class), getInterfaceType(className)),
            getFieldName(),
            Modifier.PRIVATE)
        .addAnnotation(Nullable.class)
        .initializer("new ArrayList()")
        .build();
  }

  private MethodSpec getOnChangedSpec() {
    return MethodSpec.methodBuilder("onChanged")
        .addParameter(
            ParameterSpec.builder(keyField.typeName, keyField.keyName.toLowerCase()).build())
        .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
        .build();
  }

  public String getClazzName() {
    return StringUtils.toUpperCamel(keyField.keyName) + CHANGED_LISTENER_POSTFIX;
  }

  private String getFieldName() {
    return keyField.keyName + CHANGED_LISTENER_POSTFIX;
  }

  public ClassName getInterfaceType(String className) {
    return ClassName.get(keyField.packageName + "." + className, getClazzName());
  }
}
