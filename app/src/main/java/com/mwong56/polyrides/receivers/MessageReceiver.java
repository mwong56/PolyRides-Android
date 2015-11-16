package com.mwong56.polyrides.receivers;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.mwong56.polyrides.R;
import com.mwong56.polyrides.activities.MainActivity;
import com.mwong56.polyrides.activities.MessageActivity;
import com.mwong56.polyrides.application.PolyRidesApp;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by micha on 11/4/2015.
 */
public class MessageReceiver extends BroadcastReceiver {

  private static final String TAG = MessageReceiver.TAG;

  @Override
  public void onReceive(Context context, Intent intent) {

    try {
      JSONObject json = new JSONObject(intent.getExtras().getString("com.parse.Data"));

      String alert = json.getString("alert");
      String groupId = json.isNull("groupId") ? null : json.getString("groupId");

      String applicationGroupId = ((PolyRidesApp) context.getApplicationContext()).getMessageGroupIdInForeground();
      if (applicationGroupId != null && groupId != null) {
        if (applicationGroupId.equals(groupId)) {
          return;
        }
      }

      String[] alertArray = alert.split("\n");
      String userName = alertArray[0];
      String message = alertArray[1];

      Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

      NotificationCompat .Builder builder = new NotificationCompat.Builder(context)
          .setLargeIcon(
              BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher))
          .setSmallIcon(R.mipmap.ic_launcher).setContentTitle(userName)
          .setContentText(message).setAutoCancel(true).setSound(soundUri)
          .setVibrate(new long[]{0, 100, 200, 300});

      Intent i;
      if (groupId != null) {
        i = new Intent(context, MessageActivity.class);
        i.putExtra("groupId", groupId);
      } else {
        i = new Intent(context, MainActivity.class);
      }

      PendingIntent pIntent = PendingIntent.getActivity(context, 0, i,
          PendingIntent.FLAG_CANCEL_CURRENT);
      builder.setContentIntent(pIntent);

      NotificationManager mNotificationManager = (NotificationManager) context
          .getSystemService(Context.NOTIFICATION_SERVICE);

      mNotificationManager.notify(0, builder.build());

    } catch (JSONException e) {
      Log.d(TAG, "JSONException: " + e.getMessage());
    }
  }


}
