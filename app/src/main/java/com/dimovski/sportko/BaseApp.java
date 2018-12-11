package com.dimovski.sportko;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;

public class BaseApp extends Application {
    private static Context context;

    public void onCreate(){
        super.onCreate();
        BaseApp.context = getApplicationContext();
        initDefaultChannelForFCM();
    }

    private void initDefaultChannelForFCM() {
        if (Build.VERSION.SDK_INT >= 26) {
            NotificationChannel notificationChannel = new NotificationChannel("FIREBASE_FCM", "Firebase FCM channel",
                    NotificationManager.IMPORTANCE_HIGH);
            notificationChannel.setDescription("Channel for Firecloud Messaging notifications about events");
            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            if (notificationManager!=null)
                notificationManager.createNotificationChannel(notificationChannel);
        }
    }

    public static Context getContext() {
        return BaseApp.context;
    }
}