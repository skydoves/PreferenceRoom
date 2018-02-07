package com.skydoves.preferenceroomdemo.converters;

import com.google.gson.Gson;
import com.skydoves.preferenceroom.PreferenceTypeConverter;

/**
 * Developed by skydoves on 2018-02-06.
 * Copyright (c) 2017 skydoves rights reserved.
 */

public class BaseGsonConverter<T> extends PreferenceTypeConverter<T> {

    private final Gson gson;

    /**
     * default constructor will be called by PreferenceRoom
     */
    public BaseGsonConverter(Class<T> clazz) {
        super(clazz);
        this.gson = new Gson();
    }

    @Override
    public String convertObject(T object) {
        return gson.toJson(object);
    }

    @Override
    public T convertType(String string) {
        return gson.fromJson(string, clazz);
    }
}
