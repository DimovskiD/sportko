package com.dimovski.sportko.utils;

import android.support.annotation.NonNull;
import com.dimovski.sportko.db.model.Event;
import com.dimovski.sportko.rest.ApiInterface;
import com.dimovski.sportko.rest.Client;
import com.dimovski.sportko.rest.SendMessageResponse;
import org.json.JSONException;
import org.json.JSONObject;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.concurrent.ExecutionException;

public class FirebaseUtils {

    static ApiInterface client;

    public static void sendFCM(Event event) {
        client = Client.getRetrofit().create(ApiInterface.class);
        SendMessageToTopic getToken = new SendMessageToTopic();
        try {
            String token= "Bearer " + getToken.execute().get();
            JSONObject message = new JSONObject();
            message.put("condition", "\'" +event.getId() +"\'"+ " in topics");
            JSONObject notification = new JSONObject();
            notification.put("body","An event you are attending has been edited. Take a look!");
            notification.put("title", "Event Edited");
            message.put("notification",notification);
            JSONObject body = new JSONObject();
            body.put("message",message);
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
