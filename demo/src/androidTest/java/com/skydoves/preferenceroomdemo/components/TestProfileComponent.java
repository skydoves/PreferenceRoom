package com.skydoves.preferenceroomdemo.components;

import com.skydoves.preferenceroom.PreferenceComponent;
import com.skydoves.preferenceroomdemo.InjectPreferenceTest;
import com.skydoves.preferenceroomdemo.entities.UserDevice;
import com.skydoves.preferenceroomdemo.entities.UserProfile;
import com.skydoves.preferenceroomdemo.entities.UserProfileWithFunctions;

/**
 * Developed by skydoves on 2017-11-29.
 * Copyright (c) 2017 skydoves rights reserved.
 */

/**
 * Component integrates entities.
 */
@PreferenceComponent(entities = {UserProfile.class, UserProfileWithFunctions.class, UserDevice.class})
public interface TestProfileComponent {
    /**
     * declare dependency injection targets.
     */
    void inject(InjectPreferenceTest __);
}
