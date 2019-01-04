package com.dimovski.sportko.utils;

/**
 * String utility class*/
public class StringUtils {

    /**Checks if a string is empty or not
     * @param x - string to be checked
     * @return true if the string is empty (without whitespaces), false otherwise*/
    public static boolean isEmpty(String x) {
        return x.trim().length() == 0;
    }
}
