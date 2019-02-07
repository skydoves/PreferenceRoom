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

import com.skydoves.preferenceroom.PreferenceTypeConverter;
import com.skydoves.preferenceroomdemo.models.PrivateInfo;

public class PrivateInfoConverter extends PreferenceTypeConverter<PrivateInfo> {

  public PrivateInfoConverter(Class<PrivateInfo> clazz) {
    super(clazz);
  }

  @Override
  public String convertObject(PrivateInfo privateInfo) {
    return privateInfo.getName() + "," + privateInfo.getAge();
  }

  @Override
  public PrivateInfo convertType(String string) {
    if (string == null) return new PrivateInfo("null", 0);
    String[] information = string.split(",");
    return new PrivateInfo(information[0], Integer.parseInt(information[1]));
  }
}
