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

import com.skydoves.preferenceroom.EncryptEntity;
import com.skydoves.preferenceroom.KeyName;
import com.skydoves.preferenceroom.PreferenceEntity;
import com.skydoves.preferenceroom.PreferenceFunction;
import com.skydoves.preferenceroom.TypeConverter;
import com.skydoves.preferenceroomdemo.converters.BaseGsonConverter;
import com.skydoves.preferenceroomdemo.converters.PrivateInfoConverter;
import com.skydoves.preferenceroomdemo.models.Pet;
import com.skydoves.preferenceroomdemo.models.PrivateInfo;

@EncryptEntity("1234567890ABCDFG")
@PreferenceEntity("UserProfile")
public class Profile {
  @KeyName("nickname")
  protected final String userNickName = "skydoves";

  /** key value will be 'Login'. (login's camel uppercase) */
  protected final boolean login = false;

  @KeyName("visits")
  protected final int visitCount = 1;

  @KeyName("userinfo")
  @TypeConverter(value = PrivateInfoConverter.class)
  protected PrivateInfo privateInfo;

  /** value used with gson. */
  @KeyName("userPet")
  @TypeConverter(BaseGsonConverter.class)
  protected Pet userPetInfo;

  /**
   * preference putter function about userNickName.
   *
   * @param nickname function in
   * @return function out
   */
  @PreferenceFunction("nickname")
  public String putUserNickFunction(String nickname) {
    return "Hello, " + nickname;
  }

  /**
   * preference getter function about userNickName.
   *
   * @param nickname function in
   * @return function out
   */
  @PreferenceFunction("nickname")
  public String getUserNickFunction(String nickname) {
    return nickname + "!!!";
  }

  /**
   * preference putter function example about visitCount's auto increment.
   *
   * @param count function in
   * @return function out
   */
  @PreferenceFunction("visits")
  public int putVisitCountFunction(int count) {
    return ++count;
  }
}
