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

package com.skydoves.preferenceroomdemo.entities

import com.skydoves.preferenceroom.KeyName
import com.skydoves.preferenceroom.PreferenceEntity
import com.skydoves.preferenceroom.PreferenceFunction
import com.skydoves.preferenceroom.TypeConverter
import com.skydoves.preferenceroomdemo.converters.PetConverter
import com.skydoves.preferenceroomdemo.converters.PrivateInfoConverter
import com.skydoves.preferenceroomdemo.models.Pet
import com.skydoves.preferenceroomdemo.models.PrivateInfo

/**
 * Developed by skydoves on 2018-02-07.
 * Copyright (c) 2017 skydoves rights reserved.
 */

@PreferenceEntity(name = "UserProfile")
open class Profile {
    @KeyName(name = "nickname")
    @JvmField val userNickName = "skydoves"

    /**
     * key name will be 'Login'. (login's camel uppercase)
     */
    @JvmField val login = false

    @KeyName(name = "visits")
    @JvmField val visitCount = 1

    @KeyName(name = "userinfo")
    @TypeConverter(converter = PrivateInfoConverter::class)
    @JvmField val privateInfo: PrivateInfo? = null

    /**
     * converter used with gson.
     */
    @KeyName(name = "userPet")
    @TypeConverter(converter = PetConverter::class)
    @JvmField val userPetInfo: Pet? = null

    /**
     * preference putter function about userNickName.
     * @param nickname function in
     * @return function out
     */
    @PreferenceFunction(keyname = "nickname")
    open fun putUserNickFunction(nickname: String): String {
        return "Hello, " + nickname
    }

    /**
     * preference getter function about userNickName.
     * @param nickname function in
     * @return function out
     */
    @PreferenceFunction(keyname = "nickname")
    open fun getUserNickFunction(nickname: String): String {
        return nickname + "!!!"
    }

    /**
     * preference putter function example about visitCount's auto increment.
     * @param count function in
     * @return function out
     */
    @PreferenceFunction(keyname = "visits")
    open fun putVisitCountFunction(count: Int): Int {
        var count = count
        return ++count
    }
}
