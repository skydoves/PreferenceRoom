package com.skydoves.preferenceroomdemo.models;

import com.skydoves.preferenceroom.InjectPreference;
import com.skydoves.preferenceroomdemo.components.PreferenceComponent_PrefsComponent;
import com.skydoves.preferenceroomdemo.entities.Preference_UserProfile;

/**
 * Developed by skydoves on 2017-11-26.
 * Copyright (c) 2017 skydoves rights reserved.
 */

public class TestClass {

    @InjectPreference
    public Preference_UserProfile userProfile;

    public void init() {
        PreferenceComponent_PrefsComponent.getInstance().inject(this);
    }

    public String getUserName() {
        return userProfile.getUserName();
    }
}
