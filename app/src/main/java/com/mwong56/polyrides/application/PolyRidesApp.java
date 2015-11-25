package com.mwong56.polyrides.application;

import android.app.Application;

import com.facebook.FacebookSdk;
import com.parse.Parse;
import com.parse.ParseInstallation;
import com.tumblr.remember.Remember;

/**
 * Created by micha on 10/13/2015.
 */
public class PolyRidesApp extends Application {

  public String messageGroupIdInForeground = null;
  public static PolyRidesApp INSTANCE = null;

  @Override
  public void onCreate() {
    super.onCreate();
    if (INSTANCE == null) {
      INSTANCE = this;
    }

    Remember.init(getApplicationContext(), "com.mwong56.polyrides");
    FacebookSdk.sdkInitialize(getApplicationContext());

    // Enable Local Datastore.
    Parse.enableLocalDatastore(this);
    Parse.initialize(this, "tNVCuf8tnZgADqVCuBUegChrNis54koIAqnGzSJ3", "pghidrtoMmpnst4uebxV5oiwtsbnNMZKxs53Yxs6");
    ParseInstallation.getCurrentInstallation().saveInBackground();
    Parse.setLogLevel(Parse.LOG_LEVEL_VERBOSE);
  }

  public void setMessageGroupIdInForeground(String groupId) {
    this.messageGroupIdInForeground = groupId;
  }

  public String getMessageGroupIdInForeground() {
    return this.messageGroupIdInForeground;
  }
}
