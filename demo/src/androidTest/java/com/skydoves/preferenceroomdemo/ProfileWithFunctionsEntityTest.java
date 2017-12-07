package com.skydoves.preferenceroomdemo;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.skydoves.preferenceroomdemo.entities.Preference_ProfileWithFunctions;
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
public class ProfileWithFunctionsEntityTest {

    private Preference_ProfileWithFunctions profile;
    private SharedPreferences preferences;

    @Before
    public void getProfileEntityInstance() {
        Context appContext = InstrumentationRegistry.getTargetContext();
        profile = Preference_ProfileWithFunctions.getInstance(appContext);
        preferences = appContext.getSharedPreferences(profile.getEntityName(), Context.MODE_PRIVATE);
    }

    @Test
    public void preferenceFunctionTest() throws Exception {
        profile.putNickname("PreferenceRoom");
        profile.putVisits(15);

        assertThat(preferences.getString(profile.nicknameKeyName(), null), is("Hello, PreferenceRoom"));
        assertThat(profile.getNickname(), is(preferences.getString(profile.nicknameKeyName(), null) + "!!!"));
        assertThat(preferences.getInt(profile.visitsKeyName(), -1), is(15 + 1));

        profile.clear();
    }

    @Test
    public void defaultTest() throws Exception {
        assertThat(profile.getNickname(), is("skydoves!!!"));
        assertThat(profile.getLogin(), is(false));
        assertThat(profile.getVisits(), is(1));
        assertThat(profile.getUserinfo().getName(), is("null"));
        Assert.assertNull(profile.getUserPet());
    }

    @Test
    public void putPreferenceTest() throws Exception {
        profile.putNickname("PreferenceRoom");
        profile.putLogin(true);
        profile.putVisits(15);
        profile.putUserinfo(new PrivateInfo("Test", 30));

        assertThat(profile.getNickname(), is("Hello, PreferenceRoom!!!"));
        assertThat(profile.getLogin(), is(true));
        assertThat(profile.getVisits(), is(15 + 1));
        assertThat(profile.getUserinfo().getName(), is("Test"));
        assertThat(profile.getUserinfo().getAge(), is(30));

        profile.clear();
    }
}
