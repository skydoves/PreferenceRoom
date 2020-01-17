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

package com.skydoves.preferenceroomdemo.converters

import com.skydoves.preferenceroom.PreferenceTypeConverter
import com.skydoves.preferenceroomdemo.models.PrivateInfo

/**
 * Developed by skydoves on 2017-11-25.
 * Copyright (c) 2017 skydoves rights reserved.
 */

class PrivateInfoConverter(clazz: Class<PrivateInfo>) : PreferenceTypeConverter<PrivateInfo>(
  clazz) {

  override fun convertObject(privateInfo: PrivateInfo): String {
    return privateInfo.name + "," + privateInfo.age
  }

  override fun convertType(string: String?): PrivateInfo {
    if (string == null) return PrivateInfo("null", 0)
    val information = string.split(",".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
    return PrivateInfo(information[0], Integer.parseInt(information[1]))
  }
}
