package com.mwong56.polyrides.services;

import com.mwong56.polyrides.models.Chat;
import com.mwong56.polyrides.models.Message;
import com.mwong56.polyrides.models.Ride;

import java.util.Date;
import java.util.List;

import rx.Observable;

/**
 * Created by micha on 10/13/2015.
 */
public interface PolyRidesService {
  Observable<Void> saveUserId(String userId);

  Observable<Void> saveNewRide(Ride ride);

  Observable<List<Ride>> getRides(Date date, boolean myRides);

  Observable<Void> removeRide(Ride ride);

  Observable<List<Message>> getMessage(String groupId);

  Observable<Void> saveMessage(Message message);

  Observable<List<Chat>> getChats();

  Observable<Chat> getChat(String groupId);

  Observable<Void> createChat(Chat chat, String userId, String otherUserId, String otherUserName);

  Observable<Void> updateChat(Chat chat, String userId);

  Observable<Void> clearMessagesCounter(String groupId, String userId);

  Observable<Void> sendPush(String otherUserId, String userName, String groupId, String text);
}
