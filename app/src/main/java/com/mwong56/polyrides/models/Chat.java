package com.mwong56.polyrides.models;

import com.parse.ParseObject;

import org.parceler.Parcel;

/**
 * Created by micha on 10/22/2015.
 */

@Parcel
public class Chat {
  int counter;
  String groupId;
  String lastMessage;
  String lastUserId;
  boolean newMessages = false;

  public static Chat ParseToMessages(ParseObject object) {
    int counter = object.getInt("counter");
    String groupId = object.getString("groupId");
    String lastMessage = object.getString("lastMessage");
    String lastUserId = object.getString("lastUserId");
    Chat chat = new Chat(groupId, lastMessage, lastUserId);
    chat.setCounter(counter);
    return chat;
  }

  public Chat() {}

  public Chat(String groupId, String lastMessage, String lastUserId) {
    this.groupId = groupId;
    this.lastMessage = lastMessage;
    this.lastUserId = lastUserId;
  }

  public String getGroupId() {
    return groupId;
  }

  public String getOtherUserId() {
    return groupId.replace(User.getUserId(), "");
  }

  public String getLastMessage() {
    return lastMessage;
  }

  public String getLastUserId() {
    return lastUserId;
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

  public void setCounter(int counter) {
    this.counter = counter;
  }

  public int getCounter() {
    return this.counter;
  }

}
