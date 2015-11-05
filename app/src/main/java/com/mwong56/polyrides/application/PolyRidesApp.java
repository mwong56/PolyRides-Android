package com.mwong56.polyrides.application;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseFacebookUtils;
import com.parse.ParseInstallation;
import com.tumblr.remember.Remember;

/**
 * Created by micha on 10/13/2015.
 */
public class PolyRidesApp extends Application {

  public String messageGroupIdInForeground = null;

  @Override
  public void onCreate() {
    super.onCreate();

    Remember.init(getApplicationContext(), "com.mwong56.polyrides");

    // Enable Local Datastore.
    Parse.enableLocalDatastore(this);

    Parse.initialize(this, "7OAm6NrMXr29nbc6As83oPjGTZBHk5ZHgNpMn8Aw", "K2MVOxHv29ogGzUut5JSNPjIX3IRcl1yaSDQCOVe");
    ParseInstallation.getCurrentInstallation().saveInBackground();
    ParseFacebookUtils.initialize(this);
    Parse.setLogLevel(Parse.LOG_LEVEL_VERBOSE);

  }

  public void setMessageGroupIdInForeground(String groupId) {
    this.messageGroupIdInForeground = groupId;
  }

  public String getMessageGroupIdInForeground() {
    return this.messageGroupIdInForeground;
  }
}
