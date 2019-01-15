/*
 * Copyright (C) 2017 skydoves
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
