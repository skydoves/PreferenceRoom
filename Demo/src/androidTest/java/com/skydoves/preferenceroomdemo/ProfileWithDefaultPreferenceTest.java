package com.skydoves.preferenceroomdemo;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.skydoves.preferenceroomdemo.entities.Preference_ProfileWithDefault;
import com.skydoves.preferenceroomdemo.models.PrivateInfo;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

/**
 * Created by skydoves on 2017. 12. 4.
 * Copyright (c) 2017 battleent rights reserved.
 */

@RunWith(AndroidJUnit4.class)
public class ProfileWithDefaultPreferenceTest {

    private Preference_ProfileWithDefault profileWithDefault;
    private SharedPreferences preference;

    @Before
    public void getProfileEntityInstance() {
        Context appContext = InstrumentationRegistry.getTargetContext();
        profileWithDefault = Preference_ProfileWithDefault.getInstance(appContext);
        preference = PreferenceManager.getDefaultSharedPreferences(appContext);
    }

    @Test
    public void defaultPreferenceTest() {
        String testNickName = "testNickName";
        profileWithDefault.putNickname(testNickName);
        assertThat(preference.getString("nickname", null), is(testNickName));

        profileWithDefault.putVisits(13);
        assertThat(profileWithDefault.getVisits(), is(preference.getInt("visits", 0)));

        preference.edit().putBoolean("Login", true).apply();
        assertThat(profileWithDefault.getLogin(), is(true));

        profileWithDefault.clear();
    }

    @Test
    public void putPreferenceTest() throws Exception {
        profileWithDefault.putNickname("PreferenceRoom");
        profileWithDefault.putLogin(true);
        profileWithDefault.putVisits(12);
        profileWithDefault.putUserinfo(new PrivateInfo("Test", 20));

        assertThat(profileWithDefault.getNickname(), is("PreferenceRoom"));
        assertThat(profileWithDefault.getLogin(), is(true));
        assertThat(profileWithDefault.getVisits(), is(12));
        assertThat(profileWithDefault.getUserinfo().getName(), is("Test"));
        assertThat(profileWithDefault.getUserinfo().getAge(), is(20));

        profileWithDefault.clear();
    }

}
