/*
 * Copyright (C) 2017 skydoves
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.skydoves.preferenceroomdemo;

import android.app.Application;
import com.skydoves.preferenceroomdemo.components.PreferenceComponent_AppComponent;
import com.skydoves.preferenceroomdemo.components.PreferenceComponent_JunitComponent;

/** Created by battleent on 2018. 2. 8.. Copyright (c) 2018 battleent All rights reserved. */

/**
 * We use a separate App for tests to prevent initializing dependency injection.
 *
 * <p>See {@link JunitTestRunner}.
 */
public class PreferenceRoomApplication extends Application {

  @Override
  public void onCreate() {
    super.onCreate();
    PreferenceComponent_AppComponent.init(this);
    PreferenceComponent_JunitComponent.init(this);
  }
}
