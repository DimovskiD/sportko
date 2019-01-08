package com.dimovski.sportko.utils;

import android.content.Context;

import java.text.DateFormat;
import java.text.Format;
import java.util.Date;

/**
 * Utility class for DateTime operations*/
public class DateTimeUtils {

    /**@param scheduled - the date to be formatted according to default locale
     * @return Formatted date in locale (without time) if @scheduled is not null,
     *          empty string if @scheduled is null
     */

    public static String formatDate(Date scheduled, Context context) {
        Format dateFormat = android.text.format.DateFormat.getDateFormat(context);
        if (scheduled!=null)
            return ((DateFormat) dateFormat).format(scheduled);
        else return "";
    }

    /**@param scheduled - the date to be formatted according to default locale
     * @return Formatted time in locale (without date) if @scheduled is not null,
     * empty string if @scheduled is null*/
    public static String formatTime(Date scheduled, Context context) {
        Format timeFormat = android.text.format.DateFormat.getTimeFormat(context);
        if (scheduled!=null)
            return ((DateFormat) timeFormat).format(scheduled);
        else return "";
    }
}
