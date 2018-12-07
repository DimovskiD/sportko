package com.dimovski.sportko.rest;

import android.util.TypedValue;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.*;

public interface ApiInterface {

    @POST("/v1/projects/sportko-837c1/messages:send")
    @Headers({"Content-Type: application/json"})
    Call<SendMessageResponse> sendMessageToTopic(@Header("Authorization") String token, @Body RequestBody json);
}
