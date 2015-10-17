package com.mwong56.polyrides.services;

import android.app.Activity;

import com.mwong56.polyrides.models.Date;
import com.mwong56.polyrides.models.Location;
import com.mwong56.polyrides.models.Time;
import com.parse.ParseException;
import com.parse.ParseFacebookUtils;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.Calendar;
import java.util.Collection;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.bolts.TaskObservable;
import rx.exceptions.OnErrorThrowable;
import rx.schedulers.Schedulers;

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
    return Observable.defer(() -> TaskObservable.just(ParseFacebookUtils.logInWithReadPermissionsInBackground(activity, permissions)))
        .observeOn(Schedulers.newThread())
        .subscribeOn(AndroidSchedulers.mainThread())
        .map(parseUser -> {
          if (parseUser == null) {
            throw OnErrorThrowable.from(new Exception("Parse user is null"));
          } else {
            return parseUser;
          }
        });
  }

  @Override
  public void saveUserId(String userId) {
    ParseInstallation installation = ParseInstallation.getCurrentInstallation();
    installation.put("userId", userId);
    installation.saveEventually();
  }

  @Override
  public Observable<Void> saveNewRide(Location start, Location end, Date date, Time time, int cost, int seats,
                                      String note, String userId) {
    return Observable.create(subscriber -> {
          ParseObject newRide = new ParseObject("Ride");
          newRide.put("startLat", start.getLatLng().latitude);
          newRide.put("startLong", start.getLatLng().longitude);
          newRide.put("endLat", end.getLatLng().latitude);
          newRide.put("endLong", end.getLatLng().longitude);
          newRide.put("startCity", start.getCity());
          newRide.put("endCity", end.getCity());
          Calendar cal = Calendar.getInstance();
          cal.set(date.getYear(), date.getMonth(), date.getDay(), time.getHour(), time.getMinute());
          newRide.put("dateTime", cal.getTime());
          newRide.put("cost", cost);
          newRide.put("seats", seats);
          newRide.put("notes", note);
          newRide.put("userId", userId);
          newRide.saveInBackground(e -> {
            if (e != null) {
              subscriber.onError(e);
            } else {
              if (!subscriber.isUnsubscribed()) {
                subscriber.onNext(null);
                subscriber.onCompleted();
              }
            }
          });
        }
    );
  }

}


