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

package com.skydoves.preferenceroomdemo.utils

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.skydoves.preferenceroomdemo.R
import com.skydoves.preferenceroomdemo.models.ItemProfile
import java.util.ArrayList

/**
 * Developed by skydoves on 2018-02-07.
 * Copyright (c) 2017 skydoves rights reserved.
 */

class ListViewAdapter(context: Context, private val layout: Int) : BaseAdapter() {
  private val inflater: LayoutInflater
  private val profileList: MutableList<ItemProfile>

  init {
    this.profileList = ArrayList()
    this.inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
  }

  override fun getCount() = profileList.size

  override fun getItem(i: Int) = profileList[i]

  override fun getItemId(i: Int) = i.toLong()

  fun addItem(itemProfile: ItemProfile) {
    this.profileList.add(itemProfile)
    notifyDataSetChanged()
  }

  override fun getView(index: Int, view: View?, viewGroup: ViewGroup): View {
    var itemView = view
    if (itemView == null) {
      itemView = this.inflater.inflate(layout, viewGroup, false)
    }

    val itemProfile = profileList[index]

    val title = itemView!!.findViewById<TextView>(R.id.item_profile_title)
    title.text = itemProfile.title

    val content = itemView.findViewById<TextView>(R.id.item_profile_content)
    content.text = itemProfile.content
    return itemView
  }
}
