package com.goyal.roboads.utils;

import android.util.Patterns;

public class CommonUtils {

  private static final String NAME_REGEX = "^[\\p{L} .'-]+$";

  public static boolean isNameValid(String name) {
    return name.matches(NAME_REGEX);
  }

  public static boolean isEmailValid(String email) {
    return Patterns.EMAIL_ADDRESS.matcher(email).matches();
  }
}
