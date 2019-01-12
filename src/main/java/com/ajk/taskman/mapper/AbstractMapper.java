package com.ajk.taskman.mapper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public abstract class AbstractMapper {

    private static String FORMAT = "yyyy-MM-dd HH:mm:ss";

    /**
     * Format a String to a Date
     */
    public Date asDate(String date) {
        if (date == null) {
            return null;
        }
        try {
            return new SimpleDateFormat(FORMAT).parse(date);
        } catch (ParseException pe) {
            return null;
        }
    }

    /**
     * Format a Date to a String
     */
    public String asString(Date date) {
        if (date == null) {
            return null;
        }
        return new SimpleDateFormat(FORMAT).format(date);
    }
}
