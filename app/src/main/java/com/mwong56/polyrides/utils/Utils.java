package com.mwong56.polyrides.utils;

import android.app.Activity;
import android.content.Context;
import android.content.IntentFilter;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.mwong56.polyrides.models.User;

import timber.log.Timber;

/**
 * Created by micha on 10/21/2015.
 */
public class Utils {

  public static String getProfileImageUrl(String userId) {
    return "https://graph.facebook.com/" + userId + "/picture?type=large";
  }

  public static String extractOtherUserId(String groupId) {
    Timber.d("Current user: %s Group ID: %s ", User.getUserId(), groupId.toString());
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

  public static void hideKeyboard(Activity activity) {
    // Check if no view has focus:
    View view = activity.getCurrentFocus();
    if (view != null) {
      InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
      imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
  }
}
