package com.rabbitt.smarttech.Firebase;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.rabbitt.smarttech.PrefsManager.Config;

import org.json.JSONException;
import org.json.JSONObject;

public class FirebaseMessengingService extends FirebaseMessagingService {

    String TAG = "rkd";
    Bitmap bitmap;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

//        if (remoteMessage.getData().size() > 0) {
//            Log.i("remote", "Data Payload: " + remoteMessage.getData().toString());
//            try
//            {
//                JSONObject json = new JSONObject(remoteMessage.getData().toString());
//                sendPushNotification(json);
//            } catch (Exception e) {
//                Log.e("remote", "Exception: " + e.getMessage());
//            }
//        }
    }

//    private void sendPushNotification(JSONObject json) {
//        //optionally we can display the json into log
//        Log.i("remote", "Notification JSON " + json.toString());
//
//        try {
//            //getting the json data
//            JSONObject data = json.getJSONObject("data");
//
//            //parsing json data
//            String book_id = data.getString("status");
////
////            Log.i("remote", "title..." + book_id);
//
//            Intent i = new Intent(this, HomeScreen.class);
//            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//
//            NotificationManager notificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
//            String NOTIFICATION_CHANNEL_ID = "my_channel_id_01";
//
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//                @SuppressLint("WrongConstant") NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, "Sir Post!", NotificationManager.IMPORTANCE_MAX);
//
//                // Configure the notification channel.
//                notificationChannel.setDescription("New post arrived...");
//                notificationChannel.enableLights(true);
//                notificationChannel.setLightColor(Color.RED);
//                notificationChannel.setVibrationPattern(new long[]{0, 1000, 500, 1000});
//                notificationChannel.enableVibration(true);
//                notificationManager.createNotificationChannel(notificationChannel);
//            }
//
//            // assuming your main activity
//            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID);
//            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, i, 0);
//            notificationBuilder.setAutoCancel(true)
//                    .setWhen(System.currentTimeMillis())
//                    .setSmallIcon(R.drawable.logo)
//                    .setPriority(Notification.PRIORITY_HIGH)
//                    .setContentTitle("Etekchno")
//                    .setContentText("New post arrived..."+book_id)
//                    .setAutoCancel(true)
//                    .setContentIntent(pendingIntent);
//
//            notificationManager.notify(/*notification id*/1, notificationBuilder.build());
//
//
//        } catch (JSONException e) {
//            Log.e("remote", "Json Exception: " + e.getMessage());
//        } catch (Exception e) {
//            Log.e("remote", "Exception: " + e.getMessage());
//        }
//    }

    @Override
    public void onNewToken(String token) {
        Log.i(TAG, "Refreshed token: " + token);

        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.

        sendRegistrationToServer(token);
    }

    private void sendRegistrationToServer(String token) {
        SharedPreferences pref = getApplicationContext().getSharedPreferences(Config.TOKEN_PREF, MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("token", token);
        editor.apply();
        editor.commit();
    }
}