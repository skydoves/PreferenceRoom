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
import com.skydoves.preferenceroom.AESEncryption;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.MethodSpec;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings({"WeakerAccess", "SpellCheckingInspection"})
public class PreferenceFieldMethodGenerator {

  private final PreferenceKeyField keyField;
  private final PreferenceEntityAnnotatedClass annotatedEntityClazz;
  private final String preference;

  private static final String SETTER_PREFIX = "put";
  private static final String GETTER_PREFIX = "get";
  private static final String KEYNAME_POSTFIX = "KeyName";
  private static final String HAS_PREFIX = "contains";
  private static final String REMOVE_PREFIX = "remove";
  private static final String INSTANCE_CONVERTER = "value";

  private static final String EDIT_METHOD = "edit()";
  private static final String APPLY_METHOD = "apply()";

  public PreferenceFieldMethodGenerator(
      PreferenceKeyField keyField,
      PreferenceEntityAnnotatedClass annotatedClass,
      String preference) {
    this.keyField = keyField;
    this.annotatedEntityClazz = annotatedClass;
    this.preference = preference;
  }

  public List<MethodSpec> getFieldMethods() {
    List<MethodSpec> methodSpecs = new ArrayList<>();

    if (!keyField.isObjectField) {
      methodSpecs.add(generateGetter());
      methodSpecs.add(generateSetter());
    } else {
      methodSpecs.add(generateObjectGetter());
      methodSpecs.add(generateObjectSetter());
    }

    methodSpecs.add(generateObjectKeyNameSpec());
    methodSpecs.add(generateContainsSpec());
    methodSpecs.add(generateRemoveSpec());
    return methodSpecs;
  }

  private MethodSpec generateGetter() {
    MethodSpec.Builder builder =
        MethodSpec.methodBuilder(getGetterPrefixName())
            .addModifiers(PUBLIC)
            .addAnnotation(Nullable.class);
    if (isEncryption()) {
      builder.addStatement(
          "return " + getEncryptedGetterStatement(),
          AESEncryption.class,
          preference,
          keyField.keyName,
          keyField.value,
          keyField.value,
          getEncryptionKey());
    } else {
      builder.addStatement(
          "return " + getGetterStatement(), preference, keyField.keyName, keyField.value);
    }
    builder.returns(keyField.typeName);
    return builder.build();
  }

  private MethodSpec generateSetter() {
    MethodSpec.Builder builder =
        MethodSpec.methodBuilder(getSetterPrefixName())
            .addModifiers(PUBLIC)
            .addParameter(keyField.typeName, keyField.keyName.toLowerCase());
    if (isEncryption()) {
      builder.addStatement(
          getSetterEncryptStatement(),
          preference,
          EDIT_METHOD,
          keyField.keyName,
          AESEncryption.class,
          keyField.keyName.toLowerCase(),
          getEncryptionKey(),
          APPLY_METHOD);
    } else {
      builder.addStatement(
          getSetterStatement(),
          preference,
          EDIT_METHOD,
          keyField.keyName,
          keyField.keyName.toLowerCase(),
          APPLY_METHOD);
    }
    builder.addStatement(getOnChangedStatement());
    return builder.build();
  }

  private MethodSpec generateObjectGetter() {
    ClassName converterClazz = ClassName.get(keyField.converterPackage, keyField.converter);
    String typeName = keyField.typeName.box().toString();
    if (typeName.contains("<")) typeName = typeName.substring(0, typeName.indexOf("<"));
    MethodSpec.Builder builder =
        MethodSpec.methodBuilder(getGetterPrefixName())
            .addModifiers(PUBLIC)
            .addAnnotation(Nullable.class)
            .addStatement(
                "$T $N = new $T($N.class)",
                converterClazz,
                INSTANCE_CONVERTER,
                converterClazz,
                typeName);
    if (isEncryption()) {
      builder.addStatement(
          "return ($T)" + getObjectEncryptedGetterStatement(),
          keyField.typeName.box(),
          INSTANCE_CONVERTER,
          AESEncryption.class,
          preference,
          keyField.keyName,
          keyField.value,
          keyField.value,
          getEncryptionKey());
    } else {
      builder.addStatement(
          "return ($T)" + getObjectGetterStatement(),
          keyField.typeName.box(),
          INSTANCE_CONVERTER,
          preference,
          keyField.keyName,
          keyField.value);
    }
    builder.returns(keyField.typeName);
    return builder.build();
  }

