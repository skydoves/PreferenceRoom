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
import com.facebook.stetho.Stetho;
import com.skydoves.preferenceroomdemo.components.PreferenceComponent_AppComponent;

public class MyApplication extends Application {

  @Override
  public void onCreate() {
    super.onCreate();
    // initialize Stetho for debugging local data
    Stetho.initializeWithDefaults(this);

    /** initialize instances of preference component and entities. {@link AppComponent} */
    PreferenceComponent_AppComponent.init(this);
  }
}
