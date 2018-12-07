package com.dimovski.sportko.service;

import android.content.Context;
import android.os.AsyncTask;
import com.dimovski.sportko.BaseApp;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

public class GetFirebaseAccessToken extends AsyncTask<Void,Void,String> {


    private static String[] SCOPES = {"https://www.googleapis.com/auth/firebase.messaging"};

    private static String getMessagingToken(Context c) throws IOException {
        InputStream ins = c.getResources().openRawResource(
                c.getResources().getIdentifier("auth_token",
                        "raw", c.getPackageName()));
        GoogleCredential googleCredential = GoogleCredential
                .fromStream(ins)
                .createScoped(Arrays.asList(SCOPES));
        ins.close();
        googleCredential.refreshToken();
        return googleCredential.getAccessToken();
    }


    @Override
    protected String doInBackground(Void[] voids) {
        String token="";
        try {
             token = getMessagingToken(BaseApp.getContext());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return token;
    }
}
