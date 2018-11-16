package com.dimovski.sportko.utils;

import android.content.Context;

import java.text.DateFormat;
import java.text.Format;
import java.util.Date;

public class DateTimeUtils {
    public static String formatDate(Date scheduled, Context context) {
        Format dateFormat = android.text.format.DateFormat.getDateFormat(context);
        return ((DateFormat) dateFormat).format(scheduled);
    }

    public static String formatTime(Date scheduled, Context context) {
        Format timeFormat = android.text.format.DateFormat.getTimeFormat(context);
        return ((DateFormat) timeFormat).format(scheduled);
    }
}
