package com.skydoves.preferenceroomdemo.entities;

import com.skydoves.preferenceroom.KeyName;
import com.skydoves.preferenceroom.PreferenceEntity;
import com.skydoves.preferenceroom.TypeConverter;
import com.skydoves.preferenceroomdemo.converters.BaseGsonConverter;
import com.skydoves.preferenceroomdemo.models.Pet;
import com.skydoves.preferenceroomdemo.models.PrivateInfo;

/**
 * Developed by skydoves on 2018-02-07.
 * Copyright (c) 2017 skydoves rights reserved.
 */

@PreferenceEntity(name = "ProfileBaseConverter")
public class UserProfileBaseConverter {
    @KeyName(name = "nickname")
    protected final String userNickName = "skydoves";

    /**
     * key name will be 'Login'. (login's camel uppercase)
     */
    protected final boolean login = false;

    @KeyName(name = "visits")
    protected final int visitCount = 1;

    @KeyName(name = "userinfo")
    @TypeConverter(converter = BaseGsonConverter.class)
    protected PrivateInfo privateInfo;

    @KeyName(name = "userPet")
    @TypeConverter(converter = BaseGsonConverter.class)
    protected Pet userPetInfo;

    @KeyName(name = "test")
    protected final String test = "test";
}
