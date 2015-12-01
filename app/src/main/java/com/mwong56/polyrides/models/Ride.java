package com.mwong56.polyrides.models;

import com.parse.ParseObject;
import org.parceler.Parcel;

import java.util.Date;

/**
 * Created by micha on 10/17/2015.
 */
@Parcel
public class Ride {
  Location start;
  Location end;
  DateTime dateTime;
  int cost;
  int seats;
  String note;
  String userId;
  String objectId;

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
    String objectId = object.getObjectId();

    Location start = new Location(startLat, startLong, startCity);
    Location end = new Location(endLat, endLong, endCity);

    return new Ride(start, end, DateTime.dateToDateTime(date), cost, seats, notes, userId, objectId);
  }

  public Ride() {}

  public Ride(Location start, Location end, DateTime dateTime, int cost, int seats, String note, String userId, String objectId) {
    this.start = start;
    this.end = end;
    this.dateTime = dateTime;
    this.cost = cost;
    this.seats = seats;
    this.note = note;
    this.userId = userId;
    this.objectId = objectId;
  }

  public String getObjectId() {
    return objectId;
  }

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
