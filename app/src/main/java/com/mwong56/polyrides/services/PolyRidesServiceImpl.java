package com.mwong56.polyrides.services;

import com.mwong56.polyrides.models.Chat;
import com.mwong56.polyrides.models.Message;
import com.mwong56.polyrides.models.Ride;
import com.mwong56.polyrides.models.User;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParsePush;
import com.parse.ParseQuery;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by micha on 10/13/2015.
 */
public class PolyRidesServiceImpl implements PolyRidesService {

  private static final int ONE_DAY_AHEAD = 60 * 60 * 24 * 1000;
  private static final int ONE_DAY_BEHIND = -60 * 60 * 24 * 1000;

  private static class SingletonHolder {
    private static final PolyRidesService INSTANCE = new PolyRidesServiceImpl();
  }

  public static PolyRidesService get() {
    return SingletonHolder.INSTANCE;
  }

  private PolyRidesServiceImpl() {
    // do nothing.
  }

  @Override
  public Observable<Void> saveUserId(String userId) {
    Observable toReturn = Observable.fromCallable(() -> {
      ParseInstallation installation = ParseInstallation.getCurrentInstallation();
      installation.put("user", userId);
      installation.save();
      return null;
    });
    return toReturn.observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.newThread());
  }

  public Observable<Void> removeRide(Ride ride) {
    Observable toReturn = Observable.fromCallable(() -> {
      if (ride.getObjectId() == null) {
        throw new Exception("Object is null");
      }
      ParseQuery query = new ParseQuery("Ride");
      query.whereEqualTo("objectId", ride.getObjectId());
      ParseObject object = query.getFirst();
      object.delete();
      return null;
    });
    return toReturn.observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.newThread());
  }

  public Observable<Void> saveMessage(Message message) {
    Observable toReturn = Observable.fromCallable(() -> {
      ParseObject toSave = new ParseObject("Message");
      toSave.put("groupId", message.getGroupId());
      toSave.put("userId", message.getUserId());
      toSave.put("text", message.getText());
      toSave.put("userName", message.getUserName());
      toSave.save();
      return null;
    });

    return toReturn.observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.newThread());
  }

  @Override
  public Observable<Void> createChat(Chat chat, String userId, String otherUserId, String otherUserName) {
    Observable<Void> toReturn = Observable.fromCallable(() -> {
      ParseObject toSave = new ParseObject("Messages");
      toSave.put("counter", 1);
      toSave.put("groupId", chat.getGroupId());
      toSave.put("lastMessage", chat.getLastMessage());
      toSave.put("lastUserId", chat.getLastUserId());
      toSave.put("otherUserId", otherUserId);
      toSave.put("otherUserName", otherUserName);
      toSave.put("userId", userId);
      toSave.put("updatedAction", Calendar.getInstance().getTime());
      toSave.save();
      return null;
    });

    return toReturn.observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.newThread());
  }

  @Override
  public Observable<Void> updateChat(Chat chat, String userId) {
    Observable<Void> toReturn = Observable.fromCallable(() -> {
      ParseQuery query = new ParseQuery("Messages");
      query.whereEqualTo("groupId", chat.getGroupId());
      query.whereEqualTo("userId", userId);
      ParseObject object = query.getFirst();

      if (!userId.equals(User.getUserId())) {
        object.put("counter", object.getInt("counter") + 1);
      }

      object.put("lastMessage", chat.getLastMessage());
      object.put("lastUserId", chat.getLastUserId());
      object.put("updatedAction", Calendar.getInstance().getTime());

      object.save();
      return null;
    });

    return toReturn.observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.newThread());
  }

  @Override
  public Observable<Void> clearMessagesCounter(String groupId, String userId) {
    Observable<Void> toReturn = Observable.fromCallable(() -> {
      ParseQuery query = new ParseQuery("Messages");
      query.whereEqualTo("groupId", groupId);
      query.whereEqualTo("userId", userId);
      ParseObject object = query.getFirst();
      object.put("counter", 0);
      object.save();
      return null;
    });
    return toReturn.observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.newThread());
  }

  @Override
  public Observable<Void> sendPush(String userId, String userName, String groupId, String text) {
    Observable<Void> toReturn = Observable.fromCallable(() -> {
      ParseQuery pushQuery = ParseInstallation.getQuery();
      pushQuery.whereEqualTo("user", userId);

      ParsePush push = new ParsePush();
      push.setQuery(pushQuery);

      JSONObject object = new JSONObject();
      object.put("badge", "Increment");
      object.put("alert", userName + "\n" + text);
      object.put("groupId", groupId);
      push.setData(object);
      push.send();
      return null;
    });

    return toReturn.observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.newThread());
  }

  @Override
  public Observable<List<Chat>> getChats() {
    Observable toReturn = Observable.fromCallable(() -> {
      ParseQuery<ParseObject> query = ParseQuery.getQuery("Messages");
      query.whereEqualTo("userId", User.getUserId());
      List<Chat> messages = new ArrayList<>();
      for (ParseObject object : query.find()) {
        messages.add(Chat.ParseToMessages(object));
      }
      return messages;
    });
    return toReturn.observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.newThread());
  }

  @Override
  public Observable<Chat> getChat(String groupId) {
    Observable toReturn = Observable.fromCallable(() -> {
      ParseQuery<ParseObject> query = ParseQuery.getQuery("Messages");
      query.whereEqualTo("userId", User.getUserId());
      query.whereEqualTo("groupId", groupId);
      ParseObject object = query.getFirst();
      return object == null ? null : Chat.ParseToMessages(object);
    });

    return toReturn.observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.newThread());
  }

  @Override
  public Observable<List<Message>> getMessage(String groupId) {
    Observable toReturn = Observable.fromCallable(() -> {
      ParseQuery<ParseObject> query = ParseQuery.getQuery("Message");
      query.whereEqualTo("groupId", groupId);
      query.addAscendingOrder("createdAt");

      List<Message> messageList = new ArrayList<>();
      for (ParseObject object : query.find()) {
        messageList.add(Message.ParseToMessage(object));
      }
      return messageList;

    });
    return toReturn.observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.newThread());
  }

  @Override
  public Observable<Void> saveNewRide(Ride ride) {
    Observable toReturn = Observable.fromCallable(() -> {
          ParseObject newRide = new ParseObject("Ride");
          newRide.put("startLat", ride.getStart().getLatLng().latitude);
          newRide.put("startLong", ride.getStart().getLatLng().longitude);
          newRide.put("endLat", ride.getEnd().getLatLng().latitude);
          newRide.put("endLong", ride.getEnd().getLatLng().longitude);
          newRide.put("startCity", ride.getStart().getCity());
          newRide.put("endCity", ride.getEnd().getCity());
          newRide.put("dateTime", ride.getDate());
          newRide.put("cost", ride.getCost());
          newRide.put("seats", ride.getSeats());
          newRide.put("notes", ride.getNote());
          newRide.put("userId", ride.getUserId());
          newRide.put("name", User.getUserName());
          newRide.put("email", "\"\"");
          newRide.save();
          return null;
        }
    );
    return toReturn.observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.newThread());
  }

  @Override
  public Observable<List<Ride>> getRides(Date date) {
    Observable toReturn = Observable.fromCallable(() -> {
      ParseQuery<ParseObject> query = ParseQuery.getQuery("Ride");
      Date currentDate = Calendar.getInstance().getTime();
      Date newDate = new Date(date.getTime() + ONE_DAY_BEHIND);
      query.whereNotEqualTo("userId", User.getUserId());

      // If the date inputted - 1 day is greater than current date, search for date - 1
      if (currentDate.compareTo(newDate) <= 0) {
        query.whereGreaterThanOrEqualTo("dateTime", newDate);
      } else {
        query.whereGreaterThanOrEqualTo("dateTime", currentDate);
      }
      query.whereLessThanOrEqualTo("dateTime", new Date(date.getTime() + ONE_DAY_AHEAD));

      List<Ride> rides = new ArrayList<>();

      for (ParseObject object : query.find()) {
        rides.add(Ride.parseToRide(object));
      }
      return rides;
    });

    return toReturn.observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.newThread());
  }

  @Override
  public Observable<List<Ride>> getMyRides() {
    Observable toReturn = Observable.fromCallable(() -> {
      ParseQuery<ParseObject> query = ParseQuery.getQuery("Ride");
      Date currentDate = Calendar.getInstance().getTime();
      query.whereEqualTo("userId", User.getUserId());
      query.whereGreaterThanOrEqualTo("dateTime", currentDate);

      List<Ride> rides = new ArrayList<>();

      for (ParseObject object : query.find()) {
        rides.add(Ride.parseToRide(object));
      }
      return rides;
    });

    return toReturn.observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.newThread());
  }
}
