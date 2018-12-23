package com.dimovski.sportko.utils;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import com.dimovski.sportko.BaseApp;
import com.dimovski.sportko.R;
import com.dimovski.sportko.data.Constants;
import com.dimovski.sportko.db.model.Event;
import com.dimovski.sportko.internal.DynamicLinkListener;
import com.dimovski.sportko.rest.ApiInterface;
import com.dimovski.sportko.rest.Client;
import com.dimovski.sportko.rest.SendMessageResponse;
import com.dimovski.sportko.service.GetFirebaseAccessToken;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.dynamiclinks.DynamicLink;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.ShortDynamicLink;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.Gson;
import org.json.JSONException;
import org.json.JSONObject;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.concurrent.ExecutionException;

public class FirebaseUtils {

    static ApiInterface client;

    public static void sendFCM(Event event, String action) {

        Context context = BaseApp.getContext();
        client = Client.getRetrofit().create(ApiInterface.class);
        GetFirebaseAccessToken getToken = new GetFirebaseAccessToken();
        try {
            String token= "Bearer " + getToken.execute().get();
            JSONObject message = new JSONObject();
            JSONObject notification = new JSONObject();
            switch (action) {
                case Constants.EDITED:
                    message.put("condition", "\'" +event.getId() +"\'"+ " in topics");
                    notification.put("body", String.format(context.getString(R.string.event_edited_desc), event.getTitle()));
                    notification.put("title", context.getString(R.string.event_edited));
                    break;
                case Constants.DELETED:
                    message.put("condition", "\'" +event.getId() +"\'"+ " in topics");
                    notification.put("body", String.format(context.getString(R.string.event_cancelled), event.getTitle()));
                    notification.put("title", context.getString(R.string.event_deleted));
                    break;
                case Constants.NEW_ATTENDEE:
                    message.put("condition", "\'" +event.getId()+"-creator" +"\'"+ " in topics");
                    notification.put("body", String.format(context.getString(R.string.new_attendee), event.getTitle()));
                    notification.put("title", context.getString(R.string.new_event_attendee));
                    break;
                case Constants.ATTENDEE_CANCELLED:
                    message.put("condition", "\'" +event.getId()+"-creator" +"\'"+ " in topics");
                    notification.put("body", String.format(context.getString(R.string.attendee_cancelled), event.getTitle()));
                    notification.put("title", context.getString(R.string.atendee_cancelled_attendance));
                    break;

            }

            message.put("notification",notification);
            JSONObject body = new JSONObject();
            body.put("message",message);

            JSONObject android = new JSONObject();
            JSONObject notificationAndroid = new JSONObject();

            switch (action) {
                case Constants.EDITED:
                    notificationAndroid.put("body", String.format(context.getString(R.string.event_edited_desc), event.getTitle()));
                    notificationAndroid.put("title", context.getString(R.string.event_edited));
                    JSONObject data = new JSONObject();
                    String eventJson = new Gson().toJson(event);
                    data.put(Constants.EVENT,eventJson);
                    message.put("data",data);
                    break;
                case Constants.DELETED:
                    notificationAndroid.put("body", String.format(context.getString(R.string.new_attendee), event.getTitle()));
                    notificationAndroid.put("title", context.getString(R.string.event_deleted));
                    break;
                case Constants.NEW_ATTENDEE:
                    notificationAndroid.put("body", String.format(context.getString(R.string.new_attendee), event.getTitle()));
                    notificationAndroid.put("title", context.getString(R.string.new_event_attendee));
                    JSONObject dataEvent = new JSONObject();
                    String eventJson1 = new Gson().toJson(event);
                    dataEvent.put(Constants.EVENT,eventJson1);
                    message.put("data",dataEvent);
                    break;
                case Constants.ATTENDEE_CANCELLED:
                    notificationAndroid.put("body", String.format(context.getString(R.string.attendee_cancelled), event.getTitle()));
                    notificationAndroid.put("title", context.getString(R.string.atendee_cancelled_attendance));
                    JSONObject dataEventAttendeeCancelled = new JSONObject();
                    String eventJsonAttendeeCancelled = new Gson().toJson(event);
                    dataEventAttendeeCancelled.put(Constants.EVENT,eventJsonAttendeeCancelled);
                    message.put("data",dataEventAttendeeCancelled);
                    break;

            }

            notificationAndroid.put("sound","default");
            notificationAndroid.put("click_action","DETAIL_ACTIVITY");
            notificationAndroid.put("channel_id","FIREBASE_FCM");

            android.put("notification",notificationAndroid);

            message.put("android",android);

            okhttp3.RequestBody body1 = okhttp3.RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"),body.toString());

            Call<SendMessageResponse> call = client.sendMessageToTopic(token,body1);
            call.enqueue(new Callback<SendMessageResponse>() {
                @Override
                public void onResponse(@NonNull Call<SendMessageResponse> call, @NonNull Response<SendMessageResponse> response) {

                }

                @Override
                public void onFailure(@NonNull Call<SendMessageResponse> call, Throwable t) {

                }
            });

        } catch (JSONException | InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }

    public static void subscribeToTopic(String topic,Boolean shouldSubscribe) {
        if (shouldSubscribe)
            FirebaseMessaging.getInstance().subscribeToTopic(topic);
        else  FirebaseMessaging.getInstance().unsubscribeFromTopic(topic);
    }

    public static void createDynamicLink(String eventId, final DynamicLinkListener listner) {
        FirebaseDynamicLinks.getInstance().createDynamicLink()
                .setLink(Uri.parse("https://sportko573477588.wordpress.com/event?id="+eventId))
                .setDomainUriPrefix("https://sportko.page.link")
                // Open links with this app on Android
                .setAndroidParameters(new DynamicLink.AndroidParameters.Builder("com.dimovski.sportko").build())
                .buildShortDynamicLink(ShortDynamicLink.Suffix.SHORT).addOnSuccessListener(new OnSuccessListener<ShortDynamicLink>() {
                    @Override
                    public void onSuccess(ShortDynamicLink shortDynamicLink) {
                        listner.shortDynamicLinkCreated(shortDynamicLink);
                    }
                });


    }


}
