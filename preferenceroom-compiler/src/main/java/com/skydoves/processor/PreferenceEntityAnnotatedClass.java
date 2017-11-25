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

import android.support.annotation.NonNull;

import com.google.common.base.Strings;
import com.google.common.base.VerifyException;
import com.skydoves.preferenceroom.PreferenceEntity;
import com.squareup.javapoet.TypeName;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.util.Elements;

public class PreferenceEntityAnnotatedClass {

    public final String packageName;
    public final TypeElement annotatedElement;
    public final TypeName typeName;
    public final String clazzName;
    public final String preferenceName;
    public final List<PreferenceKeyField> keyFields;

    public PreferenceEntityAnnotatedClass(@NonNull TypeElement annotatedElement, @NonNull Elements elementUtils) throws VerifyException {
        PreferenceEntity preferenceRoom = annotatedElement.getAnnotation(PreferenceEntity.class);
        PackageElement packageElement = elementUtils.getPackageOf(annotatedElement);
        this.packageName = packageElement.isUnnamed() ? null : packageElement.getQualifiedName().toString();
        this.annotatedElement = annotatedElement;
        this.typeName = TypeName.get(annotatedElement.asType());
        this.clazzName = annotatedElement.getSimpleName().toString();
        this.preferenceName = Strings.isNullOrEmpty(preferenceRoom.name()) ? StringUtils.toUpperCamel(this.clazzName) : preferenceRoom.name();
        this.keyFields = new ArrayList<>();

        if(Strings.isNullOrEmpty(preferenceName)) {
            throw new VerifyException("You should specify PreferenceRoom class name.");
        }

        Map<String, String> checkMap = new HashMap<>();
        annotatedElement.getEnclosedElements().stream()
                .filter(variable -> variable instanceof VariableElement)
                .map(variable -> (VariableElement) variable)
                .forEach(variable -> {
                    try {
                        PreferenceKeyField keyField = new PreferenceKeyField(variable);

                        if(checkMap.get(keyField.keyName) != null) {
                            throw new VerifyException(String.format("\'%s\' key is already used in class.", keyField.keyName));
                        }

                        checkMap.put(keyField.keyName, keyField.clazzName);
                        keyFields.add(keyField);
                    } catch (IllegalAccessException e) {
                        throw new VerifyException(e.getMessage());
                    }
                });
    }
}
