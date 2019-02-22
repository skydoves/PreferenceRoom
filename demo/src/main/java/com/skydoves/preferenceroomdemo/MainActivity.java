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

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.ViewCompat;
import com.skydoves.preferenceroom.InjectPreference;
import com.skydoves.preferenceroomdemo.components.AppComponent;
import com.skydoves.preferenceroomdemo.components.PreferenceComponent_AppComponent;
import com.skydoves.preferenceroomdemo.models.ItemProfile;
import com.skydoves.preferenceroomdemo.utils.ListViewAdapter;

public class MainActivity extends AppCompatActivity {

  /** UserProfile Component. {@link AppComponent} */
  @InjectPreference public PreferenceComponent_AppComponent component;

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    PreferenceComponent_AppComponent.getInstance()
        .inject(this); // inject dependency injection to MainActivity.

    Toolbar toolbar = findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);

    initializeUI();
    setProfileButton();
  }

  private void initializeUI() {
    ListViewAdapter adapter = new ListViewAdapter(this, R.layout.item_profile);

    if (component.UserProfile().getLogin()) {
      ListView listView = findViewById(R.id.content_listView);
      ViewCompat.setNestedScrollingEnabled(listView, true);
      listView.setAdapter(adapter);

      adapter.addItem(new ItemProfile("message", component.UserProfile().getNickname()));
      adapter.addItem(
          new ItemProfile("nick value", component.UserProfile().getUserinfo().getName()));
      adapter.addItem(new ItemProfile("age", component.UserProfile().getUserinfo().getAge() + ""));
      adapter.addItem(new ItemProfile("visits", component.UserProfile().getVisits() + ""));

      /**
       * increment visits count. show {@link com.skydoves.preferenceroomdemo.entities.Profile}
       * putVisitCountFunction()
       */
      component.UserProfile().putVisits(component.UserProfile().getVisits());
    }

    if (component.UserDevice().getUuid() == null) {
      putDumpDeviceInfo();
    } else {
      adapter.addItem(new ItemProfile("version", component.UserDevice().getVersion()));
      adapter.addItem(new ItemProfile("uuid", component.UserDevice().getUuid()));
    }
  }

  private void setProfileButton() {
    Button needLoginView = findViewById(R.id.content_button);
    needLoginView.setOnClickListener(
        new View.OnClickListener() {
          @Override
          public void onClick(View view) {
            startActivity(new Intent(getBaseContext(), LoginActivity.class));
            finish();
          }
        });
  }

  private void putDumpDeviceInfo() {
    component.UserDevice().putVersion("1.0.0.0");
    component.UserDevice().putUuid("00001234-0000-0000-0000-000123456789");
  }
}
