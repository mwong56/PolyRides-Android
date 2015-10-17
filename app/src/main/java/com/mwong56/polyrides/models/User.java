package com.mwong56.polyrides.models;

import com.tumblr.remember.Remember;

/**
 * Created by micha on 10/16/2015.
 */
public class User {

  public static String getUserId() {
    return Remember.getString("userId", null);
  }

  public static void setUserId(String userId) {
    Remember.putString("userId", userId);
  }
}
