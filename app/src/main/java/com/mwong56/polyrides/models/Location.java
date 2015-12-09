package com.mwong56.polyrides.models;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.android.gms.location.places.Place;
import com.google.android.gms.maps.model.LatLng;
import com.mwong56.polyrides.application.PolyRidesApp;

import java.util.Locale;

import timber.log.Timber;

/**
 * Created by micha on 10/10/2015.
 */
public class Location implements Parcelable {
  private LatLng latLng;
  private String address;
  private String name;
  private String city;

  public Location(double lat, double lng, String city) {
    latLng = new LatLng(lat, lng);
    this.city = city;
    if (this.city == null || this.city.length() == 0) {
      Geocoder geocoder = new Geocoder(PolyRidesApp.INSTANCE.getBaseContext(), Locale.ENGLISH);
      if (geocoder != null) {
        LatLng temp = this.latLng;
        Address address;
        try {
          address = geocoder.getFromLocation(temp.latitude, temp.longitude, 1).get(0);
          if (address.getLocality() != null && address.getLocality().length() > 0) {
            this.city = address.getLocality();
          }
        } catch (Exception e) {
          Timber.e(e, "GeoCoding parse failed.");
        }
      }
    }
  }


  public Location(Place place, Context context) {
    this.latLng = place.getLatLng();
    this.address = place.getAddress().toString();
    this.name = place.getName().toString();

    Geocoder geocoder = new Geocoder(context, Locale.ENGLISH);
    if (geocoder != null) {
      LatLng temp = this.latLng;
      Address address;
      try {
        address = geocoder.getFromLocation(temp.latitude, temp.longitude, 1).get(0);
        this.address = address.getLocality() + ", " + address.getAdminArea() + ", " + address.getCountryName();

        if (address.getLocality() != null && address.getLocality().length() > 0) {
          this.city = address.getLocality();
        }
      } catch (Exception e) {
        Timber.e(e, "Geocoding parse failed.");
      }
    }
  }

  /**
   * Returns the distance to the other location (in meters).
   */
  public double getDistanceTo(Location other) {
    return this.toLocation().distanceTo(other.toLocation());
  }

  public android.location.Location toLocation() {
    android.location.Location location = new android.location.Location(city);
    location.setLatitude(latLng.latitude);
    location.setLongitude(latLng.longitude);
    return location;
  }

  public LatLng getLatLng() {
    return latLng;
  }

  public String getAddress() {
    return address;
  }

  public String getName() {
    return name;
  }

  public String getCity() {
    if (city == null) {
      return address;
    } else {
      return city;
    }
  }

  protected Location(Parcel in) {
    latLng = in.readParcelable(LatLng.class.getClassLoader());
    address = in.readString();
    name = in.readString();
    city = in.readString();
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeParcelable(latLng, flags);
    dest.writeString(address);
    dest.writeString(name);
    dest.writeString(city);
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
