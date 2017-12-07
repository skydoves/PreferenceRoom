package com.skydoves.preferenceroomdemo;

import android.content.Context;
import android.support.test.InstrumentationRegistry;

import com.skydoves.preferenceroom.InjectPreference;
import com.skydoves.preferenceroomdemo.components.PreferenceComponent_TestProfileComponent;
import com.skydoves.preferenceroomdemo.entities.Preference_Profile;
import com.skydoves.preferenceroomdemo.entities.Preference_ProfileWithFunctions;
import com.skydoves.preferenceroomdemo.models.PrivateInfo;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

/**
 * Developed by skydoves on 2017-11-29.
 * Copyright (c) 2017 skydoves rights reserved.
 */

@RunWith(JUnit4.class)
public class InjectPreferenceTest {

    @InjectPreference
    public Preference_Profile profile;

    @InjectPreference
    public Preference_ProfileWithFunctions profileWithFunctions;

    @InjectPreference
    public PreferenceComponent_TestProfileComponent testProfileComponent;

    @Before
    public void inject() {
        Context appContext = InstrumentationRegistry.getTargetContext();
        PreferenceComponent_TestProfileComponent.init(appContext);
        PreferenceComponent_TestProfileComponent.getInstance().inject(this);
    }

    @Test
    public void injectTest() throws Exception {
        Assert.assertNotNull(profile);
        Assert.assertNotNull(profileWithFunctions);
        Assert.assertNotNull(testProfileComponent);
    }

    @Test
    public void putProfileEntityTest() throws Exception {
        profile.putNickname("putProfileEntityTest");
        profile.putLogin(true);
        profile.putVisits(32);
        profile.putUserinfo(new PrivateInfo("Test", 20));

        assertThat(profile.getNickname(), is("putProfileEntityTest"));
        assertThat(profile.getLogin(), is(true));
        assertThat(profile.getVisits(), is(32));
        assertThat(profile.getUserinfo().getName(), is("Test"));
        assertThat(profile.getUserinfo().getAge(), is(20));

        profile.clear();
    }
}
