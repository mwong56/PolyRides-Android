package com.mwong56.polyrides.models;

/**
 * Created by micha on 10/10/2015.
 */
public class Date {
  private int year;
  private int month;
  private int day;

  public Date(int year, int month, int day) {
    this.year = year;
    this.month = month;
    this.day = day;
  }

  public int getYear() {
    return year;
  }

  public int getMonth() {
    return month;
  }

  public int getDay() {
    return day;
  }
}
