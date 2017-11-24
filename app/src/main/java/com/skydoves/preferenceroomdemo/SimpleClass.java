package com.skydoves.preferenceroomdemo;

import com.skydoves.preferenceroom.InjectPreference;
import com.skydoves.preferenceroom.PreferenceRoom;

/**
 * Developed by skydoves on 2017-11-24.
 * Copyright (c) 2017 skydoves rights reserved.
 */

public class SimpleClass {

    @InjectPreference
    public Preference_UserProfile userProfile;

    public void inject() {
        PreferenceRoom.inject(this);
    }

    public int getUserName() {
        return userProfile.getVisit();
    }
}
