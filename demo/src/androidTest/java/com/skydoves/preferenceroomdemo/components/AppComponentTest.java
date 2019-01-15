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

package com.skydoves.preferenceroomdemo.components;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

/** Developed by skydoves on 2017-11-29. Copyright (c) 2017 skydoves rights reserved. */
@RunWith(JUnit4.class)
public class AppComponentTest {

  private PreferenceComponent_AppComponent appComponent;

  @Before
  public void getComponentInstance() throws Exception {
    appComponent = PreferenceComponent_AppComponent.getInstance();
  }

  @Test
  public void componentInitializeTest() throws Exception {
    Assert.assertNotNull(PreferenceComponent_AppComponent.getInstance());
  }

  @Test
  public void entityInitializeTest() throws Exception {
    Assert.assertNotNull(appComponent.UserProfile());
    Assert.assertNotNull(appComponent.UserDevice());
  }

  @Test
  public void entityListTest() throws Exception {
    assertThat(appComponent.getEntityNameList().get(0).toString(), is("UserProfile"));
    assertThat(appComponent.getEntityNameList().get(1).toString(), is("UserDevice"));
  }
}
