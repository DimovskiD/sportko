package com.dimovski.sportko.rest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

/**Singleton Retrofit REST client*/
public class Client {

    private static Retrofit retrofit;
    private static final String URL = "https://fcm.googleapis.com";

    /**@return singleton instance of @{@link Retrofit}*/
    public static Retrofit getRetrofit() {
        if (retrofit == null) {
            Gson gson = new GsonBuilder().disableHtmlEscaping().create();

            retrofit = new Retrofit.Builder().
                    baseUrl(URL)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .build();
        }
        return retrofit;
    }

}
