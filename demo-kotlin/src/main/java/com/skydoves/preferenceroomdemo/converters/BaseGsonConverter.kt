package com.skydoves.preferenceroomdemo.converters

import com.google.gson.Gson
import com.skydoves.preferenceroom.PreferenceTypeConverter

/**
 * Developed by skydoves on 2018-02-06.
 * Copyright (c) 2017 skydoves rights reserved.
 */

class BaseGsonConverter<T>(clazz: Class<T>) : PreferenceTypeConverter<T>(clazz) {

    private val gson: Gson = Gson()

    override fun convertObject(obj: T?): String {
        return gson.toJson(obj)
    }

    override fun convertType(string: String?): T? {
        when(string == null) {
            true -> return null
            else -> return gson.fromJson(string, clazz)
        }
    }
}