package com.dimovski.sportko.utils;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class LocationUtils {

    public static String getCityForLocation(Context context, double lat, double lon) {
        Geocoder gcd = new Geocoder(context, Locale.getDefault());

        String city = "";
        List<Address> list = null;
        try {
            list = gcd.getFromLocation(lat,lon,1);
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        if (list!=null && list.size() > 0) {
            city = list.get(0).getLocality();
        }
        return city;
    }
}
