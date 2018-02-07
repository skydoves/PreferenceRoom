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
