package com.skydoves.preferenceroomdemo.entities;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.google.gson.Gson;
import com.skydoves.preferenceroomdemo.models.Pet;
import com.skydoves.preferenceroomdemo.models.PrivateInfo;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

/**
 * Developed by skydoves on 2017-11-29.
 * Copyright (c) 2017 skydoves rights reserved.
 */

@RunWith(AndroidJUnit4.class)
public class ProfileEntityTest {

    private Preference_UserProfile profile;
    private SharedPreferences preferences;

    @Before
    public void getProfileEntityInstance() {
        Context appContext = InstrumentationRegistry.getTargetContext();
        profile = Preference_UserProfile.getInstance(appContext);
        preferences = appContext.getSharedPreferences(profile.getEntityName(), Context.MODE_PRIVATE);
    }

    @Test
    public void preferenceTest() {
        profile.clear();
        preferences.edit().putString(profile.nicknameKeyName(), "PreferenceRoom").apply();
        preferences.edit().putBoolean(profile.LoginKeyName(), true).apply();
        preferences.edit().putInt(profile.visitsKeyName(), 12).apply();

        assertThat(preferences.getString(profile.nicknameKeyName(), null) + "!!!", is(profile.getNickname()));
        assertThat(preferences.getBoolean(profile.LoginKeyName(), false), is(profile.getLogin()));
        assertThat(preferences.getInt(profile.visitsKeyName(), -1), is(profile.getVisits()));
    }

    @Test
    public void defaultTest() throws Exception {
        profile.clear();
        assertThat(profile.getNickname(), is("skydoves!!!"));
        assertThat(profile.getLogin(), is(false));
        assertThat(profile.getVisits(), is(1));
        assertThat(profile.getUserinfo().getName(), is("null"));
        Assert.assertNull(profile.getUserPet());
    }

    @Test
    public void putPreferenceTest() throws Exception {
        profile.clear();
        profile.putNickname("PreferenceRoom");
        profile.putLogin(true);
        profile.putVisits(12);
        profile.putUserinfo(new PrivateInfo("Jaewoong", 123));

        assertThat(profile.getNickname(), is("Hello, PreferenceRoom!!!"));
        assertThat(profile.getLogin(), is(true));
        assertThat(profile.getVisits(), is(13));
        assertThat(profile.getUserinfo().getName(), is("Jaewoong"));
        assertThat(profile.getUserinfo().getAge(), is(123));
    }

    @Test
    public void baseGsonConverterTest() throws Exception {
        Gson gson = new Gson();
        Pet pet = new Pet("skydoves", 11, true, Color.WHITE);

        profile.putUserPet(pet);
        assertThat(preferences.getString(profile.userPetKeyName(), null), notNullValue());

        Pet petFromPreference = gson.fromJson(preferences.getString(profile.userPetKeyName(), null), Pet.class);
        assertThat(petFromPreference.getName(), is("skydoves"));
        assertThat(petFromPreference.getAge(), is(11));
        assertThat(petFromPreference.isFeed(), is(true));
        assertThat(petFromPreference.getColor(), is(Color.WHITE));

        assertThat(profile.getUserPet().getName(), is("skydoves"));
        assertThat(profile.getUserPet().getAge(), is(11));
        assertThat(profile.getUserPet().isFeed(), is(true));
        assertThat(profile.getUserPet().getColor(), is(Color.WHITE));
    }

    @Test
    public void keyNameListTest() throws Exception {
        Assert.assertEquals(profile.getkeyNameList().size(), 5);
    }
}