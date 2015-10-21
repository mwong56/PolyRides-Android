package com.mwong56.polyrides.utils;

/**
 * Created by micha on 10/21/2015.
 */
public class Utils {

  public static String getProfileImageUrl(String userId) {
    return "https://graph.facebook.com/" + userId + "/picture?type=large";
  }
}
