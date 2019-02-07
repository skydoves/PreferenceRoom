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

package com.skydoves.preferenceroomdemo.utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.skydoves.preferenceroomdemo.R;
import com.skydoves.preferenceroomdemo.models.ItemProfile;
import java.util.ArrayList;
import java.util.List;

public class ListViewAdapter extends BaseAdapter {

  private int layout;
  private LayoutInflater inflater;
  private List<ItemProfile> profileList;

  public ListViewAdapter(Context context, int layout) {
    this.profileList = new ArrayList<>();
    this.layout = layout;
    this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
  }

  @Override
  public int getCount() {
    return profileList.size();
  }

  @Override
  public Object getItem(int i) {
    return profileList.get(i);
  }

  @Override
  public long getItemId(int i) {
    return i;
  }

  public void addItem(ItemProfile itemProfile) {
    this.profileList.add(itemProfile);
    notifyDataSetChanged();
  }

  @Override
  public View getView(int index, View view, ViewGroup viewGroup) {
    if (view == null) view = this.inflater.inflate(layout, viewGroup, false);

    ItemProfile itemProfile = profileList.get(index);

    TextView title = view.findViewById(R.id.item_profile_title);
    title.setText(itemProfile.getTitle());

    TextView content = view.findViewById(R.id.item_profile_content);
    content.setText(itemProfile.getContent());
    return view;
  }
}
