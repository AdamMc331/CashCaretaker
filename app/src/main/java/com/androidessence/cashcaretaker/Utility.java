package com.androidessence.cashcaretaker;

import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.text.NumberFormat;

/**
 * Utility functions used for formatting dates and currencies.
 *
 * Created by adammcneilly on 10/30/15.
 */
public class Utility {
    // Formats
    private static final String DB_DATE_FORMAT = "yyyy-MM-dd";
    private static final String UI_DATE_FORMAT = "MMMM dd, yyyy";
    private static final NumberFormat currencyFormat = NumberFormat.getCurrencyInstance();

    /**
     * Converts a LocalDate to a string to be saved in the database.
     */
    public static String getDBDateString(LocalDate d){
        // Do not allow dates with negative years
        if(d.getYear() < 0){
            throw new UnsupportedOperationException("LocalDate has negative year value.");
        } else {
            return DateTimeFormat.forPattern(DB_DATE_FORMAT).print(d);
        }
    }

    /**
     * Returns a string representation of a date for storage in the database.
     */
    public static String getUIDateString(LocalDate d){
        // Do not allow dates with negative years
        if(d.getYear() < 0){
            throw new UnsupportedOperationException("LocalDate has negative year value.");
        } else {
            DateTimeFormatter dtf = DateTimeFormat.forPattern(UI_DATE_FORMAT);
            return dtf.print(d);
        }
    }

    /**
     * Converts a string from the database to a date string to be displayed on the UI.
     */
    public static String getUIDateStringFromDB(String dbString){
        return DateTimeFormat.forPattern(UI_DATE_FORMAT).print(getDateFromDb(dbString));
    }

    /**
     * Converts a string from the database to a LocalDate object.
     */
    public static LocalDate getDateFromDb(String dbString){
        // Do not allow dates with negative years
        LocalDate retDate = DateTimeFormat.forPattern(DB_DATE_FORMAT).parseLocalDate(dbString);
        if(retDate.getYear() < 0){
            throw new UnsupportedOperationException("LocalDate has negative year value.");
        } else{
            return retDate;
        }
    }

    /**DoaTons
     * Formats a double value as a currency string to be displayed to the user.
     */
    public static String getCurrencyString(double currency){
        return currencyFormat.format(currency);
    }
}
