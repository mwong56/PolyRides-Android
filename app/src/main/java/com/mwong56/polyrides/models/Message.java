package com.mwong56.polyrides.models;

import android.text.format.DateUtils;

import com.parse.ParseObject;

import org.parceler.Parcel;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by micha on 10/23/2015.
 */

@Parcel
public class Message {
  String groupId;
  String userId;
  String text;
  String userName;
  Date date;

  public static Message ParseToMessage(ParseObject object) {
    String groupId = object.getString("groupId");
    String userId = object.getString("userId");
    String text = object.getString("text");
    String userName = object.getString("userName");
    Date date = object.getCreatedAt();
    return new Message(groupId, userId, text, userName, date);
  }

  public Message() {
  }

  public Message(String groupId, String userId, String text, String userName, Date date) {
    this.groupId = groupId;
    this.userId = userId;
    this.text = text;
    this.userName = userName;
    this.date = date;
  }

  public String getGroupId() {
    return groupId;
  }

  public String getUserId() {
    return userId;
  }

  public String getText() {
    return text;
  }

  public String getUserName() {
    return userName;
  }

  public String getDate() {
    if (DateUtils.isToday(this.date.getTime())) {
      SimpleDateFormat dateFormat = new SimpleDateFormat("h:mm a");
      return dateFormat.format(this.date);
    }
    SimpleDateFormat dateFormat = new SimpleDateFormat("MMM d");
    return dateFormat.format(this.date);
  }

  public boolean isOutgoingMessage() {
    return User.getUserId().equals(userId);
  }
}
