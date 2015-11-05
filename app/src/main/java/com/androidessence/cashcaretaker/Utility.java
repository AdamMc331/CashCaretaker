package com.androidessence.cashcaretaker;

import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.text.NumberFormat;

/**
 * Created by adammcneilly on 10/30/15.
 */
public class Utility {
    private static final String DB_DATE_FORMAT = "yyyy-MM-dd";
    private static final String UI_DATE_FORMAT = "MMMM dd, yyyy";
    private static NumberFormat currencyFormat = NumberFormat.getCurrencyInstance();

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

    public static String getUIDateStringFromDB(String dbString){
        return DateTimeFormat.forPattern(UI_DATE_FORMAT).print(getDateFromDb(dbString));
    }

    public static LocalDate getDateFromDb(String dbString){
        // Do not allow dates with negative years
        LocalDate retDate = DateTimeFormat.forPattern(DB_DATE_FORMAT).parseLocalDate(dbString);
        if(retDate.getYear() < 0){
            throw new UnsupportedOperationException("LocalDate has negative year value.");
        } else{
            return retDate;
        }
    }

    public static String getCurrencyString(double currency){
        return currencyFormat.format(currency);
    }
}
