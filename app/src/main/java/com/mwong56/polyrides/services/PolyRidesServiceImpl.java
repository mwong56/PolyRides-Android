package com.mwong56.polyrides.services;

import android.app.Activity;

import com.mwong56.polyrides.models.Message;
import com.mwong56.polyrides.models.Messages;
import com.mwong56.polyrides.models.Ride;
import com.mwong56.polyrides.models.User;
import com.parse.ParseFacebookUtils;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.bolts.TaskObservable;
import rx.exceptions.OnErrorThrowable;
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
  public Observable<ParseUser> facebookLogin(final Activity activity, final Collection<String> permissions) {
    return Observable.defer(() -> TaskObservable.just(ParseFacebookUtils.logInWithReadPermissionsInBackground(activity, permissions)))
        .observeOn(AndroidSchedulers.mainThread())
        .subscribeOn(Schedulers.newThread())
        .map(parseUser -> {
          if (parseUser == null) {
            throw OnErrorThrowable.from(new Exception("Parse user is null"));
          } else {
            return parseUser;
          }
        });
  }

  @Override
  public void saveUserId(String userId) {
    ParseInstallation installation = ParseInstallation.getCurrentInstallation();
    installation.put("userId", userId);
    installation.saveEventually();
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

  @Override
  public Observable<List<Messages>> getMessages() {
    Observable toReturn = Observable.create(subscriber -> {
      ParseQuery<ParseObject> query = ParseQuery.getQuery("Messages");
      query.whereEqualTo("userId", User.getUserId());
      try {
        List<Messages> messages = new ArrayList<>();
        for (ParseObject object : query.find()) {
          messages.add(Messages.ParseToMessages(object));
        }

        if (!subscriber.isUnsubscribed()) {
          subscriber.onNext(messages);
          subscriber.onCompleted();
        }

      } catch (Exception e) {
        subscriber.onError(e);
      }

    });
    return toReturn;
  }

  @Override
  public Observable<List<Message>> getMessage(String groupId) {
    Observable toReturn = Observable.create(subscriber -> {
      ParseQuery<ParseObject> query = ParseQuery.getQuery("Message");
      query.whereEqualTo("groupId", groupId);

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


