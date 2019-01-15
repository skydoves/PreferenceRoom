/*
 * Copyright (C) 2019 skydoves
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

import com.skydoves.preferenceroom.PreferenceChangedListener;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.TypeSpec;

import javax.lang.model.element.Modifier;

import static javax.lang.model.element.Modifier.PUBLIC;

@SuppressWarnings("WeakerAccess")
public class PreferenceChangeListenerGenerator {

    private final PreferenceKeyField keyField;

    private static final String CHANGED_LISTENER_POSTFIX = "OnChangedListener";

    public PreferenceChangeListenerGenerator(PreferenceKeyField keyField) {
        this.keyField = keyField;
    }

    public TypeSpec generate() {
        TypeSpec.Builder builder = TypeSpec.interfaceBuilder(getClazzName())
                .addModifiers(PUBLIC)
                .addSuperinterface(PreferenceChangedListener.class)
                .addMethod(getOnChangedSpec());

        return builder.build();
    }

    private MethodSpec getOnChangedSpec() {
        return MethodSpec.methodBuilder("onChanged")
                .addParameter(ParameterSpec.builder(keyField.typeName, "value").build())
                .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
                .build();
    }

    private String getClazzName() {
        return StringUtils.toUpperCamel(keyField.keyName) + CHANGED_LISTENER_POSTFIX;
    }
}
