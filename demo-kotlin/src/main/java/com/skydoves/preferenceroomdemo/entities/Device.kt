package com.skydoves.preferenceroomdemo.entities

import com.skydoves.preferenceroom.KeyName
import com.skydoves.preferenceroom.PreferenceEntity
import com.skydoves.preferenceroom.PreferenceFunction
import com.skydoves.preferenceroomdemo.utils.SecurityUtils

/**
 * Developed by skydoves on 2017-02-07.
 * Copyright (c) 2017 skydoves rights reserved.
 */

@PreferenceEntity(name = "UserDevice")
open class Device {
    @KeyName(name = "version")
    @JvmField val deviceVersion: String? = null

    @KeyName(name = "uuid")
    @JvmField val userUUID: String? = null

    /**
     * preference putter function example about uuid with encrypt AES.
     * @param uuid function in
     * @return function out
     */
    @PreferenceFunction(keyname = "uuid")
    open fun putUuidFunction(uuid: String?): String? {
        return SecurityUtils.encrypt(uuid)
    }

    /**
     * preference putter function example about uuid with decrypt AES.
     * @param uuid function in
     * @return function out
     */
    @PreferenceFunction(keyname = "uuid")
    open fun getUuidFunction(uuid: String?): String? {
        return SecurityUtils.decrypt(uuid)
    }
}
