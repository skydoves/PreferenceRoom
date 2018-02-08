package com.skydoves.preferenceroomdemo.components;

import com.skydoves.preferenceroom.PreferenceComponent;
import com.skydoves.preferenceroomdemo.entities.TestProfile;

/**
 * Developed by skydoves on 2017-11-20.
 * Copyright (c) 2017 skydoves rights reserved.
 */

/**
 * Component integrates entities.
 */
@PreferenceComponent(entities = {TestProfile.class})
public interface JunitComponent {
    /**
     * declare dependency injection targets.
     */
    void inject(JunitComponentTest __);
}
