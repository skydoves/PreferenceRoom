package com.skydoves.preferenceroom;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import androidx.annotation.Nullable;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

@SuppressWarnings({"GetInstance", "SpellCheckingInspection", "unused"})
public class AESEncryption {
    public static @Nullable String encrypt(String input, String key) {
      if (input == null || key == null) return null;
      byte[] crypted;
      try {
        SecretKeySpec secretKeySpec = new SecretKeySpec(key.getBytes(), "AES");
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);
        crypted = cipher.doFinal(input.getBytes());
      } catch(Exception e) {
        return input;
      }
      BASE64Encoder encoder = new BASE64Encoder();
      if (crypted == null) return null;
      return encoder.encode(crypted);
    }

    public static @Nullable String decrypt(String input, String defaultValue, String key) {
      if (input == null || key == null) return defaultValue;
      byte[] output;
      try {
        BASE64Decoder decoder = new BASE64Decoder();
        SecretKeySpec secretKeySpec = new SecretKeySpec(key.getBytes(), "AES");
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, secretKeySpec);
        output = cipher.doFinal(decoder.decodeBuffer(input));
      } catch(Exception e) {
        return defaultValue;
      }
      if (output == null || new String(output).equals("")
          || new String(output).equals("null")) return defaultValue;
      return new String(output);
    }
}
