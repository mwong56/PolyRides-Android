package com.mwong56.polyrides.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.parse.ParseObject;

import java.util.Date;

/**
 * Created by micha on 10/17/2015.
 */
public class Ride implements Parcelable {
  private Location start;
  private Location end;
  private DateTime dateTime;
  private int cost;
  private int seats;
  private String note;
  private String userId;

  public static Ride parseToRide(ParseObject object) {
    int cost = object.getInt("cost");
    Date date = object.getDate("dateTime");
    String notes = object.getString("notes");
    int seats = object.getInt("seats");
    double endLat = object.getDouble("endLat");
    double endLong = object.getDouble("endLong");
    double startLat = object.getDouble("startLat");
    double startLong = object.getDouble("startLong");
    String endCity = object.getString("endCity");
    String startCity = object.getString("startCity");
    String userId = object.getString("userId");

    Location start = new Location(startLat, startLong, startCity);
    Location end = new Location(endLat, endLong, endCity);

    return new Ride(start, end, DateTime.dateToDateTime(date), cost, seats, notes, userId);
  }

  public Ride(Location start, Location end, DateTime dateTime, int cost, int seats, String note, String userId) {
    this.start = start;
    this.end = end;
    this.dateTime = dateTime;
    this.cost = cost;
    this.seats = seats;
    this.note = note;
    this.userId = userId;
  }


  protected Ride(Parcel in) {
    start = in.readParcelable(Location.class.getClassLoader());
    end = in.readParcelable(Location.class.getClassLoader());
    dateTime = in.readParcelable(DateTime.class.getClassLoader());
    cost = in.readInt();
    seats = in.readInt();
    note = in.readString();
    userId = in.readString();
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeParcelable(start, flags);
    dest.writeParcelable(end, flags);
    dest.writeParcelable(dateTime, flags);
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
    return dateTime.getDate();
  }

  public DateTime getDateTime() {
    return dateTime;
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

  public String getNote() {
    return note;
  }
}
