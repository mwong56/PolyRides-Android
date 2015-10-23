package com.mwong56.polyrides.services;

import android.app.Activity;

import com.mwong56.polyrides.models.Ride;
import com.parse.ParseUser;

import java.util.Collection;
import java.util.Date;
import java.util.List;

import rx.Observable;

/**
 * Created by micha on 10/13/2015.
 */
public interface PolyRidesService {

  Observable<ParseUser> facebookLogin(Activity activity, Collection<String> permissions);

  void saveUserId(String userId);

  Observable<Void> saveNewRide(Ride ride);

  Observable<List<Ride>> getRides(Date date, boolean myRides);

  Observable<Void> removeRide(Ride ride);

//  Observable<List<Messages>> getMessages(Ride ride);
}
