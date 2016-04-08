package com.mwong56.polyrides.models;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.format.DateUtils;

import com.parse.ParseObject;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by micha on 10/23/2015.
 */

public class Message implements Parcelable {
  String groupId;
  String userId;
  String text;
  String userName;
  Date date;

  protected Message(Parcel in) {
    groupId = in.readString();
    userId = in.readString();
    text = in.readString();
    userName = in.readString();
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(groupId);
    dest.writeString(userId);
    dest.writeString(text);
    dest.writeString(userName);
  }

  @Override
  public int describeContents() {
    return 0;
  }

  public static final Creator<Message> CREATOR = new Creator<Message>() {
    @Override
    public Message createFromParcel(Parcel in) {
      return new Message(in);
    }

    @Override
    public Message[] newArray(int size) {
      return new Message[size];
    }
  };

  public static Message ParseToMessage(ParseObject object) {
    String groupId = object.getString("groupId");
    String userId = object.getString("userId");
    String text = object.getString("text");
    String userName = object.getString("userName");
    Date date = object.getCreatedAt();
    return new Message(groupId, userId, text, userName, date);
  }

  private Message() {
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
