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

package com.skydoves.preferenceroomdemo

import android.content.Intent
import android.os.Bundle
import androidx.core.view.ViewCompat
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import android.widget.Button
import com.skydoves.preferenceroom.InjectPreference
import com.skydoves.preferenceroomdemo.components.PreferenceComponent_UserProfileComponent
import com.skydoves.preferenceroomdemo.models.ItemProfile
import com.skydoves.preferenceroomdemo.utils.ListViewAdapter
import kotlinx.android.synthetic.main.content_scrolling.*

/**
 * Developed by skydoves on 2017-11-26.
 * Copyright (c) 2017 skydoves rights reserved.
 */

class MainActivity : AppCompatActivity() {

    /**
     * UserProfile Component.
     * [com.skydoves.preferenceroomdemo.components.UserProfileComponent]
     */
    @InjectPreference lateinit var component: PreferenceComponent_UserProfileComponent

    private val adapter by lazy { ListViewAdapter(this, R.layout.item_profile) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        PreferenceComponent_UserProfileComponent.getInstance().inject(this) // inject dependency injection to MainActivity.

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        initializeUI()
        setProfileButton()
    }

    private fun initializeUI() {
        when(component.UserProfile().login) {
            true -> {
                ViewCompat.setNestedScrollingEnabled(content_listView, true)
                content_listView.adapter = adapter

                adapter.addItem(ItemProfile("message", component.UserProfile().nickname!!))
                adapter.addItem(ItemProfile("nick value", component.UserProfile().userinfo!!.name))
                adapter.addItem(ItemProfile("age", component.UserProfile().userinfo!!.age.toString() + ""))
                adapter.addItem(ItemProfile("visits", component.UserProfile().visits.toString() + ""))

                /**
                 * increment visits count.
                 * show [com.skydoves.preferenceroomdemo.entities.Profile] putVisitCountFunction()
                 */
                component.UserProfile().putVisits(component.UserProfile().visits)
            }
        }

        component.UserDevice().uuid?.let {
            adapter.addItem(ItemProfile("version", component.UserDevice().version!!))
            adapter.addItem(ItemProfile("uuid", component.UserDevice().uuid!!))
        } ?: putDumpDeviceInfo()
    }

    private fun setProfileButton() {
        val needLoginView = findViewById<Button>(R.id.content_button)
        needLoginView.setOnClickListener {
            startActivity(Intent(baseContext, LoginActivity::class.java))
            finish()
        }
    }

    private fun putDumpDeviceInfo() {
        component.UserDevice().putVersion("1.0.0.0")
        component.UserDevice().putUuid("00001234-0000-0000-0000-000123456789")
    }
}
