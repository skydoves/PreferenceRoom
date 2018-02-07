package com.skydoves.preferenceroomdemo;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.skydoves.preferenceroomdemo.entities.Preference_ProfileBaseConverter;
import com.skydoves.preferenceroomdemo.models.PrivateInfo;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

/**
 * Developed by skydoves on 2018-02-07.
 * Copyright (c) 2017 skydoves rights reserved.
 */

@RunWith(AndroidJUnit4.class)
public class ProfileEntityBaseConverterTest {

    private Preference_ProfileBaseConverter profile;
    private SharedPreferences preferences;

    @Before
    public void getProfileEntityInstance() {
        Context appContext = InstrumentationRegistry.getTargetContext();
        profile = Preference_ProfileBaseConverter.getInstance(appContext);
        preferences = appContext.getSharedPreferences(profile.getEntityName(), Context.MODE_PRIVATE);
    }

    @Test
    public void preferenceTest() {
        preferences.edit().putString(profile.nicknameKeyName(), "PreferenceRoom").apply();
        preferences.edit().putBoolean(profile.LoginKeyName(), true).apply();
        preferences.edit().putInt(profile.visitsKeyName(), 12).apply();

        assertThat(preferences.getString(profile.nicknameKeyName(), null), is(profile.getNickname()));
        assertThat(preferences.getBoolean(profile.LoginKeyName(), false), is(profile.getLogin()));
        assertThat(preferences.getInt(profile.visitsKeyName(), -1), is(profile.getVisits()));
        profile.clear();
    }

    @Test
    public void defaultTest() throws Exception {
        profile.clear();
        assertThat(profile.getNickname(), is("skydoves"));
        assertThat(profile.getLogin(), is(false));
        assertThat(profile.getVisits(), is(1));
        Assert.assertNull(profile.getUserinfo());
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
        Assert.assertEquals(profile.getkeyNameList().size(), 6);
    }
}