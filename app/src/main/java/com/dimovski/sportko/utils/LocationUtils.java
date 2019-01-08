package com.dimovski.sportko.utils;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import java.io.IOException;
import java.util.List;
import java.util.Locale;

/**
 * Class for location utilities*/
public class LocationUtils {

    /**
     * Returns the name of the city for a given location, provided in latitude and longitude
     * @param context - activity/application context required by @{@link Geocoder}
     * @param lat - latitude in double format
     * @param lon - longitude in double format
     * @return the name of the city, if a city is found for given coordinates, empty string otherwise*/
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
