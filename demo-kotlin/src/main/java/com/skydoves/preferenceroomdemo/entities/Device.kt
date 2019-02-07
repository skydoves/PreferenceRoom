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
import com.skydoves.preferenceroomdemo.utils.SecurityUtils

/**
 * Developed by skydoves on 2017-02-07.
 * Copyright (c) 2017 skydoves rights reserved.
 */

@PreferenceEntity("UserDevice")
open class Device {
    @KeyName("version")
    @JvmField val deviceVersion: String? = null

    @KeyName("uuid")
    @JvmField val userUUID: String? = null

    /**
     * preference putter function example about uuid with encrypt AES.
     * @param uuid function in
     * @return function out
     */
    @PreferenceFunction("uuid")
    open fun putUuidFunction(uuid: String?): String? {
        return SecurityUtils.encrypt(uuid)
    }

    /**
     * preference putter function example about uuid with decrypt AES.
     * @param uuid function in
     * @return function out
     */
    @PreferenceFunction("uuid")
    open fun getUuidFunction(uuid: String?): String? {
        return SecurityUtils.decrypt(uuid)
    }
}
