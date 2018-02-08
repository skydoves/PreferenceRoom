package com.skydoves.preferenceroomdemo;

import android.app.Application;
import android.content.Context;
import android.support.test.runner.AndroidJUnitRunner;

/**
 * Created by battleent on 2018. 2. 8..
 * Copyright (c) 2018 battleent All rights reserved.
 */

public class JunitTestRunner extends AndroidJUnitRunner {
    @Override
    public Application newApplication(ClassLoader cl, String className, Context context) throws InstantiationException, IllegalAccessException, ClassNotFoundException {
        return super.newApplication(cl, PreferenceRoomApplication.class.getName(), context);
    }
}
