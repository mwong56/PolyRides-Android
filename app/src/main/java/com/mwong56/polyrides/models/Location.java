package com.mwong56.polyrides.models;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.android.gms.location.places.Place;
import com.google.android.gms.maps.model.LatLng;

import java.util.Locale;

/**
 * Created by micha on 10/10/2015.
 */
public class Location implements Parcelable {
  private LatLng latLng;
  private String address;
  private String name;
  private String city;

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
        String addressLine = address.getAddressLine(1);
        if (addressLine != null && addressLine.length() > 0) {
          this.address = addressLine;
        }

        if (address.getLocality() != null && address.getLocality().length() > 0) {
          this.city = address.getLocality();
        }
      } catch (Exception e) {
        // do nothing.
      }
    }
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
