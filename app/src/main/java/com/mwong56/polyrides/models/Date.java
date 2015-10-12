package com.mwong56.polyrides.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by micha on 10/10/2015.
 */
public class Date implements Parcelable{
  private int year;
  private int month;
  private int day;

  public Date(int year, int month, int day) {
    this.year = year;
    this.month = month;
    this.day = day;
  }

  protected Date(Parcel in) {
    year = in.readInt();
    month = in.readInt();
    day = in.readInt();
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeInt(year);
    dest.writeInt(month);
    dest.writeInt(day);
  }

  @Override
  public int describeContents() {
    return 0;
  }

  public static final Creator<Date> CREATOR = new Creator<Date>() {
    @Override
    public Date createFromParcel(Parcel in) {
      return new Date(in);
    }

    @Override
    public Date[] newArray(int size) {
      return new Date[size];
    }
  };

  public int getYear() {
    return year;
  }

  public int getMonth() {
    return month;
  }

  public int getDay() {
    return day;
  }

  @Override
  public String toString() {
    return (month + 1) + "/" + day + "/" + year;
  }
}
