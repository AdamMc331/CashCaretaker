package com.androidessence.cashcaretaker.database

import androidx.room.TypeConverter
import java.util.Date

/**
 * Database converters that will convert longs to dates and vise versa when inserting/pulling from the database.
 */
internal class Converters {
    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return if (value == null) null else Date(value)
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time
    }
}