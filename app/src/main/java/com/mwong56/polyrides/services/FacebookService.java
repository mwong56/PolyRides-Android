package com.mwong56.polyrides.services;

import com.facebook.AccessToken;

import rx.Observable;

/**
 * Created by micha on 10/14/2015.
 */
public interface FacebookService {
  Observable<String> getMyUserId(AccessToken token);

  Observable<String> getMyUserName(AccessToken token);

  Observable<String> getUserName(final AccessToken token, final String userId);
}
