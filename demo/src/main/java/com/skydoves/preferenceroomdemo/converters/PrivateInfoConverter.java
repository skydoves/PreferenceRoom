package com.skydoves.preferenceroomdemo.converters;

import com.skydoves.preferenceroom.PreferenceTypeConverter;
import com.skydoves.preferenceroomdemo.models.PrivateInfo;

/**
 * Developed by skydoves on 2017-11-25.
 * Copyright (c) 2017 skydoves rights reserved.
 */

public class PrivateInfoConverter extends PreferenceTypeConverter<PrivateInfo> {

    @Override
    public String convertObject(PrivateInfo privateInfo) {
        return privateInfo.getName() + "," + privateInfo.getAge();
    }

    @Override
    public PrivateInfo convertType(String string) {
        if(string == null) return new PrivateInfo("null",0);
        String[] information = string.split(",");
        return new PrivateInfo(information[0], Integer.parseInt(information[1]));
    }
}
