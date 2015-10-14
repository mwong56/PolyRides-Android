package com.mwong56.polyrides.services;

import android.app.Activity;

import com.parse.ParseFacebookUtils;
import com.parse.ParseInstallation;
import com.parse.ParseUser;

import java.util.Collection;

import rx.Observable;
import rx.bolts.TaskObservable;

/**
 * Created by micha on 10/13/2015.
 */
public class PolyRidesServiceImpl implements PolyRidesService {

  private static class SingletonHolder {
    private static final PolyRidesService INSTANCE = new PolyRidesServiceImpl();
  }

  public static PolyRidesService get() {
    return SingletonHolder.INSTANCE;
  }

  private PolyRidesServiceImpl() {
    // do nothing.
  }

  @Override
  public Observable<ParseUser> facebookLogin(final Activity activity, final Collection<String> permissions) {
    return Observable.defer(() -> TaskObservable.just(ParseFacebookUtils.logInWithReadPermissionsInBackground(activity, permissions)));
  }

  @Override
  public void saveParseuserInfo(String userId, String username) {
    ParseUser.getCurrentUser().put("userId", userId);
    ParseUser.getCurrentUser().put("userName", username);
    ParseInstallation installation = ParseInstallation.getCurrentInstallation();
    installation.put("userId", userId);
    installation.saveEventually();
  }

}


