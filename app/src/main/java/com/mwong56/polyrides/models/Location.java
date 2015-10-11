package com.mwong56.polyrides.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.android.gms.location.places.Place;
import com.google.android.gms.maps.model.LatLng;

/**
 * Created by micha on 10/10/2015.
 */
public class Location implements Parcelable {
  private LatLng latLng;
  private CharSequence address;
  private CharSequence name;

  public Location(Place place) {
    this.latLng = place.getLatLng();
    this.address = place.getAddress();
    this.name = place.getName();
  }

  public LatLng getLatLng() {
    return latLng;
  }

  public CharSequence getAddress() {
    return address;
  }

  public CharSequence getName() {
    return name;
  }

  public static Creator<Location> getCREATOR() {
    return CREATOR;
  }

  protected Location(Parcel in) {
    latLng = in.readParcelable(LatLng.class.getClassLoader());
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeParcelable(latLng, flags);
  }

  @Override
  public int describeContents() {
    return 0;
  }

  public static final Creator<Location> CREATOR = new Creator<Location>() {
    @Override
    public Location createFromParcel(Parcel in) {
      return new Location(in);
    }

    @Override
    public Location[] newArray(int size) {
      return new Location[size];
    }
  };
}
