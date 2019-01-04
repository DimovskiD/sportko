package com.dimovski.sportko.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**Class for network utilities*/
public class NetworkUtils {

    /**Checks if the device is currently connected to Internet
     * @param context - context required to get @{@link ConnectivityManager}
     * @return true if there is an active network connection, false otherwise*/
    public static boolean checkInternetConnection(Context context) {
        if (context == null)
            return false;

        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnected();
    }
}
