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

package com.skydoves.preferenceroomdemo.entities;

import com.skydoves.preferenceroom.KeyName;
import com.skydoves.preferenceroom.PreferenceEntity;
import com.skydoves.preferenceroom.PreferenceFunction;
import com.skydoves.preferenceroomdemo.utils.SecurityUtils;

@PreferenceEntity("UserDevice")
public class Device {
  @KeyName("version")
  public final String deviceVersion = null;

  @KeyName("uuid")
  public final String userUUID = null;

  /**
   * preference putter function example about uuid with encrypt AES.
   *
   * @param uuid function in
   * @return function out
   */
  @PreferenceFunction("uuid")
  public String putUuidFunction(String uuid) {
    return SecurityUtils.encrypt(uuid);
  }

  /**
   * preference putter function example about uuid with decrypt AES.
   *
   * @param uuid function in
   * @return function out
   */
  @PreferenceFunction("uuid")
  public String getUuidFunction(String uuid) {
    return SecurityUtils.decrypt(uuid);
  }
}
