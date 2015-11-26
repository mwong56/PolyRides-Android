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
    Observable toReturn = Observable.create(subscriber -> {
      try {
        ParseInstallation installation = ParseInstallation.getCurrentInstallation();
        installation.put("user", userId);
        installation.save();

        if (!subscriber.isUnsubscribed()) {
          subscriber.onNext(null);
          subscriber.onCompleted();
        }
      } catch (Exception e) {
        subscriber.onError(e);
      }
    });
    return toReturn.observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.newThread());
  }

  public Observable<Void> removeRide(Ride ride) {
    Observable toReturn = Observable.create(subscriber -> {
      if (ride.getObjectId() == null) {
        subscriber.onError(new Exception("Object is null"));
      }
      ParseQuery query = new ParseQuery("Ride");
      query.whereEqualTo("objectId", ride.getObjectId());

      try {
        ParseObject object = query.getFirst();
        object.delete();

        if (!subscriber.isUnsubscribed()) {
          subscriber.onNext(null);
          subscriber.onCompleted();
        }
      } catch (Exception e) {
        subscriber.onError(e);
      }
    });
    return toReturn.observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.newThread());
  }

  public Observable<Void> saveMessage(Message message) {
    Observable toReturn = Observable.create(subscriber -> {
      ParseObject toSave = new ParseObject("Message");
      toSave.put("groupId", message.getGroupId());
      toSave.put("userId", message.getUserId());
      toSave.put("text", message.getText());
      toSave.put("userName", message.getUserName());

      try {
        toSave.save();
        if (!subscriber.isUnsubscribed()) {
          subscriber.onNext(null);
          subscriber.onCompleted();
        }
      } catch (Exception e) {
        subscriber.onError(e);
      }
    });

    return toReturn.observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.newThread());
  }

  @Override
  public Observable<Void> createChat(Chat chat, String userId, String otherUserId, String otherUserName) {
    Observable<Void> toReturn = Observable.create(subscriber -> {
      ParseObject toSave = new ParseObject("Messages");
      toSave.put("counter", 1);
      toSave.put("groupId", chat.getGroupId());
      toSave.put("lastMessage", chat.getLastMessage());
      toSave.put("lastUserId", chat.getLastUserId());
      toSave.put("otherUserId", otherUserId);
      toSave.put("otherUserName", otherUserName);
      toSave.put("userId", userId);
      toSave.put("updatedAction", Calendar.getInstance().getTime());


      try {
        toSave.save();
        if (!subscriber.isUnsubscribed()) {
          subscriber.onNext(null);
          subscriber.onCompleted();
        }
      } catch (Exception e) {
        subscriber.onError(e);
      }
    });

    return toReturn.observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.newThread());
  }

  @Override
  public Observable<Void> updateChat(Chat chat, String userId) {
    Observable<Void> toReturn = Observable.create(subscriber -> {
      ParseQuery query = new ParseQuery("Messages");
      query.whereEqualTo("groupId", chat.getGroupId());
      query.whereEqualTo("userId", userId);
      ParseObject object = null;
      try {
        object = query.getFirst();
      } catch (Exception e) {
        subscriber.onError(e);
      }

      if (!userId.equals(User.getUserId())) {
        object.put("counter", object.getInt("counter") + 1);
      }

      object.put("lastMessage", chat.getLastMessage());
      object.put("lastUserId", chat.getLastUserId());
      object.put("updatedAction", Calendar.getInstance().getTime());

      try {
        object.save();
        if (!subscriber.isUnsubscribed()) {
          subscriber.onNext(null);
          subscriber.onCompleted();
        }
      } catch (Exception e) {
        subscriber.onError(e);
      }
    });

    return toReturn.observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.newThread());
  }

  @Override
  public Observable<Void> clearMessagesCounter(String groupId, String userId) {
    Observable<Void> toReturn = Observable.create(subscriber -> {
      ParseQuery query = new ParseQuery("Messages");
      query.whereEqualTo("groupId", groupId);
      query.whereEqualTo("userId", userId);
      ParseObject object = null;
      try {
        object = query.getFirst();
      } catch (Exception e) {
        subscriber.onError(e);
      }

      object.put("counter", 0);

      try {
        object.save();
        if (!subscriber.isUnsubscribed()) {
          subscriber.onNext(null);
          subscriber.onCompleted();
        }
      } catch (Exception e) {
        subscriber.onError(e);
      }
    });

    return toReturn.observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.newThread());
  }

  @Override
  public Observable<Void> sendPush(String userId, String userName, String groupId, String text) {
    Observable<Void> toReturn = Observable.create(subscriber -> {

      ParseQuery pushQuery = ParseInstallation.getQuery();
      pushQuery.whereEqualTo("user", userId);

      ParsePush push = new ParsePush();
      push.setQuery(pushQuery);

      try {
        JSONObject object = new JSONObject();
        object.put("badge", "Increment");
        object.put("alert", userName + "\n" + text);
        object.put("groupId", groupId);
        push.setData(object);
        push.send();

        if (!subscriber.isUnsubscribed()) {
          subscriber.onNext(null);
          subscriber.onCompleted();
        }
      } catch (Exception e) {
        subscriber.onError(e);
      }
    });

    return toReturn.observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.newThread());
  }

  @Override
  public Observable<List<Chat>> getChats() {
    Observable toReturn = Observable.create(subscriber -> {
      ParseQuery<ParseObject> query = ParseQuery.getQuery("Messages");
      query.whereEqualTo("userId", User.getUserId());
      try {
        List<Chat> messages = new ArrayList<>();
        for (ParseObject object : query.find()) {
          messages.add(Chat.ParseToMessages(object));
        }

        if (!subscriber.isUnsubscribed()) {
          subscriber.onNext(messages);
          subscriber.onCompleted();
        }

      } catch (Exception e) {
        subscriber.onError(e);
      }

    });
    return toReturn.observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.newThread());
  }

  @Override
  public Observable<Chat> getChat(String groupId) {
    Observable toReturn = Observable.create(subscriber -> {
      ParseQuery<ParseObject> query = ParseQuery.getQuery("Messages");
      query.whereEqualTo("userId", User.getUserId());
      query.whereEqualTo("groupId", groupId);
      try {
        ParseObject object = query.getFirst();
        if (object == null) {
          if (!subscriber.isUnsubscribed()) {
            subscriber.onNext(null);
            subscriber.onCompleted();
          }
        } else {
          if (!subscriber.isUnsubscribed()) {
            subscriber.onNext(Chat.ParseToMessages(object));
            subscriber.onCompleted();
          }
        }
      } catch (Exception e) {
        subscriber.onError(e);
      }
    });

    return toReturn.observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.newThread());
  }

  @Override
  public Observable<List<Message>> getMessage(String groupId) {
    Observable toReturn = Observable.create(subscriber -> {
      ParseQuery<ParseObject> query = ParseQuery.getQuery("Message");
      query.whereEqualTo("groupId", groupId);
      query.addAscendingOrder("createdAt");

      try {
        List<Message> messageList = new ArrayList<>();
        for (ParseObject object : query.find()) {
          messageList.add(Message.ParseToMessage(object));
        }

        if (!subscriber.isUnsubscribed()) {
          subscriber.onNext(messageList);
          subscriber.onCompleted();
        }
      } catch (Exception e) {
        subscriber.onError(e);
      }
    });
    return toReturn.observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.newThread());
  }

  @Override
  public Observable<Void> saveNewRide(Ride ride) {
    Observable toReturn = Observable.create(subscriber -> {
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
          newRide.saveInBackground(e -> {
            if (e != null) {
              subscriber.onError(e);
            } else {
              if (!subscriber.isUnsubscribed()) {
                subscriber.onNext(null);
                subscriber.onCompleted();
              }
            }
          });
        }
    );
    return toReturn.observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.newThread());
  }

  @Override
  public Observable<List<Ride>> getRides(Date date, boolean myRides) {
    Observable toReturn = Observable.create(subscriber -> {
      ParseQuery<ParseObject> query = ParseQuery.getQuery("Ride");
      Date currentDate = Calendar.getInstance().getTime();
      query.whereGreaterThanOrEqualTo("dateTime", currentDate);
      if (currentDate.compareTo(date) > 0) {
        Date newDate = new Date(currentDate.getTime() + ONE_DAY_BEHIND);

        if (newDate.compareTo(date) < 0) {
          query.whereGreaterThanOrEqualTo("dateTime", new Date(currentDate.getTime() + ONE_DAY_BEHIND));
          query.whereLessThanOrEqualTo("dateTime", new Date(currentDate.getTime() + ONE_DAY_AHEAD));
        }
      }
      if (myRides) {
        query.whereEqualTo("userId", User.getUserId());
      } else {
        query.whereNotEqualTo("userId", User.getUserId());
      }

      try {
        List<Ride> rides = new ArrayList<>();

        for (ParseObject object : query.find()) {
          rides.add(Ride.parseToRide(object));
        }
        if (!subscriber.isUnsubscribed()) {
          subscriber.onNext(rides);
          subscriber.onCompleted();
        }
      } catch (Exception e) {
        subscriber.onError(e);
      }
    });

    return toReturn.observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.newThread());
  }
}
