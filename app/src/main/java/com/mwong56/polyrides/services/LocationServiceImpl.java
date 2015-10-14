package com.mwong56.polyrides.services;

import android.content.Context;

import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceLikelihood;

import pl.charmas.android.reactivelocation.ReactiveLocationProvider;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by micha on 10/10/2015.
 */
public class LocationServiceImpl implements LocationService {

  private static class SingletonHolder {
    private static LocationServiceImpl INSTANCE = new LocationServiceImpl();
  }

  public static LocationServiceImpl instance() {
    return SingletonHolder.INSTANCE;
  }

  @Override
  public Observable<Place> getCurrentLocation(Context context) {
    ReactiveLocationProvider locationProvider = new ReactiveLocationProvider(context);
    return locationProvider.getCurrentPlace(null)
        .observeOn(Schedulers.newThread())
        .subscribeOn(AndroidSchedulers.mainThread())
        .flatMap(buffer -> {
          PlaceLikelihood placeLikelihood = buffer.get(0);
          if (placeLikelihood != null) {
            return Observable.just(placeLikelihood.getPlace());
          } else {
            return Observable.error(new NoLocationException());
          }
        });
  }

  class NoLocationException extends Exception {
    public NoLocationException() {
      super("Could not find location.");
    }

  }
}
