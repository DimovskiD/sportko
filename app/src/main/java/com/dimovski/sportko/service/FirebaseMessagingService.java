package com.dimovski.sportko.service;

import android.util.Log;
import com.dimovski.sportko.utils.NotificationUtils;
import com.google.firebase.messaging.RemoteMessage;

public class FirebaseMessagingService extends com.google.firebase.messaging.FirebaseMessagingService {

    private static String TAG = "FBMS";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        remoteMessage.getNotification();
        NotificationUtils.sendNotification(remoteMessage.getNotification(),remoteMessage.getNotification().getTitle(),remoteMessage.getNotification().getBody());
    }

}
