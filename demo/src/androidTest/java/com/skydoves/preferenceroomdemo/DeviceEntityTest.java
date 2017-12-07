package com.skydoves.preferenceroomdemo;

import android.content.Context;
import android.support.test.InstrumentationRegistry;

import com.skydoves.preferenceroomdemo.entities.Preference_UserDevice;

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

    private Preference_UserDevice device;

    @Before
    public void getProfileEntityInstance() {
        Context appContext = InstrumentationRegistry.getTargetContext();
        device = Preference_UserDevice.getInstance(appContext);
    }

    @Test
    public void securityTest() {
        String uuid = "00001234-0000-0000-0000-000123456789";

        device.putUuid(uuid);
        assertThat(device.getUuid(), is(uuid));
    }
}
