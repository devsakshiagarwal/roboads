package com.goyal.roboads.utils;

import android.annotation.SuppressLint;
import java.security.NoSuchAlgorithmException;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

class AESHelper {

  public static SecretKey getSecretKey() {
    KeyGenerator keyGenerator = null;
    try {
      keyGenerator = KeyGenerator.getInstance("AES");
    } catch (NoSuchAlgorithmException e) {
      e.printStackTrace();
    }
    assert keyGenerator != null;
    keyGenerator.init(256);
    return keyGenerator.generateKey();
  }

  @SuppressLint("GetInstance")
  public static String encrypt(byte[] plaintext, SecretKey key) throws Exception
  {
    Cipher cipher = Cipher.getInstance("AES");
    SecretKeySpec keySpec = new SecretKeySpec(key.getEncoded(), "AES");
    cipher.init(Cipher.ENCRYPT_MODE, keySpec);
    return new String(cipher.doFinal(plaintext));
  }

  @SuppressLint("GetInstance")
  public static String decrypt(byte[] cipherText, SecretKey key)
  {
    try {
      Cipher cipher = Cipher.getInstance("AES");
      SecretKeySpec keySpec = new SecretKeySpec(key.getEncoded(), "AES");
      cipher.init(Cipher.DECRYPT_MODE, keySpec);
      return new String(cipher.doFinal(cipherText));
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }
}