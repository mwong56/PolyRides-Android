package com.mwong56.polyrides.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.parse.ParseObject;


/**
 * Created by micha on 10/22/2015.
 */

public class Chat implements Parcelable {
  int counter;
  String groupId;
  String lastMessage;
  String lastUserId;
  boolean newMessages = false;

  protected Chat(Parcel in) {
    counter = in.readInt();
    groupId = in.readString();
    lastMessage = in.readString();
    lastUserId = in.readString();
    newMessages = in.readByte() != 0;
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeInt(counter);
    dest.writeString(groupId);
    dest.writeString(lastMessage);
    dest.writeString(lastUserId);
    dest.writeByte((byte) (newMessages ? 1 : 0));
  }

  @Override
  public int describeContents() {
    return 0;
  }

  public static final Creator<Chat> CREATOR = new Creator<Chat>() {
    @Override
    public Chat createFromParcel(Parcel in) {
      return new Chat(in);
    }

    @Override
    public Chat[] newArray(int size) {
      return new Chat[size];
    }
  };

  public static Chat ParseToMessages(ParseObject object) {
    int counter = object.getInt("counter");
    String groupId = object.getString("groupId");
    String lastMessage = object.getString("lastMessage");
    String lastUserId = object.getString("lastUserId");
    Chat chat = new Chat(groupId, lastMessage, lastUserId);
    chat.setCounter(counter);
    return chat;
  }

  private Chat() {}

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
