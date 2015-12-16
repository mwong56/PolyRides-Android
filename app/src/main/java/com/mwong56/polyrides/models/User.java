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

  public static String getUserName() {
    return Remember.getString("userName", null);
  }

  public static void setUserName(String userName) {
    Remember.putString("userName", userName);
  }

  public static void logout(Remember.Callback callback) {
    Remember.clear(callback);
  }

  public static void logout() {
    logout(null);
  }
}
