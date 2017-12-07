package com.skydoves.preferenceroomdemo.entities;

import com.skydoves.preferenceroom.KeyName;
import com.skydoves.preferenceroom.PreferenceEntity;
import com.skydoves.preferenceroom.PreferenceFunction;
import com.skydoves.preferenceroomdemo.utils.SecurityUtils;

/**
 * Developed by skydoves on 2017-11-20.
 * Copyright (c) 2017 skydoves rights reserved.
 */

@PreferenceEntity(name = "UserDevice")
public class Device {
    @KeyName(name = "version")
    public final String deviceVersion = null;

    @KeyName(name = "uuid")
    public final String userUUID = null;

    /**
     * preference putter function example about uuid with encrypt AES.
     * @param uuid function in
     * @return function out
     */
    @PreferenceFunction(keyname = "uuid")
    public String putUuidFunction(String uuid) {
        return SecurityUtils.encrypt(uuid);
    }

    /**
     * preference putter function example about uuid with decrypt AES.
     * @param uuid function in
     * @return function out
     */
    @PreferenceFunction(keyname = "uuid")
    public String getUuidFunction(String uuid) {
        return SecurityUtils.decrypt(uuid);
    }
}
