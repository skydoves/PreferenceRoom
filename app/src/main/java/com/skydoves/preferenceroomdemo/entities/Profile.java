package com.skydoves.preferenceroomdemo.entities;

import com.skydoves.preferenceroom.KeyName;
import com.skydoves.preferenceroom.PreferenceEntity;
import com.skydoves.preferenceroom.PreferenceFunction;
import com.skydoves.preferenceroom.TypeConverter;
import com.skydoves.preferenceroomdemo.models.MyClass;
import com.skydoves.preferenceroomdemo.converters.MyClassConverter;

/**
 * Developed by skydoves on 2017-11-18.
 * Copyright (c) 2017 skydoves rights reserved.
 */

@PreferenceEntity(name = "UserProfile")
public class Profile {
    @KeyName(name = "login")
    protected final boolean isLoggedIn = false;
    @KeyName(name = "visit")
    protected final int visitCount = 12;
    @KeyName(name = "UserName")
    protected final String nickName = "skydoves";
    protected final float myFloat = 56.2f;
    protected final long cash = 1241224L;

    @KeyName(name = "YourClass")
    @TypeConverter(converter = MyClassConverter.class)
    protected MyClass myClass;

    @PreferenceFunction(keyname = "UserName")
    public String putUserNameFunction(String username) {
        return "hello " + username;
    }

    @PreferenceFunction(keyname = "UserName")
    public String getUserNameFunction(String username) {
        return username + "!!!";
    }
}
