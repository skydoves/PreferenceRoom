package com.skydoves.preferenceroomdemo.components;

import com.skydoves.preferenceroom.PreferenceComponent;
import com.skydoves.preferenceroomdemo.MainActivity;
import com.skydoves.preferenceroomdemo.converters.Test;
import com.skydoves.preferenceroomdemo.entities.Profile;
import com.skydoves.preferenceroomdemo.models.TestClass;

/**
 * Developed by skydoves on 2017-11-20.
 * Copyright (c) 2017 skydoves rights reserved.
 */

@PreferenceComponent(entities = {Profile.class, Test.class})
public interface PrefsComponent {
    void inject(MainActivity inject);
    void inject(Profile __);
    void inject(TestClass __);
}
