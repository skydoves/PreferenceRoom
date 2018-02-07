package com.skydoves.preferenceroomdemo.converters

import com.google.gson.Gson
import com.skydoves.preferenceroom.PreferenceTypeConverter
import com.skydoves.preferenceroomdemo.models.Pet

/**
 * Developed by skydoves on 2017-11-26.
 * Copyright (c) 2017 skydoves rights reserved.
 */

class PetConverter(clazz: Class<Pet>) : PreferenceTypeConverter<Pet>(clazz) {

    private val gson: Gson

    init {
        this.gson = Gson()
    }

    override fun convertObject(pet: Pet): String {
        return gson.toJson(pet)
    }

    override fun convertType(string: String): Pet {
        return gson.fromJson(string, Pet::class.java)
    }
}
