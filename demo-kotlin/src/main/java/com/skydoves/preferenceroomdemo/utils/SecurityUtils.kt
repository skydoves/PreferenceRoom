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

import android.util.Base64

import javax.crypto.Cipher
import javax.crypto.spec.SecretKeySpec

/**
 * Developed by skydoves on 2018-02-07.
 * Copyright (c) 2017 skydoves rights reserved.
 */

object SecurityUtils {
    private val key = "abc12345Bab12345"

    fun encrypt(input: String?): String? {
        if (input == null) return null
        var encrypted: ByteArray? = null
        try {
            val skey = SecretKeySpec(key.toByteArray(), "AES")
            val cipher = Cipher.getInstance("AES/ECB/PKCS5Padding")
            cipher.init(Cipher.ENCRYPT_MODE, skey)
            encrypted = cipher.doFinal(input.toByteArray())
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return Base64.encodeToString(encrypted, Base64.DEFAULT)
    }

    fun decrypt(input: String?): String? {
        if (input == null) return null
        var decrypted: ByteArray? = null
        try {
            val skey = SecretKeySpec(key.toByteArray(), "AES")
            val cipher = Cipher.getInstance("AES/ECB/PKCS5Padding")
            cipher.init(Cipher.DECRYPT_MODE, skey)
            decrypted = cipher.doFinal(Base64.decode(input, Base64.DEFAULT))
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return String(decrypted!!)
    }
}
