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

import androidx.annotation.Nullable;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.MethodSpec;

import java.util.ArrayList;
import java.util.List;

import static javax.lang.model.element.Modifier.PUBLIC;

@SuppressWarnings("WeakerAccess")
public class PreferenceFieldMethodGenerator {

    private final PreferenceKeyField keyField;
    private final PreferenceEntityAnnotatedClass annotatedEntityClazz;
    private final String preference;

    private static final String SETTER_PREFIX = "put";
    private static final String GETTER_PREFIX = "get";
    private static final String KEYNAME_POSTFIX = "KeyName";
    private static final String HAS_PREFIX = "contains";
    private static final String REMOVE_PREFIX = "remove";
    private static final String INSTANCE_CONVERTER = "converter";

    private static final String EDIT_METHOD = "edit()";
    private static final String APPLY_METHOD = "apply()";

    public PreferenceFieldMethodGenerator(PreferenceKeyField keyField, PreferenceEntityAnnotatedClass annotatedClass, String preference) {
        this.keyField = keyField;
        this.annotatedEntityClazz = annotatedClass;
        this.preference = preference;
    }

    public List<MethodSpec> getFieldMethods() {
        List<MethodSpec> methodSpecs = new ArrayList<>();

        if(!keyField.isObjectField) {
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
        return MethodSpec.methodBuilder(getGetterPrefixName())
                .addModifiers(PUBLIC)
                .addAnnotation(Nullable.class)
                .addStatement("return " + getGetterStatement(), preference, keyField.keyName, keyField.value)
                .returns(keyField.typeName)
                .build();
    }

    private MethodSpec generateSetter() {
        return MethodSpec.methodBuilder(getSetterPrefixName())
                .addModifiers(PUBLIC)
                .addParameter(keyField.typeName, keyField.keyName.toLowerCase())
                .addStatement(getSetterStatement(), preference, EDIT_METHOD, keyField.keyName, keyField.keyName.toLowerCase(), APPLY_METHOD)
                .build();
    }

    private MethodSpec generateObjectGetter() {
        ClassName converterClazz = ClassName.get(keyField.converterPackage, keyField.converter);
        return MethodSpec.methodBuilder(getGetterPrefixName())
                .addModifiers(PUBLIC)
                .addAnnotation(Nullable.class)
                .addStatement("$T $N = new $T($T.class)", converterClazz, INSTANCE_CONVERTER, converterClazz, keyField.typeName.box())
                .addStatement("return ($T)" + getObjectGetterStatement(), keyField.typeName.box(), INSTANCE_CONVERTER, preference, keyField.keyName, keyField.value)
                .returns(keyField.typeName)
                .build();
    }

    private MethodSpec generateObjectSetter() {
        ClassName converterClazz = ClassName.get(keyField.converterPackage, keyField.converter);
        return MethodSpec.methodBuilder(getSetterPrefixName())
                .addModifiers(PUBLIC)
                .addParameter(keyField.typeName, keyField.keyName.toLowerCase())
                .addStatement("$T $N = new $T($T.class)", converterClazz, INSTANCE_CONVERTER, converterClazz, keyField.typeName.box())
                .addStatement(getSetterStatement(), preference, EDIT_METHOD, keyField.keyName, INSTANCE_CONVERTER + ".convertObject(" + keyField.keyName.toLowerCase() + ")", APPLY_METHOD)
                .build();
    }

    private MethodSpec generateObjectKeyNameSpec() {
        return MethodSpec.methodBuilder(getKeynamePostfixName())
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
                .addStatement("$N.$N.remove($S).$N", preference, EDIT_METHOD, keyField.keyName, APPLY_METHOD)
                .build();
    }

    private String getGetterPrefixName() {
        return GETTER_PREFIX + StringUtils.toUpperCamel(this.keyField.keyName);
    }

    private String getSetterPrefixName() {
        return SETTER_PREFIX + StringUtils.toUpperCamel(this.keyField.keyName);
    }

    private String getKeynamePostfixName() {
        return this.keyField.keyName + KEYNAME_POSTFIX;
    }

    private String getContainsPrefixName() {
        return HAS_PREFIX + StringUtils.toUpperCamel(this.keyField.keyName);
    }

    private String getRemovePrefixName() {
        return REMOVE_PREFIX + StringUtils.toUpperCamel(this.keyField.keyName);
    }

    private String getGetterTypeMethodName() {
        return GETTER_PREFIX + StringUtils.toUpperCamel(this.keyField.typeStringName);
    }

    private String getSetterTypeMethodName() {
        return SETTER_PREFIX + StringUtils.toUpperCamel(this.keyField.typeStringName);
    }

    private String getGetterStatement() {
        if(annotatedEntityClazz.getterFunctionsList.containsKey(keyField.keyName)) {
            String superMethodName =  annotatedEntityClazz.getterFunctionsList.get(keyField.keyName).getSimpleName().toString();
            if (keyField.value instanceof String)
                return String.format("super.%s($N.getString($S, $S))", superMethodName);
            else if (keyField.value instanceof Float)
                return String.format("super.%s($N." + getGetterTypeMethodName() + "($S, $Lf))", superMethodName);
            else
                return String.format("super.%s($N." + getGetterTypeMethodName() + "($S, $L))", superMethodName);
        } else {
            if (keyField.value instanceof String)
                return "$N.getString($S, $S)";
            else if (keyField.value instanceof Float)
                return "$N." + getGetterTypeMethodName() + "($S, $Lf)";
            else
                return "$N." + getGetterTypeMethodName() + "($S, $L)";
        }
    }

    private String getObjectGetterStatement() {
        if(annotatedEntityClazz.getterFunctionsList.containsKey(keyField.keyName)) {
            String superMethodName = annotatedEntityClazz.getterFunctionsList.get(keyField.keyName).getSimpleName().toString();
            if (keyField.value instanceof String)
                return String.format("super.%s($N.convertType($N.getString($S, $S)))", superMethodName);
            else if (keyField.value instanceof Float)
                return String.format("super.%s($N.convertType($N." + getGetterTypeMethodName() + "($S, $Lf)))", superMethodName);
            else
                return String.format("super.%s($N.convertType($N." + getGetterTypeMethodName() + "($S, $L))", superMethodName);
        } else {
            if (keyField.value instanceof String)
                return "$N.convertType($N.getString($S, $S))";
            else if (keyField.value instanceof Float)
                return "$N.convertType($N." + getGetterTypeMethodName() + "($S, $Lf))";
            else
                return "$N.convertType($N." + getGetterTypeMethodName() + "($S, $L))";
        }
    }

    private String getSetterStatement() {
        if(annotatedEntityClazz.setterFunctionsList.containsKey(keyField.keyName)) {
            return String.format("$N.$N." + getSetterTypeMethodName() + "($S, super.%s($N)).$N", annotatedEntityClazz.setterFunctionsList.get(keyField.keyName).getSimpleName());
        } else
            return "$N.$N." + getSetterTypeMethodName() + "($S, $N).$N";
    }
}
