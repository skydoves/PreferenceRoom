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

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.skydoves.preferenceroom.InjectPreference
import com.skydoves.preferenceroomdemo.components.PreferenceComponent_UserProfileComponent
import com.skydoves.preferenceroomdemo.entities.Preference_UserProfile
import com.skydoves.preferenceroomdemo.models.PrivateInfo
import kotlinx.android.synthetic.main.activity_login.*
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast

/**
 * Developed by skydoves on 2017-11-26.
 * Copyright (c) 2017 skydoves rights reserved.
 */

class LoginActivity : AppCompatActivity() {

    /**
     * UserProfile entity.
     * [com.skydoves.preferenceroomdemo.entities.Profile]
     */
    @InjectPreference lateinit var userProfile: Preference_UserProfile

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        PreferenceComponent_UserProfileComponent.getInstance().inject(this)

        login_button.setOnClickListener {
            val inputNick = login_editText_nick.text.toString()
            val inputAge = login_editText_age.text.toString()
            when(inputNick.isNotEmpty() && inputAge.isNotEmpty()) {
                true -> {
                    userProfile.putLogin(true)
                    userProfile.putNickname(inputNick)
                    userProfile.putUserinfo(PrivateInfo(inputNick, Integer.parseInt(inputAge)))

                    startActivity<MainActivity>()
                    finish()
                }
                false -> toast("please fill all inputs")
            }
        }
    }
}
