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

package com.skydoves.preferenceroomdemo.entities;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import android.content.Context;
import android.content.SharedPreferences;
import androidx.test.InstrumentationRegistry;
import com.skydoves.preferenceroomdemo.utils.SecurityUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

/** Developed by skydoves on 2017-11-29. Copyright (c) 2017 skydoves rights reserved. */
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
