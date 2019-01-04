package com.dimovski.sportko;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;


/**Base application class - provides application context where needed*/
public class BaseApp extends Application {

    //singleton instance of the base app class
    private static BaseApp app;

    public void onCreate(){
        super.onCreate();
        app = this;
        initDefaultChannelForFCM();
    }

    /**
     * Initializes the default notification channels for Firebase Cloud messaging notifications
     * Required for Android O and above in order to show notifications*/
    private void initDefaultChannelForFCM() {
        if (Build.VERSION.SDK_INT >= 26) {
            NotificationChannel notificationChannel = new NotificationChannel("FIREBASE_FCM", "Firebase FCM channel",
                    NotificationManager.IMPORTANCE_HIGH);
            notificationChannel.setDescription("Channel for Firecloud Messaging notifications about events");
            NotificationManager notificationManager = (NotificationManager) app.getSystemService(Context.NOTIFICATION_SERVICE);
            if (notificationManager!=null)
                notificationManager.createNotificationChannel(notificationChannel);
        }
    }

    /**
     * Provides the application context
     * Safe to use app context almost everywhere, as it exists while the application is alive.*/
    public static Context getContext() {
        return app.getApplicationContext();
    }
}