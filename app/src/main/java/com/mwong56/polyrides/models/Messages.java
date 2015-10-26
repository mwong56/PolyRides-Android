package com.mwong56.polyrides.models;

import com.parse.ParseObject;

import org.parceler.Parcel;

/**
 * Created by micha on 10/22/2015.
 */

@Parcel
public class Messages {
  int counter;
  String description;
  String groupId;
  String lastMessage;
  String lastUserId;
  boolean newMessages = false;

  public static Messages ParseToMessages(ParseObject object) {
    int counter = object.getInt("counter");
    String description = object.getString("description");
    String groupId = object.getString("groupId");
    String lastMessage = object.getString("lastMessage");
    String lastUserId = object.getString("lastUserId");
    return new Messages(counter, description, groupId, lastMessage, lastUserId);
  }

  public Messages() {

  }

  public Messages(int counter, String description, String groupId, String lastMessage, String lastUserId) {
    this.counter = counter;
    this.description = description;
    this.groupId = groupId;
    this.lastMessage = lastMessage;
    this.lastUserId = lastUserId;

    if (lastMessage == null || lastUserId == null) {
      this.newMessages = true;
    }
  }

  public int getCounter() {
    return counter;
  }

  public String getDescription() {
    return description;
  }

  public String getGroupId() {
    return groupId;
  }

  public String getOtherUserId() {
//    return groupId.replace(User.getUserId(), "");
    return "10153738107997317";
  }

  public String getLastMessage() {
    return lastMessage;
  }

  public String getLastUserId() {
    return lastUserId;
  }

  public void setCounter(int counter) {
    this.counter = counter;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public void setGroupId(String groupId) {
    this.groupId = groupId;
  }

  public void setLastMessage(String lastMessage) {
    this.lastMessage = lastMessage;
  }

  public void setLastUserId(String lastUserId) {
    this.lastUserId = lastUserId;
  }

  public boolean isNewMessages() {
    return this.newMessages;
  }

  public void setIsNewMessage(boolean flag) {
    this.newMessages = flag;
  }
}
