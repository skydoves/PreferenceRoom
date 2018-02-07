package com.skydoves.preferenceroomdemo.converters

import com.google.gson.Gson
import com.skydoves.preferenceroom.PreferenceTypeConverter

/**
 * Developed by skydoves on 2018-02-06.
 * Copyright (c) 2017 skydoves rights reserved.
 */

class BaseGsonConverter<T>(clazz: Class<T>) : PreferenceTypeConverter<T>(clazz) {

    private val gson: Gson

    init {
        this.gson = Gson()
    }

    override fun convertObject(s_object: T): String {
        return gson.toJson(s_object)
    }

    override fun convertType(string: String): T {
        return gson.fromJson(string, clazz)
    }
}
