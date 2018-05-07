package com.skydoves.preferenceroomdemo.components;

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
public class AppComponentTest {

    private PreferenceComponent_AppComponent appComponent;

    @Before
    public void getComponentInstance() throws Exception {
        appComponent = PreferenceComponent_AppComponent.getInstance();
    }

    @Test
    public void componentInitializeTest() throws Exception  {
        Assert.assertNotNull(PreferenceComponent_AppComponent.getInstance());
    }

    @Test
    public void entityInitializeTest() throws Exception  {
        Assert.assertNotNull(appComponent.UserProfile());
        Assert.assertNotNull(appComponent.UserDevice());
    }

    @Test
    public void entityListTest() throws Exception {
        assertThat(appComponent.getEntityNameList().get(0).toString(), is("UserProfile"));
        assertThat(appComponent.getEntityNameList().get(1).toString(), is("UserDevice"));
    }
}
