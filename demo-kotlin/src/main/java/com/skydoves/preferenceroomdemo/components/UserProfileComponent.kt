package com.skydoves.preferenceroomdemo.components

import com.skydoves.preferenceroom.PreferenceComponent
import com.skydoves.preferenceroomdemo.LoginActivity
import com.skydoves.preferenceroomdemo.MainActivity
import com.skydoves.preferenceroomdemo.entities.Device
import com.skydoves.preferenceroomdemo.entities.Profile

/**
 * Developed by skydoves on 2017-11-20.
 * Copyright (c) 2017 skydoves rights reserved.
 */

/**
 * Component integrates entities.
 */
@PreferenceComponent(entities = [Profile::class, Device::class])
interface UserProfileComponent {
    /**
     * declare dependency injection targets.
     */
    fun inject(target: MainActivity)
    fun inject(target: LoginActivity)
}
