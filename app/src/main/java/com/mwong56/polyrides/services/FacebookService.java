package com.mwong56.polyrides.services;

import com.facebook.AccessToken;

import org.json.JSONObject;

import rx.Observable;

/**
 * Created by micha on 10/14/2015.
 */
public interface FacebookService {
  Observable<JSONObject> getUserDetails(AccessToken token);
}