  private MethodSpec generateObjectSetter() {
    ClassName converterClazz = ClassName.get(keyField.converterPackage, keyField.converter);
    String typeName = keyField.typeName.box().toString();
    if (typeName.contains("<")) typeName = typeName.substring(0, typeName.indexOf("<"));
    MethodSpec.Builder builder =
        MethodSpec.methodBuilder(getSetterPrefixName())
            .addModifiers(PUBLIC)
            .addParameter(keyField.typeName, keyField.keyName.toLowerCase())
            .addStatement(
                "$T $N = new $T($N.class)",
                converterClazz,
                INSTANCE_CONVERTER,
                converterClazz,
                typeName);
    if (isEncryption()) {
      builder.addStatement(
          getSetterEncryptStatement(),
          preference,
          EDIT_METHOD,
          keyField.keyName,
          AESEncryption.class,
          INSTANCE_CONVERTER + ".convertObject(" + keyField.keyName.toLowerCase() + ")",
          getEncryptionKey(),
          APPLY_METHOD);
    } else {
      builder.addStatement(
          getSetterStatement(),
          preference,
          EDIT_METHOD,
          keyField.keyName,
          INSTANCE_CONVERTER + ".convertObject(" + keyField.keyName.toLowerCase() + ")",
          APPLY_METHOD);
    }
    builder.addStatement(getOnChangedStatement());
    return builder.build();
  }

  private MethodSpec generateObjectKeyNameSpec() {
    return MethodSpec.methodBuilder(getKeyNamePostfixName())
        .addModifiers(PUBLIC)
        .returns(String.class)
        .addStatement("return $S", keyField.keyName)
        .build();
  }

  private MethodSpec generateContainsSpec() {
    return MethodSpec.methodBuilder(getContainsPrefixName())
        .addModifiers(PUBLIC)
        .addStatement("return $N.contains($S)", preference, keyField.keyName)
        .returns(boolean.class)
        .build();
  }

  private MethodSpec generateRemoveSpec() {
    return MethodSpec.methodBuilder(getRemovePrefixName())
        .addModifiers(PUBLIC)
        .addStatement(
            "$N.$N.remove($S).$N", preference, EDIT_METHOD, keyField.keyName, APPLY_METHOD)
        .build();
  }

  private String getGetterPrefixName() {
    return GETTER_PREFIX + StringUtils.toUpperCamel(this.keyField.keyName);
  }

  private String getSetterPrefixName() {
    return SETTER_PREFIX + StringUtils.toUpperCamel(this.keyField.keyName);
  }

  private String getKeyNamePostfixName() {
    return this.keyField.keyName + KEYNAME_POSTFIX;
  }

  private String getContainsPrefixName() {
    return HAS_PREFIX + StringUtils.toUpperCamel(this.keyField.keyName);
  }

  private String getRemovePrefixName() {
    return REMOVE_PREFIX + StringUtils.toUpperCamel(this.keyField.keyName);
  }

  private String getGetterTypeMethodName() {
    if (isEncryption()) {
      return GETTER_PREFIX + StringUtils.toUpperCamel("String");
    } else {
      return GETTER_PREFIX + StringUtils.toUpperCamel(this.keyField.typeStringName);
    }
  }

  private String getSetterTypeMethodName() {
    if (isEncryption()) {
      return SETTER_PREFIX + StringUtils.toUpperCamel("String");
    } else {
      return SETTER_PREFIX + StringUtils.toUpperCamel(this.keyField.typeStringName);
    }
  }

  private String getGetterStatement() {
    if (annotatedEntityClazz.getterFunctionsList.containsKey(keyField.keyName)) {
      String superMethodName =
          annotatedEntityClazz.getterFunctionsList.get(keyField.keyName).getSimpleName().toString();
      if (keyField.value instanceof String)
        return String.format("super.%s($N.getString($S, $S))", superMethodName);
      else if (keyField.value instanceof Float)
        return String.format(
            "super.%s($N." + getGetterTypeMethodName() + "($S, $Lf))", superMethodName);
      else
        return String.format(
            "super.%s($N." + getGetterTypeMethodName() + "($S, $L))", superMethodName);
    } else {
      if (keyField.value instanceof String) return "$N.getString($S, $S)";
      else if (keyField.value instanceof Float)
        return "$N." + getGetterTypeMethodName() + "($S, $Lf)";
      else return "$N." + getGetterTypeMethodName() + "($S, $L)";
    }
  }

