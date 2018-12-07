package com.dimovski.sportko.utils;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import com.dimovski.sportko.BaseApp;
import com.dimovski.sportko.R;

import com.dimovski.sportko.ui.ListActivity;
import com.google.firebase.messaging.RemoteMessage;


public class NotificationUtils {

    public static void sendNotification(RemoteMessage.Notification notification, String title, String desc){

        Context context =BaseApp.getContext();
            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            String channelDesc = "";
            String channelName = "";
            if (title.equals(context.getString(R.string.event_edited))) {
                channelName = "EDITED_EVENT";
                channelDesc = "Channel for receiving notifications about edited events";
            }
            else if (title.equals(context.getString(R.string.event_deleted))) {
                channelName = "DELETED_EVENT";
                channelDesc = "Channel for receiving notifications about deleted events";
            }

            if (Build.VERSION.SDK_INT >= 26) {
                NotificationChannel notificationChannel = new NotificationChannel(channelName, channelName,
                        NotificationManager.IMPORTANCE_HIGH);
                notificationChannel.setDescription(channelDesc);
                notificationManager.createNotificationChannel(notificationChannel);
            }

            Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context)
                    .setAutoCancel(true)
                    .setSound(defaultSoundUri)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setStyle(new NotificationCompat.BigTextStyle()
                            .bigText(desc))
                    .setPriority(android.app.Notification.PRIORITY_MAX)
                    .setDefaults(android.app.Notification.DEFAULT_ALL)
                    .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher));

            Intent intent = new Intent(context,ListActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_ONE_SHOT);

            notificationBuilder
                    .setContentTitle(title)
                    .setContentText(desc)
                    .setContentIntent(pendingIntent);

            assert notificationManager != null;
            notificationManager.notify(0, notificationBuilder.build());
        }
    }

