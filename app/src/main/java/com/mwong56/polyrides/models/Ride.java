package com.mwong56.polyrides.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by micha on 10/17/2015.
 */
public class Ride implements Parcelable {
  private Location start;
  private Location end;
  private Date date;
  private Time time;
  private int cost;
  private int seats;
  private String note;
  private String userId;

  public Ride(Location start, Location end, Date date, Time time, int cost, int seats, String note, String userId) {
    this.start = start;
    this.end = end;
    this.date = date;
    this.time = time;
    this.cost = cost;
    this.seats = seats;
    this.note = note;
    this.userId = userId;
  }

  protected Ride(Parcel in) {
    start = in.readParcelable(Location.class.getClassLoader());
    end = in.readParcelable(Location.class.getClassLoader());
    date = in.readParcelable(Date.class.getClassLoader());
    time = in.readParcelable(Time.class.getClassLoader());
    cost = in.readInt();
    seats = in.readInt();
    note = in.readString();
    userId = in.readString();
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeParcelable(start, flags);
    dest.writeParcelable(end, flags);
    dest.writeParcelable(date, flags);
    dest.writeParcelable(time, flags);
    dest.writeInt(cost);
    dest.writeInt(seats);
    dest.writeString(note);
    dest.writeString(userId);
  }

  @Override
  public int describeContents() {
    return 0;
  }

  public static final Creator<Ride> CREATOR = new Creator<Ride>() {
    @Override
    public Ride createFromParcel(Parcel in) {
      return new Ride(in);
    }

    @Override
    public Ride[] newArray(int size) {
      return new Ride[size];
    }
  };

  public Date getDate() {
    return date;
  }

  public Location getStart() {
    return start;
  }

  public Location getEnd() {
    return end;
  }

  public int getCost() {
    return cost;
  }

  public int getSeats() {
    return seats;
  }

  public String getUserId() {
    return userId;
  }

  public Time getTime() {
    return time;
  }

  public String getNote() {
    return note;
  }

}
