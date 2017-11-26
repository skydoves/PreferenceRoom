package com.skydoves.preferenceroomdemo.converters;

import com.google.gson.Gson;
import com.skydoves.preferenceroom.PreferenceTypeConverter;
import com.skydoves.preferenceroomdemo.models.Pet;

/**
 * Developed by skydoves on 2017-11-26.
 * Copyright (c) 2017 skydoves rights reserved.
 */

public class PetConverter extends PreferenceTypeConverter<Pet> {

    private final Gson gson;

    /**
     * default constructor will be called by PreferenceRoom
     */
    public PetConverter() {
        this.gson = new Gson();
    }

    @Override
    public String convertObject(Pet pet) {
        return gson.toJson(pet);
    }

    @Override
    public Pet convertType(String string) {
        return gson.fromJson(string, Pet.class);
    }
}
