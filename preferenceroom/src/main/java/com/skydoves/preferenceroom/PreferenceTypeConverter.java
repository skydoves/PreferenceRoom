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

/** PreferenceTypeConverter is an abstract class for saving object data through functions. */
public abstract class PreferenceTypeConverter<T> {

  public Class<T> clazz;

  public PreferenceTypeConverter(Class<T> clazz) {
    this.clazz = clazz;
  }

  /**
   * converts an object to string value for saving.
   *
   * @param object an object for saving.
   * @return converted string value.
   */
  public abstract String convertObject(T object);

  /**
   * converts a saved string value and recovers the original object.
   *
   * @param string saved string value.
   * @return recovered original object.
   */
  public abstract T convertType(String string);
}
