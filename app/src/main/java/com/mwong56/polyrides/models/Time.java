package com.mwong56.polyrides.models;

/**
 * Created by micha on 10/10/2015.
 */
public class Time {
  private int hour;
  private int minute;

  public Time(int hour, int minute) {
    this.hour = hour;
    this.minute = minute;
  }

  public int getHour() {
    return hour;
  }

  public int getMinute() {
    return minute;
  }
}
