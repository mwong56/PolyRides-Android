package com.mwong56.polyrides.models;

import com.parse.ParseObject;

import org.parceler.Parcel;

/**
 * Created by micha on 10/23/2015.
 */

@Parcel
public class Message {
  String groupId;
  String userId;
  String text;
  String userName;

  public static Message ParseToMessage(ParseObject object) {
    String groupId = object.getString("groupId");
    String userId = object.getString("userId");
    String text = object.getString("text");
    String userName = object.getString("userName");
    return new Message(groupId, userId, text, userName, false);
  }

  public Message() {
  }

  public Message(String groupId, String userId, String text, String userName, boolean isNewMessage) {
    this.groupId = groupId;
    this.userId = userId;
    this.text = text;
    this.userName = userName;
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

  public boolean isOutgoingMessage() {
    return User.getUserId().equals(userId);
  }
}
