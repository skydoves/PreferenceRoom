package com.skydoves.preferenceroomdemo.models;

/**
 * Developed by skydoves on 2017-11-25.
 * Copyright (c) 2017 skydoves rights reserved.
 */

public class PrivateInfo {

    private String name;
    private int age;

    public PrivateInfo(String name, int age) {
        this.name = name;
        this.age = age;
    }

    public int getAge() {
        return this.age;
    }

    public String getName() {
        return this.name;
    }
}
