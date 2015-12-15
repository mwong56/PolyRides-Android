package com.mwong56.polyrides.receivers;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;

import com.mwong56.polyrides.R;
import com.mwong56.polyrides.activities.MainActivity;
import com.mwong56.polyrides.activities.MessageActivity;
import com.mwong56.polyrides.application.PolyRidesApp;

import org.json.JSONObject;

import timber.log.Timber;

/**
 * Created by micha on 11/4/2015.
 */
public class MessageReceiver extends BroadcastReceiver {

  private static final String TAG = MessageReceiver.TAG;

  @Override
  public void onReceive(Context context, Intent intent) {

    try {
      Bundle bundle = intent.getExtras();
      if (bundle == null) {
        return;
      }

      String data = bundle.getString("com.parse.Data");
      if (data == null || data.length() == 0) {
        return;
      }

      JSONObject json = new JSONObject(data);

      String alert = json.getString("alert");
      String groupId = null;
      if (json.has("groupId")) {
        json.getString("groupId");

        String applicationGroupId = ((PolyRidesApp) context.getApplicationContext()).getMessageGroupIdInForeground();
        if (applicationGroupId != null) {
          if (applicationGroupId.equals(groupId)) {
            return;
          }
        }
      }


      String[] alertArray = alert.split("\n");
      String userName = alertArray[0];
      String message = alertArray[1];

      Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

      NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
          .setLargeIcon(
              BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher))
          .setSmallIcon(R.mipmap.ic_launcher).setContentTitle(userName)
          .setContentText(message).setAutoCancel(true).setSound(soundUri)
          .setVibrate(new long[]{0, 100, 200, 300});

      Intent i;
      if (groupId == null) {
        i = new Intent(context, MainActivity.class);
      } else {
        i = new Intent(context, MessageActivity.class);
        i.putExtra("groupId", groupId);
      }
      PendingIntent pIntent = PendingIntent.getActivity(context, 0, i,
          PendingIntent.FLAG_CANCEL_CURRENT);
      builder.setContentIntent(pIntent);

      NotificationManager mNotificationManager = (NotificationManager) context
          .getSystemService(Context.NOTIFICATION_SERVICE);

      mNotificationManager.notify(0, builder.build());

    } catch (Exception e) {
      Timber.e(e, "Data: %s", intent.getDataString());
    }
  }
}
