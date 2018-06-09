package com.androidessence.cashcaretaker.data;

import androidx.room.TypeConverter;

import java.util.Date;

/**
 * Database converters that will convert longs to dates and vise versa when inserting/pulling from the database.
 */
@SuppressWarnings("WeakerAccess")
public class Converters {
    @TypeConverter
    public static Date fromTimestamp(Long value) {
        return value == null ? null : new Date(value);
    }

    @TypeConverter
    public static Long dateToTimestamp(Date date) {
        if (date == null) {
            return null;
        } else {
            return date.getTime();
        }
    }
}
