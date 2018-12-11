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
    public static final String DELETED = "DELETED";
    public static final String EDITED = "EDITED";
    public static final String EVENT = "event";
    public static final int SMS_PERMISSIONS_CODE = 345;
    public static final int SMS_PERMISSIONS_CODE_LISTENER = 222;
    public static final String SELECTED_ITEMS = "SELECTED_EVENTS";
    public static final String SEARCH_QUERY = "SEARCH_QUERY";
    public static final String NEW_ATTENDEE = "NEW_ATTENDEE";
    public static final String ATENDEE_CANCELLED = "ATTENDEE_CANCELLED";
    public static String EVENT_ID = "EVENT_ID";

    public static String[] locPermission = {Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION};

}
