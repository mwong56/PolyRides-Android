package com.mwong56.polyrides.services;

import android.app.Activity;

import com.mwong56.polyrides.models.Ride;
import com.mwong56.polyrides.models.User;
import com.parse.ParseFacebookUtils;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.bolts.TaskObservable;
import rx.exceptions.OnErrorThrowable;
import rx.schedulers.Schedulers;

/**
 * Created by micha on 10/13/2015.
 */
public class PolyRidesServiceImpl implements PolyRidesService {

  private static final int ONE_DAY_AHEAD = 60 * 60 * 24 * 1000;
  private static final int ONE_DAY_BEHIND = -60 * 60 * 24 * 1000;

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
        .observeOn(AndroidSchedulers.mainThread())
        .subscribeOn(Schedulers.newThread())
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
  public Observable<Void> saveNewRide(Ride ride) {
    Observable toReturn = Observable.create(subscriber -> {
          ParseObject newRide = new ParseObject("Ride");
          newRide.put("startLat", ride.getStart().getLatLng().latitude);
          newRide.put("startLong", ride.getStart().getLatLng().longitude);
          newRide.put("endLat", ride.getEnd().getLatLng().latitude);
          newRide.put("endLong", ride.getEnd().getLatLng().longitude);
          newRide.put("startCity", ride.getStart().getCity());
          newRide.put("endCity", ride.getEnd().getCity());
          newRide.put("dateTime", ride.getDate());
          newRide.put("cost", ride.getCost());
          newRide.put("seats", ride.getSeats());
          newRide.put("notes", ride.getNote());
          newRide.put("userId", ride.getUserId());
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
    return toReturn.observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.newThread());
  }

  @Override
  public Observable<List<Ride>> getRides(Date passengerRequestDate) {
    Observable toReturn = Observable.create(subscriber -> {
      ParseQuery<ParseObject> query = ParseQuery.getQuery("Ride");
      Date currentDate = Calendar.getInstance().getTime();
      query.whereGreaterThanOrEqualTo("dateTime", currentDate);
      if (currentDate.compareTo(passengerRequestDate) > 0) {
        Date newDate = new Date(currentDate.getTime() + ONE_DAY_BEHIND);

        if (newDate.compareTo(passengerRequestDate) < 0) {
          query.whereGreaterThanOrEqualTo("dateTime", new Date(currentDate.getTime() + ONE_DAY_BEHIND));
          query.whereLessThanOrEqualTo("dateTime", new Date(currentDate.getTime() + ONE_DAY_AHEAD));
        }
      }

      query.whereNotEqualTo("userId", User.getUserId());
      try {
        List<Ride> rides = new ArrayList<>();

        for (ParseObject object : query.find()) {
          rides.add(Ride.parseToRide(object));
        }
        if (!subscriber.isUnsubscribed()) {
          subscriber.onNext(rides);
          subscriber.onCompleted();
        }
      } catch (Exception e) {
        subscriber.onError(e);
      }
    });

    return toReturn.observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.newThread());
  }

}


