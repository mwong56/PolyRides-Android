package com.mwong56.polyrides.services;

import android.content.Context;

import com.google.android.gms.location.places.Place;

import rx.Observable;

/**
 * Created by micha on 10/9/2015.
 */
public interface LocationService {

  Observable<Place> getCurrentLocation(Context context);

}
