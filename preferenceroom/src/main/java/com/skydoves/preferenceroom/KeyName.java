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

package com.skydoves.preferenceroom;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.CLASS;

/**
 * Marks a field as an SharedPreference key. This field will have a mapping SharedPreference key with Upper camel case.
 */
@Documented
@Target(FIELD)
@Retention(CLASS)
public @interface KeyName {
    /**
     * Preference Key name in the SharedPreference. If not set, defaults to the field name with Upper camel case.
     *
     * @return The Preference key name of the SharedPreference.
     */
    String name() default "";
}
