package com.skydoves.preferenceroomdemo.components;

import com.skydoves.preferenceroom.InjectPreference;

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
public class JunitComponentTest {

    @InjectPreference
    public PreferenceComponent_JunitComponent junitComponent;

    /**
     * dependency injection {@link com.skydoves.preferenceroomdemo.components.JunitComponent}
     */
    @Before
    public void inject() throws Exception {
        PreferenceComponent_JunitComponent.getInstance().inject(this);
    }

    /**
     * injection test
     * @throws Exception NullPointerException
     */
    @Test
    public void injectionTest() throws Exception {
        Assert.assertNotNull(junitComponent);
        Assert.assertNotNull(junitComponent.TestProfile());
    }

    @Test
    public void entityListTest() throws Exception {
        assertThat(junitComponent.getEntityNameList().get(0).toString(), is("TestProfile"));
    }
}
