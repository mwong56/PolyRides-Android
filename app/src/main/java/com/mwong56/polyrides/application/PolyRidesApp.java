package com.mwong56.polyrides.application;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseFacebookUtils;
import com.parse.ParseInstallation;

/**
 * Created by micha on 10/13/2015.
 */
public class PolyRidesApp extends Application {

  @Override
  public void onCreate() {
    super.onCreate();

    // Enable Local Datastore.
    Parse.enableLocalDatastore(this);

    Parse.initialize(this, "c6e3kc0AWh95MO0sBssnh00v4KKPkoZh1cQc4aBa", "SZ6GYt8TIixLjdpgKKT1NAqQqoc57zT9WNx0lyj7");
    ParseInstallation.getCurrentInstallation().saveInBackground();
    ParseFacebookUtils.initialize(this);
  }
}
