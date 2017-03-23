package com.androidessence.utility


import java.text.NumberFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

/**
 * Utility functions used for formatting dates and currencies.

 * Created by adammcneilly on 10/30/15.
 */
object Utility {
    // Formats
    private val DB_DATE_FORMAT = "yyyy-MM-dd"
    private val dbDateFormatter = SimpleDateFormat(DB_DATE_FORMAT, Locale.getDefault())
    private val UI_DATE_FORMAT = "MMMM dd, yyyy"
    private val uiDateFormatter = SimpleDateFormat(UI_DATE_FORMAT, Locale.getDefault())

    private val calendar = Calendar.getInstance()

    /**
     * Converts a LocalDate to a string to be saved in the database.
     */
    fun getDBDateString(d: Date?): String {
        val year = d?.year() ?: -1

        if (year < 0) {
            throw UnsupportedOperationException("LocalDate has negative year value.")
        } else {
            return dbDateFormatter.format(d)
        }
    }

    /**
     * Returns a string representation of a date for storage in the database.
     */
    fun getUIDateString(d: Date?): String {
        val year = d?.year() ?: -1

        if (year < 0) {
            throw UnsupportedOperationException("LocalDate has negative year value.")
        } else {
            return uiDateFormatter.format(d)
        }
    }

    /**
     * Converts a string from the database to a date string to be displayed on the UI.
     */
    fun getUIDateStringFromDB(dbString: String?): String {
        return uiDateFormatter.format(getDateFromDb(dbString))
    }

    /**
     * Converts a string from the database to a LocalDate object.
     */
    fun getDateFromDb(dbString: String?): Date {
        try {
            val retDate = dbDateFormatter.parse(dbString)
            calendar.time = retDate

            if (calendar.get(Calendar.YEAR) < 0) {
                throw UnsupportedOperationException("Date has negative year value.")
            } else {
                return retDate
            }
        } catch (pe: ParseException) {
            pe.printStackTrace()
            return Date()
        }

    }
}
