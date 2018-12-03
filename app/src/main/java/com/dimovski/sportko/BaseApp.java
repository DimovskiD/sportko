package com.dimovski.sportko;

import android.app.Application;
import android.content.Context;

public class BaseApp extends Application {
    private static Context context;

    public void onCreate(){
        super.onCreate();
        BaseApp.context = getApplicationContext();
    }

    public static Context getContext() {
        return BaseApp.context;
    }
}