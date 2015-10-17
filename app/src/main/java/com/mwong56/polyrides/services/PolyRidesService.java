package com.mwong56.polyrides.services;

import android.app.Activity;

import com.mwong56.polyrides.models.Date;
import com.mwong56.polyrides.models.Location;
import com.mwong56.polyrides.models.Ride;
import com.mwong56.polyrides.models.Time;
import com.parse.ParseUser;

import java.util.Collection;

import rx.Observable;

/**
 * Created by micha on 10/13/2015.
 */
public interface PolyRidesService {

  Observable<ParseUser> facebookLogin(Activity activity, Collection<String> permissions);

  void saveUserId(String userId);

  Observable<Void> saveNewRide(Ride ride);
}
