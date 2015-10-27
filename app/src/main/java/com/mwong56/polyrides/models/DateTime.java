package com.mwong56.polyrides.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by micha on 10/10/2015.
 */
public class DateTime implements Parcelable {
  private int year;
  private int month;
  private int day;
  private int hour;
  private int minute;

  public static DateTime dateToDateTime(Date date) {
    Calendar cal = Calendar.getInstance();
    cal.setTime(date);
    return new DateTime(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH),
        cal.get(Calendar.DAY_OF_MONTH), cal.get(Calendar.HOUR), cal.get(Calendar.SECOND));
  }

  public DateTime(int year, int month, int day, int hour, int minute) {
    this.year = year;
    this.month = month;
    this.day = day;
    this.hour = hour;
    this.minute = minute;
  }

  protected DateTime(Parcel in) {
    year = in.readInt();
    month = in.readInt();
    day = in.readInt();
    hour = in.readInt();
    minute = in.readInt();
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeInt(year);
    dest.writeInt(month);
    dest.writeInt(day);
    dest.writeInt(hour);
    dest.writeInt(minute);
  }

  @Override
  public int describeContents() {
    return 0;
  }

  public static final Creator<DateTime> CREATOR = new Creator<DateTime>() {
    @Override
    public DateTime createFromParcel(Parcel in) {
      return new DateTime(in);
    }

    @Override
    public DateTime[] newArray(int size) {
      return new DateTime[size];
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

  public int getHour() {
    return hour;
  }

  public int getMinute() {
    return minute;
  }


  public String printDate() {
    return (month + 1) + "/" + day + "/" + year;
  }

  public String printTime() {
    return hour + ":" + minute;

  }

  public Date getDate() {
    Calendar cal = Calendar.getInstance();
    cal.set(year, month, day, hour, minute);
    return cal.getTime();
  }
}
