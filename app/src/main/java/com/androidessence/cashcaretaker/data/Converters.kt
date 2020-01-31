package com.androidessence.cashcaretaker.data

import androidx.room.TypeConverter
import java.util.Date

/**
 * Database converters that will convert longs to dates and vise versa when inserting/pulling from the database.
 */
class Converters {
    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return if (value == null) null else Date(value)
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time
    }
}
