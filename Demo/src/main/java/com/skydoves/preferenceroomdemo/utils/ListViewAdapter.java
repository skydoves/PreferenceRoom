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

/**
 * Developed by skydoves on 2017-11-26.
 * Copyright (c) 2017 skydoves rights reserved.
 */

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
        if(view == null)
            view = this.inflater.inflate(layout, viewGroup, false);

            ItemProfile itemProfile = profileList.get(index);

            TextView title = view.findViewById(R.id.item_profile_title);
            title.setText(itemProfile.getTitle());

            TextView content = view.findViewById(R.id.item_profile_content);
            content.setText(itemProfile.getContent());
        return view;
    }
}
