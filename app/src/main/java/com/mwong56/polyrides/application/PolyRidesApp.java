package com.mwong56.polyrides.application;

import android.app.Application;
import android.util.Log;

import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseFacebookUtils;
import com.parse.ParseInstallation;
import com.parse.ParsePush;
import com.parse.SaveCallback;
import com.tumblr.remember.Remember;

/**
 * Created by micha on 10/13/2015.
 */
public class PolyRidesApp extends Application {

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
    ParsePush.subscribeInBackground("", new SaveCallback() {
      @Override
      public void done(ParseException e) {
        if (e != null) {
          Log.e("Parse Push", e.toString());
        }
      }
    });
  }
}
