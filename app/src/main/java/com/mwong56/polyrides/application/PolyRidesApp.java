package com.mwong56.polyrides.application;

import android.app.Application;
import android.content.Context;

import com.crashlytics.android.Crashlytics;
import com.facebook.FacebookSdk;
import com.mwong56.polyrides.BuildConfig;
import com.mwong56.polyrides.utils.CrashlyticsTree;
import com.parse.Parse;
import com.parse.ParseInstallation;
import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;
import com.tumblr.remember.Remember;

import io.fabric.sdk.android.Fabric;
import timber.log.Timber;
import timber.log.Timber.DebugTree;

/**
 * Created by micha on 10/13/2015.
 */
public class PolyRidesApp extends Application {

  public String messageGroupIdInForeground = null;
  public static PolyRidesApp INSTANCE = null;
  private RefWatcher refWatcher;

  public static RefWatcher getRefWatcher(Context context) {
    PolyRidesApp application = (PolyRidesApp) context.getApplicationContext();
    return application.refWatcher;
  }

  @Override
  public void onCreate() {
    super.onCreate();
    if (INSTANCE == null) {
      INSTANCE = this;
    }

    Fabric.with(this, new Crashlytics());

    Remember.init(getApplicationContext(), "com.mwong56.polyrides");
    FacebookSdk.sdkInitialize(getApplicationContext());
    refWatcher = LeakCanary.install(this);

    Parse.initialize(this, "tNVCuf8tnZgADqVCuBUegChrNis54koIAqnGzSJ3", "pghidrtoMmpnst4uebxV5oiwtsbnNMZKxs53Yxs6");
    ParseInstallation.getCurrentInstallation().saveInBackground();

    if (BuildConfig.DEBUG) {
      Timber.plant(new DebugTree());
    }
    Timber.plant(new CrashlyticsTree());
  }

  public void setMessageGroupIdInForeground(String groupId) {
    this.messageGroupIdInForeground = groupId;
  }

  public String getMessageGroupIdInForeground() {
    return this.messageGroupIdInForeground;
  }
}
