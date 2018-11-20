package com.skydoves.preferenceroomdemo.entities;

import android.content.Context;
import android.content.SharedPreferences;
import androidx.test.InstrumentationRegistry;

import com.skydoves.preferenceroomdemo.utils.SecurityUtils;

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
public class DeviceEntityTest {

    private SharedPreferences preferences;
    private Preference_UserDevice device;

    private static final String version = "1.0.0.0";
    private static final String uuid = "00001234-0000-0000-0000-000123456789";

    @Before
    public void getProfileEntityInstance() throws Exception {
        Context appContext = InstrumentationRegistry.getTargetContext();
        device = Preference_UserDevice.getInstance(appContext);
        preferences = appContext.getSharedPreferences(device.getEntityName(), Context.MODE_PRIVATE);
    }

    @Test
    public void versionTest() throws Exception {
        preferences.edit().putString(device.versionKeyName(), version).apply();
        assertThat(device.getVersion(), is(version));
    }

    @Test
    public void securityTest() throws Exception {
        device.putUuid(uuid); // encrypt
        assertThat(device.getUuid(), is(uuid)); // decrypt
        assertThat(preferences.getString("uuid", null), is(SecurityUtils.encrypt(uuid)));
    }
}
