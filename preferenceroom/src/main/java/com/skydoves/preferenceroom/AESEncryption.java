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

package com.skydoves.preferenceroom;

import androidx.annotation.Nullable;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

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
    } catch (Exception e) {
      return input;
    }
    if (crypted == null) return null;
    return Base64.encodeToString(crypted, Base64.DEFAULT);
  }

  public static @Nullable String decrypt(String input, String defaultValue, String key) {
    if (input == null || key == null) return defaultValue;
    byte[] output;
    try {
      SecretKeySpec secretKeySpec = new SecretKeySpec(key.getBytes(), "AES");
      Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
      cipher.init(Cipher.DECRYPT_MODE, secretKeySpec);
      output = cipher.doFinal(Base64.decode(input, Base64.DEFAULT));
    } catch (Exception e) {
      return defaultValue;
    }
    if (output == null || new String(output).equals("") || new String(output).equals("null"))
      return defaultValue;
    return new String(output);
  }
}
