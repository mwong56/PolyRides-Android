package com.mwong56.polyrides.models;

import com.parse.ParseObject;

import org.parceler.Parcel;

/**
 * Created by micha on 10/22/2015.
 */

@Parcel
public class Chat {
  String groupId;
  String lastMessage;
  String lastUserId;
  boolean newMessages = false;

  public static Chat ParseToMessages(ParseObject object) {
    String groupId = object.getString("groupId");
    String lastMessage = object.getString("lastMessage");
    String lastUserId = object.getString("lastUserId");
    return new Chat(groupId, lastMessage, lastUserId);
  }

  public Chat() {

  }

  public Chat(String groupId, boolean isNewMessage) {
    this.groupId = groupId;
    this.newMessages = isNewMessage;
  }

  public Chat(String groupId, String lastMessage, String lastUserId) {
    this.groupId = groupId;
    this.lastMessage = lastMessage;
    this.lastUserId = lastUserId;
//    this.newMessages = isNewMessage;
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
