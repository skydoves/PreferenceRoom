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

import androidx.annotation.NonNull;
import com.google.common.base.Strings;
import com.skydoves.preferenceroom.KeyName;
import com.skydoves.preferenceroom.TypeConverter;
import com.squareup.javapoet.TypeName;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.util.Elements;

@SuppressWarnings({"WeakerAccess", "StringConcatenationInsideStringBufferAppend"})
public class PreferenceKeyField {

  public final VariableElement variableElement;
  public final String packageName;
  public final TypeName typeName;
  public final String clazzName;
  public String typeStringName;
  public String keyName;
  public Object value;

  public String converter;
  public String converterPackage;
  public boolean isObjectField = false;

  public PreferenceKeyField(
      @NonNull VariableElement variableElement, @NonNull Elements elementUtils)
      throws IllegalAccessException {
    KeyName annotation_keyName = variableElement.getAnnotation(KeyName.class);
    this.variableElement = variableElement;
    PackageElement packageElement = elementUtils.getPackageOf(variableElement);
    this.packageName =
        packageElement.isUnnamed() ? null : packageElement.getQualifiedName().toString();
    this.typeName = TypeName.get(variableElement.asType());
    this.clazzName = variableElement.getSimpleName().toString();
    this.value = variableElement.getConstantValue();
    setTypeStringName();

    if (annotation_keyName != null)
      this.keyName =
          Strings.isNullOrEmpty(annotation_keyName.value())
              ? StringUtils.toUpperCamel(this.clazzName)
              : annotation_keyName.value();
    else this.keyName = StringUtils.toUpperCamel(this.clazzName);

    if (this.isObjectField) {
      variableElement
          .getAnnotationMirrors()
          .stream()
          .filter(
              annotationMirror ->
                  TypeName.get(annotationMirror.getAnnotationType())
                      .equals(TypeName.get(TypeConverter.class)))
          .forEach(
              annotationMirror ->
                  annotationMirror
                      .getElementValues()
                      .forEach(
                          (type, value) -> {
                            String[] split = value.getValue().toString().split("\\.");
                            StringBuilder builder = new StringBuilder();
                            for (int i = 0; i < split.length - 1; i++)
                              builder.append(split[i] + ".");
                            this.converterPackage =
                                builder.toString().substring(0, builder.toString().length() - 1);
                            this.converter = split[split.length - 1];
                          }));
    }

    if (variableElement.getModifiers().contains(Modifier.PRIVATE)) {
      throw new IllegalAccessException(
          String.format("Field \'%s\' should not be private.", variableElement.getSimpleName()));
    } else if (!this.isObjectField && !variableElement.getModifiers().contains(Modifier.FINAL)) {
      throw new IllegalAccessException(
          String.format("Field \'%s\' should be final.", variableElement.getSimpleName()));
    }
  }

  private void setTypeStringName() throws IllegalAccessException {
    if (this.typeName.equals(TypeName.BOOLEAN)) this.typeStringName = "Boolean";
    else if (this.typeName.equals(TypeName.INT)) this.typeStringName = "Int";
    else if (this.typeName.equals(TypeName.FLOAT)) this.typeStringName = "Float";
    else if (this.typeName.equals(TypeName.LONG)) this.typeStringName = "Long";
    else if (this.typeName.equals(TypeName.get(String.class))) this.typeStringName = "String";
    else if (variableElement.getAnnotation(TypeConverter.class) == null)
      throw new IllegalAccessException(
          String.format(
              "Field \'%s\' can not use %s type. \nObjects should be annotated with '@TypeConverter'.",
              variableElement.getSimpleName(), this.typeName.toString()));
    else {
      this.typeStringName = "String";
      this.isObjectField = true;
    }
  }
}
