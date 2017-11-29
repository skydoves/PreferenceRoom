package com.skydoves.preferenceroomdemo;

import android.content.Context;
import android.support.test.InstrumentationRegistry;

import com.skydoves.preferenceroomdemo.components.PreferenceComponent_TestProfileComponent;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

/**
 * Developed by skydoves on 2017-11-29.
 * Copyright (c) 2017 skydoves rights reserved.
 */

@RunWith(JUnit4.class)
public class ComponentTest {

    private PreferenceComponent_TestProfileComponent testProfileComponent;

    @Before
    public void getComponentInstance() throws Exception {
        Context appContext = InstrumentationRegistry.getTargetContext();
        testProfileComponent = PreferenceComponent_TestProfileComponent.init(appContext);
    }

    @Test
    public void componentInitializeTest() throws Exception  {
        Assert.assertNotNull(PreferenceComponent_TestProfileComponent.getInstance());
    }

    @Test
    public void entityInitializeTest() throws Exception  {
        Assert.assertNotNull(testProfileComponent.Profile());
        Assert.assertNotNull(testProfileComponent.ProfileWithFunctions());
        Assert.assertNotNull(testProfileComponent.Device());
    }
}
