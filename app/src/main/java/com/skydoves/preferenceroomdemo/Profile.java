package com.skydoves.preferenceroomdemo;

import com.skydoves.preferenceroom.KeyName;
import com.skydoves.preferenceroom.PreferenceEntity;
import com.skydoves.preferenceroom.TypeConverter;

/**
 * Developed by skydoves on 2017-11-18.
 * Copyright (c) 2017 skydoves rights reserved.
 */
@PreferenceEntity(name = "UserProfile")
public class Profile {
    @KeyName(name = "login")
    protected final boolean isLoggedIn = false;
    @KeyName(name = "visit")
    protected final int visiCount = 12;
    @KeyName(name = "UserName")
    protected final String nickName = "skydoves";
    protected final float myFloat = 56.2f;
    protected final long cash = 1241224L;

    @KeyName(name = "YourClass")
    @TypeConverter(converter = MyClassConverter.class)
    protected MyClass myClass;
}
