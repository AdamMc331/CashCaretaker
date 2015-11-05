package com.androidessence.cashcaretaker;

import android.text.InputFilter;
import android.text.Spanned;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by adammcneilly on 11/1/15.
 */
public class DecimalDigitsInputFilter implements InputFilter {
    private final Pattern mPattern;

    public DecimalDigitsInputFilter() {
        mPattern = Pattern.compile("[0-9]*+((\\.[0-9]{0,2})?)||(\\.)?");
    }

    /**
     * Filters the text based on the Regex pattern.
     */
    @Override
    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
        //Matcher matcher = mPattern.matcher(dest.subSequence(0, dstart) +
        // source.subSequence(start, end) + dest.subSequence(dend, dest.length()));
        Matcher matcher = mPattern.matcher(dest.subSequence(0, dstart).toString() + source.subSequence(start, end).toString() + dest.subSequence(dend, dest.length()));
        if (!matcher.matches()) {
            return "";
        } else {
            return null;
        }
    }
}
