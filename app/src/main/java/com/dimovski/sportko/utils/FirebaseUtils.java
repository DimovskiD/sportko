package com.dimovski.sportko.utils;

import android.content.Context;
import android.support.annotation.NonNull;
import com.dimovski.sportko.BaseApp;
import com.dimovski.sportko.R;
import com.dimovski.sportko.data.Constants;
import com.dimovski.sportko.db.model.Event;
import com.dimovski.sportko.rest.ApiInterface;
import com.dimovski.sportko.rest.Client;
import com.dimovski.sportko.rest.SendMessageResponse;
import com.dimovski.sportko.service.GetFirebaseAccessToken;
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
            message.put("condition", "\'" +event.getId() +"\'"+ " in topics");
            JSONObject notification = new JSONObject();
            if (action.equals(Constants.EDITED))   {
                notification.put("body", String.format(context.getString(R.string.event_edited_desc), event.getTitle()));
                notification.put("title", context.getString(R.string.event_edited));
            }

            else if (action.equals(Constants.DELETED)) {
                notification.put("body", String.format(context.getString(R.string.event_cancelled), event.getTitle()));
                notification.put("title", context.getString(R.string.event_deleted));
            }
            message.put("notification",notification);
            JSONObject body = new JSONObject();
            body.put("message",message);

            JSONObject android = new JSONObject();
            JSONObject notificationAndroid = new JSONObject();
            if (action.equals(Constants.EDITED))   {
                notificationAndroid.put("body", String.format(context.getString(R.string.event_edited_desc), event.getTitle()));
                notificationAndroid.put("title", context.getString(R.string.event_edited));
            }

            else if (action.equals(Constants.DELETED)) {
                notificationAndroid.put("body", String.format(context.getString(R.string.event_cancelled), event.getTitle()));
                notificationAndroid.put("title", context.getString(R.string.event_deleted));
            }
            notificationAndroid.put("sound","default");
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


}
