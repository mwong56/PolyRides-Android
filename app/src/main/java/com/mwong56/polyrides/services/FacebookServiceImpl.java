package com.mwong56.polyrides.services;

import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;

import org.json.JSONException;
import org.json.JSONObject;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by micha on 10/14/2015.
 */
public class FacebookServiceImpl implements FacebookService {

  private static class SingletonHolder {
    private static final FacebookService INSTANCE = new FacebookServiceImpl();
  }

  public static FacebookService get() {
    return SingletonHolder.INSTANCE;
  }

  private FacebookServiceImpl() {
    // do nothing.
  }

  @Override
  public Observable<String> getUserId(final AccessToken token) {
    return Observable.create(subscriber ->
        GraphRequest.newMeRequest(token, (jsonObject, graphResponse) -> {
          if (jsonObject == null) {
            subscriber.onError(new Exception("Error: " + graphResponse.toString()));
          } else {
            if (!subscriber.isUnsubscribed()) {
              String userId = null;
              try {
                userId = jsonObject.getString("id");
              } catch (JSONException e) {
                subscriber.onError(e);
              }

              subscriber.onNext(userId);
              subscriber.onCompleted();
            }
          }
        }).executeAsync());
  }

  @Override
  public Observable<String> getUserName(final AccessToken token) {
    Observable<String> toReturn = Observable.create(subscriber ->
        GraphRequest.newMeRequest(token, (jsonObject, graphResponse) -> {
          if (jsonObject == null) {
            subscriber.onError(new Exception("Error: " + graphResponse.toString()));
          } else {
            if (!subscriber.isUnsubscribed()) {
              String userName = null;
              try {
                userName = jsonObject.getString("name");
              } catch (JSONException e) {
                subscriber.onError(e);
              }

              subscriber.onNext(userName);
              subscriber.onCompleted();
            }
          }
        }).executeAsync());
    return toReturn;
  }
}