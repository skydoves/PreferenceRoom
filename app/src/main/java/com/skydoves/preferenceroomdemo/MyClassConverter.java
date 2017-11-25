package com.skydoves.preferenceroomdemo;

import com.skydoves.preferenceroom.PreferenceTypeConverter;

/**
 * Developed by skydoves on 2017-11-25.
 * Copyright (c) 2017 skydoves rights reserved.
 */

public class MyClassConverter extends PreferenceTypeConverter<MyClass> {

    @Override
    public String convertObject(MyClass object) {
        return object.getName() + ":" + object.getAge();
    }

    @Override
    public MyClass convertType(String string) {
        String[] information = string.split(":");
        MyClass myClass = new MyClass(information[0], Integer.parseInt(information[1]));
        return myClass;
    }
}
