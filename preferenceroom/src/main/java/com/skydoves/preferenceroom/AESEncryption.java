package com.skydoves.preferenceroom;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import androidx.annotation.Nullable;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

@SuppressWarnings({"GetInstance", "SpellCheckingInspection", "unused"})
public class AESEncryption {
    public static @Nullable String encrypt(String input, String key) {
      byte[] crypted = null;
      try {
        SecretKeySpec secretKeySpec = new SecretKeySpec(key.getBytes(), "AES");
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);
        crypted = cipher.doFinal(input.substring(0, 16).getBytes());
      } catch(Exception e) {
        e.printStackTrace();
      }
      BASE64Encoder encoder = new BASE64Encoder();
      if (crypted == null) return null;
      return encoder.encode(crypted);
    }

    public static @Nullable String decrypt(String input, String key) {
      byte[] output = null;
      try {
        BASE64Decoder decoder = new BASE64Decoder();
        SecretKeySpec secretKeySpec = new SecretKeySpec(key.getBytes(), "AES");
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, secretKeySpec);
        output = cipher.doFinal(decoder.decodeBuffer(input));
      } catch(Exception e) {
        e.printStackTrace();
      }
      if (output == null) return null;
      return new String(output);
    }
}
