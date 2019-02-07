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

package com.skydoves.preferenceroomdemo.converters;

import com.google.gson.Gson;
import com.skydoves.preferenceroom.PreferenceTypeConverter;

public class BaseGsonConverter<T> extends PreferenceTypeConverter<T> {

  private final Gson gson;

  /** default constructor will be called by PreferenceRoom */
  public BaseGsonConverter(Class<T> clazz) {
    super(clazz);
    this.gson = new Gson();
  }

  @Override
  public String convertObject(T object) {
    return gson.toJson(object);
  }

  @Override
  public T convertType(String string) {
    return gson.fromJson(string, clazz);
  }
}
