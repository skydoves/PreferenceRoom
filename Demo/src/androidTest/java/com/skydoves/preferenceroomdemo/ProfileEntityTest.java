package com.skydoves.preferenceroomdemo;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.skydoves.preferenceroomdemo.entities.Preference_Profile;
import com.skydoves.preferenceroomdemo.models.PrivateInfo;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

/**
 * Developed by skydoves on 2017-11-29.
 * Copyright (c) 2017 skydoves rights reserved.
 */

@RunWith(AndroidJUnit4.class)
public class ProfileEntityTest {

    private Preference_Profile profile;

    @Before
    public void getProfileEntityInstance() {
        Context appContext = InstrumentationRegistry.getTargetContext();
        profile = Preference_Profile.getInstance(appContext);
    }

    @Test
    public void defaultTest() throws Exception {
        assertThat(profile.getNickname(), is("skydoves"));
        assertThat(profile.getLogin(), is(false));
        assertThat(profile.getVisits(), is(1));
        assertThat(profile.getUserinfo().getName(), is("null"));
        Assert.assertNull(profile.getUserPet());
    }

    @Test
    public void putPreferenceTest() throws Exception {
        profile.putNickname("PreferenceRoom");
        profile.putLogin(true);
        profile.putVisits(12);
        profile.putUserinfo(new PrivateInfo("Test", 20));

        assertThat(profile.getNickname(), is("PreferenceRoom"));
        assertThat(profile.getLogin(), is(true));
        assertThat(profile.getVisits(), is(12));
        assertThat(profile.getUserinfo().getName(), is("Test"));
        assertThat(profile.getUserinfo().getAge(), is(20));

        profile.clear();
    }

    @Test
    public void keyNameListTest() throws Exception {
        Assert.assertEquals(profile.getkeyNameList().size(), 5);
    }
}