  private String getEncryptedGetterStatement() {
    if (annotatedEntityClazz.getterFunctionsList.containsKey(keyField.keyName)) {
      String superMethodName =
          annotatedEntityClazz.getterFunctionsList.get(keyField.keyName).getSimpleName().toString();
      return wrapperClassFormatting(
          String.format("super.%s($T.decrypt($N.getString($S, $S), $S, $S))", superMethodName));
    }
    return wrapperClassFormatting("$T.decrypt($N.getString($S, $S), $S, $S)");
  }

  private String getObjectGetterStatement() {
    if (annotatedEntityClazz.getterFunctionsList.containsKey(keyField.keyName)) {
      String superMethodName =
          annotatedEntityClazz.getterFunctionsList.get(keyField.keyName).getSimpleName().toString();
      if (keyField.value instanceof String)
        return String.format("super.%s($N.convertType($N.getString($S, $S)))", superMethodName);
      else if (keyField.value instanceof Float)
        return String.format(
            "super.%s($N.convertType($N." + getGetterTypeMethodName() + "($S, $Lf)))",
            superMethodName);
      else
        return String.format(
            "super.%s($N.convertType($N." + getGetterTypeMethodName() + "($S, $L))",
            superMethodName);
    } else {
      if (keyField.value instanceof String) return "$N.convertType($N.getString($S, $S))";
      else if (keyField.value instanceof Float)
        return "$N.convertType($N." + getGetterTypeMethodName() + "($S, $Lf))";
      else return "$N.convertType($N." + getGetterTypeMethodName() + "($S, $L))";
    }
  }

  private String getObjectEncryptedGetterStatement() {
    if (annotatedEntityClazz.getterFunctionsList.containsKey(keyField.keyName)) {
      String superMethodName =
          annotatedEntityClazz.getterFunctionsList.get(keyField.keyName).getSimpleName().toString();
      return String.format(
          "super.%s($N.convertType($T.decrypt($N.getString($S, $S), $S, $S)))", superMethodName);
    }
    return "$N.convertType($T.decrypt($N.getString($S, $S), $S, $S))";
  }

  private String getSetterStatement() {
    if (annotatedEntityClazz.setterFunctionsList.containsKey(keyField.keyName)) {
      return String.format(
          "$N.$N." + getSetterTypeMethodName() + "($S, super.%s($N)).$N",
          annotatedEntityClazz.setterFunctionsList.get(keyField.keyName).getSimpleName());
    } else return "$N.$N." + getSetterTypeMethodName() + "($S, $N).$N";
  }

  private String getSetterEncryptStatement() {
    if (annotatedEntityClazz.setterFunctionsList.containsKey(keyField.keyName)) {
      return String.format(
          "$N.$N."
              + getSetterTypeMethodName()
              + "($S, $T.encrypt(String.valueOf(super.%s($N)), $S)).$N",
          annotatedEntityClazz.setterFunctionsList.get(keyField.keyName).getSimpleName());
    } else
      return "$N.$N." + getSetterTypeMethodName() + "($S, $T.encrypt(String.valueOf($N), $S)).$N";
  }

  private String getOnChangedStatement() {
    String onChangeListener =
        keyField.keyName + PreferenceChangeListenerGenerator.CHANGED_LISTENER_POSTFIX;
    PreferenceChangeListenerGenerator generator = new PreferenceChangeListenerGenerator(keyField);
    return "if("
        + onChangeListener
        + " != null) "
        + "for ("
        + generator.getClazzName()
        + " "
        + "listener : "
        + onChangeListener
        + ") "
        + "listener."
        + PreferenceChangeListenerGenerator.CHANGED_ABSTRACT_METHOD
        + "("
        + keyField.keyName.toLowerCase()
        + ")";
  }

  private boolean isEncryption() {
    return annotatedEntityClazz.isEncryption;
  }

  private String getEncryptionKey() {
    return annotatedEntityClazz.encryptionKey;
  }

  private String wrapperClassFormatting(String statement) {
    if (keyField.value instanceof Boolean) {
      return String.format("Boolean.valueOf(%s).booleanValue()", statement);
    } else if (keyField.value instanceof Integer) {
      return String.format("Integer.valueOf(%s).intValue()", statement);
    } else if (keyField.value instanceof Float) {
      return String.format("Float.valueOf(%s).floatValue()", statement);
    }
    return statement;
  }
}
