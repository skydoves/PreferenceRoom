package com.skydoves.preferenceroomdemo;

import com.skydoves.preferenceroom.PreferenceComponent;

/**
 * Developed by skydoves on 2017-11-20.
 * Copyright (c) 2017 skydoves rights reserved.
 */

@PreferenceComponent(entities = {Profile.class, Test.class})
public interface PrefsComponent {
    void inject(MainActivity __);
    void inject(Profile __);
}
