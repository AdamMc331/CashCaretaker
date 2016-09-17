package com.androidessence.utility;


import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Utility functions used for formatting dates and currencies.
 *
 * Created by adammcneilly on 10/30/15.
 */
public class Utility {
    // Formats
    private static final String DB_DATE_FORMAT = "yyyy-MM-dd";
    private static final SimpleDateFormat dbDateFormatter = new SimpleDateFormat(DB_DATE_FORMAT, Locale.getDefault());
    private static final String UI_DATE_FORMAT = "MMMM dd, yyyy";
    private static final SimpleDateFormat uiDateFormatter = new SimpleDateFormat(UI_DATE_FORMAT, Locale.getDefault());
    private static final NumberFormat currencyFormat = NumberFormat.getCurrencyInstance();

    private static final Calendar calendar = Calendar.getInstance();

    /**
     * Converts a LocalDate to a string to be saved in the database.
     */
    public static String getDBDateString(Date d){
        calendar.setTime(d);

        // Do not allow dates with negative years
        if(calendar.get(Calendar.YEAR) < 0){
            throw new UnsupportedOperationException("LocalDate has negative year value.");
        } else {
            return dbDateFormatter.format(d);
        }
    }

    /**
     * Returns a string representation of a date for storage in the database.
     */
    public static String getUIDateString(Date d){
        calendar.setTime(d);

        // Do not allow dates with negative years
        if(calendar.get(Calendar.YEAR) < 0){
            throw new UnsupportedOperationException("LocalDate has negative year value.");
        } else {
            return uiDateFormatter.format(d);
        }
    }

    /**
     * Converts a string from the database to a date string to be displayed on the UI.
     */
    public static String getUIDateStringFromDB(String dbString){
        return uiDateFormatter.format(getDateFromDb(dbString));
    }

    /**
     * Converts a string from the database to a LocalDate object.
     */
    public static Date getDateFromDb(String dbString){
        try {
            Date retDate = dbDateFormatter.parse(dbString);
            calendar.setTime(retDate);

            if(calendar.get(Calendar.YEAR) < 0) {
                throw new UnsupportedOperationException("Date has negative year value.");
            } else {
                return retDate;
            }
        } catch(ParseException pe) {
            pe.printStackTrace();
            return new Date();
        }
    }

    /**DoaTons
     * Formats a double value as a currency string to be displayed to the user.
     */
    public static String getCurrencyString(double currency){
        return currencyFormat.format(currency);
    }
}
