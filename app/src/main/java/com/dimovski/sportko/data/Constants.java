package com.dimovski.sportko.data;

import android.Manifest;

public class Constants {

    public static final String SHARED_PREF = "SharedPrefs";

    public static final String EMAIL = "EMAIL_USER_LOGGED_IN";
    public static final String CREATE = "CREATE_EVENT";
    public static final int INVALID_ID = -1;
    public static final String EDIT_MODE = "EDIT_MODE";
    public static final int PLACE_AUTOCOMPLETE_REQUEST_CODE = 123;
    public static final int LOCATION_PERMISSION = 333;
    public static final String USER = "USER";
    public static final String LOCATION = "USE_LOCATION";
    public static String EVENT_ID = "EVENT_ID";

    public static String[] locPermission = {Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION};

}
