package com.telivercustomer;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

import com.bumptech.glide.Glide;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.GsonBuilder;
import com.teliver.sdk.core.Teliver;
import com.teliver.sdk.models.NotificationData;

import java.util.Map;

public class FirebaseMessaging extends FirebaseMessagingService {

    private Application application;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        try {
            application = (Application) getApplicationContext();
            if (Teliver.isTeliverPush(remoteMessage)) {
                Map<String, String> pushData = remoteMessage.getData();
                final NotificationData data = new GsonBuilder().create().fromJson(pushData.get("description"), NotificationData.class);
                Log.d("TELIVER::", "PUSH MESSAGE == " + data.getTrackingID() + "message == " + data.getMessage() + "command == "
                        + data.getCommand());
                Intent intent = new Intent(this, MainActivity.class);
                intent.putExtra("msg", data.getMessage());
                intent.putExtra(Application.TRACKING_ID, data.getTrackingID());
                intent.setAction("tripId");
                LocalBroadcastManager.getInstance(this).sendBroadcast(intent);

                sendPush(data);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void sendPush(NotificationData data) {
        NotificationCompat.Builder notification = new NotificationCompat.Builder(this);
        notification.setContentTitle("Teliver");
        notification.setContentText(data.getMessage());
        notification.setSmallIcon(R.drawable.ic_scooter);
        notification.setLargeIcon(getBitmapIcon(this));

        notification.setStyle(new NotificationCompat.BigTextStyle().bigText(data.getMessage()).setBigContentTitle("Teliver"));
        Intent intent = new Intent();
        intent.setAction("teliverMap");
        intent.putExtra("msg", data.getMessage());
        intent.putExtra(Application.TRACKING_ID, data.getTrackingID());

        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 1, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        notification.setContentIntent(pendingIntent);
        notification.setAutoCancel(true);
        notification.setPriority(Notification.PRIORITY_MAX);
        notification.setDefaults(Notification.DEFAULT_ALL);
        notification.addAction(R.drawable.ic_run, "Start Tracking", pendingIntent);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(1, notification.build());
    }


    public static Bitmap getBitmapIcon(Context context) {
        try {
            return Glide.with(context).load(R.drawable.ic_notification_icon).
                    asBitmap().into(144, 144).get();
        } catch (Exception e) {
            return null;
        }
    }
}


