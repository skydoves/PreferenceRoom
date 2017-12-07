package com.skydoves.preferenceroomdemo.utils;

import android.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

/**
 * Developed by skydoves on 2017-11-26.
 * Copyright (c) 2017 skydoves rights reserved.
 */

public class SecurityUtils {
    private static final String key = "abc12345Bab12345";

    public static String encrypt(String input) {
        if(input == null) return null;
        byte[] encrypted = null;
        try{
            SecretKeySpec skey = new SecretKeySpec(key.getBytes(), "AES");
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, skey);
            encrypted = cipher.doFinal(input.getBytes());
        } catch(Exception e){
            e.printStackTrace();
        }
        return Base64.encodeToString(encrypted, Base64.DEFAULT);
    }

    public static String decrypt(String input) {
        if(input == null) return null;
        byte[] decrypted = null;
        try{
            SecretKeySpec skey = new SecretKeySpec(key.getBytes(), "AES");
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, skey);
            decrypted = cipher.doFinal(Base64.decode(input, Base64.DEFAULT));
        } catch(Exception e){
            e.printStackTrace();
        }
        return new String(decrypted);
    }
}
