package com.mwong56.polyrides.utils;

import android.content.IntentFilter;

import com.mwong56.polyrides.models.User;

/**
 * Created by micha on 10/21/2015.
 */
public class Utils {

  public static String getProfileImageUrl(String userId) {
    return "https://graph.facebook.com/" + userId + "/picture?type=large";
  }

  public static String extractOtherUserId(String groupId) {
    return groupId.replace(User.getUserId(), "");
  }

  public static IntentFilter buildParseIntentFilter() {
    IntentFilter filter = new IntentFilter();
    filter.addAction("android.intent.action.BOOT_COMPLETED");
    filter.addAction("android.intent.action.USER_PRESENT");
    filter.addAction("com.parse.push.intent.RECEIVE");
    filter.addAction("com.parse.push.intent.DELETE");
    filter.addAction("com.parse.push.intent.OPEN");
    return filter;
  }
}
