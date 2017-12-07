package com.skydoves.preferenceroomdemo.models;

/**
 * Developed by skydoves on 2017-11-26.
 * Copyright (c) 2017 skydoves rights reserved.
 */

public class ItemProfile {
    private String title;
    private String content;

    public ItemProfile(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }
}
