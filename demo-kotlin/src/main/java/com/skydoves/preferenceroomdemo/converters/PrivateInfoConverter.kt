package com.skydoves.preferenceroomdemo.converters

import com.skydoves.preferenceroom.PreferenceTypeConverter
import com.skydoves.preferenceroomdemo.models.PrivateInfo

/**
 * Developed by skydoves on 2017-11-25.
 * Copyright (c) 2017 skydoves rights reserved.
 */

class PrivateInfoConverter(clazz: Class<PrivateInfo>) : PreferenceTypeConverter<PrivateInfo>(clazz) {

    override fun convertObject(privateInfo: PrivateInfo): String {
        return privateInfo.name + "," + privateInfo.age
    }

    override fun convertType(string: String?): PrivateInfo {
        if (string == null) return PrivateInfo("null", 0)
        val information = string.split(",".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        return PrivateInfo(information[0], Integer.parseInt(information[1]))
    }
}
