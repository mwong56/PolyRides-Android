package com.mwong56.polyrides.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by micha on 10/10/2015.
 */
public class Time implements Parcelable {
  private int hour;
  private int minute;

  public Time(int hour, int minute) {
    this.hour = hour;
    this.minute = minute;
  }

  protected Time(Parcel in) {
    hour = in.readInt();
    minute = in.readInt();
  }

  public int getHour() {
    return hour;
  }

  public int getMinute() {
    return minute;
  }

  @Override
  public String toString() {
    return hour + ":" + minute;
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeInt(hour);
    dest.writeInt(minute);
  }

  @Override
  public int describeContents() {
    return 0;
  }

  public static final Creator<Time> CREATOR = new Creator<Time>() {
    @Override
    public Time createFromParcel(Parcel in) {
      return new Time(in);
    }

    @Override
    public Time[] newArray(int size) {
      return new Time[size];
    }
  };


}
