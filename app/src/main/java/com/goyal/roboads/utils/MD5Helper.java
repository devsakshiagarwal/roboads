package com.goyal.roboads.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import static org.jivesoftware.smack.util.StringUtils.MD5;

public class MD5Helper {

  public static String getMd5Encrypted(final String s) {
    try {
      MessageDigest digest = java.security.MessageDigest.getInstance(MD5);
      digest.update(s.getBytes());
      byte[] messageDigest = digest.digest();

      StringBuilder hexString = new StringBuilder();
      for (byte aMessageDigest : messageDigest) {
        String h = Integer.toHexString(0xFF & aMessageDigest);
        while (h.length() < 2) {
          h = "0".concat(h);
        }
        hexString.append(h);
      }
      return hexString.toString();
    } catch (NoSuchAlgorithmException e) {
      e.printStackTrace();
    }
    return "";
  }
}