package com.skydoves.preferenceroomdemo;

import android.app.Application;

import com.skydoves.preferenceroomdemo.components.PreferenceComponent_AppComponent;
import com.skydoves.preferenceroomdemo.components.PreferenceComponent_JunitComponent;

/**
 * Created by battleent on 2018. 2. 8..
 * Copyright (c) 2018 battleent All rights reserved.
 */

/**
 * We use a separate App for tests to prevent initializing dependency injection.
 *
 * See {@link JunitTestRunner}.
 */
public class PreferenceRoomApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        PreferenceComponent_AppComponent.init(this);
        PreferenceComponent_JunitComponent.init(this);
    }
}
