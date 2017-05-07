package com.androidessence.cashcaretaker

import android.text.InputFilter
import android.text.Spanned

import java.util.regex.Matcher
import java.util.regex.Pattern

/**
 * Input filter that only allows two decimal digits in a number.

 * Created by adammcneilly on 11/1/15.
 */
class DecimalDigitsInputFilter : InputFilter {
    private val pattern: Pattern = Pattern.compile("[0-9]*+((\\.[0-9]{0,2})?)||(\\.)?")

    /**
     * Filters the text based on the Regex pattern.
     */
    override fun filter(source: CharSequence, start: Int, end: Int, dest: Spanned, dstart: Int, dend: Int): CharSequence? {
        //Matcher matcher = pattern.matcher(dest.subSequence(0, dstart) +
        // source.subSequence(start, end) + dest.subSequence(dend, dest.length()));
        val matcher = pattern.matcher(dest.subSequence(0, dstart).toString() + source.subSequence(start, end).toString() + dest.subSequence(dend, dest.length))
        if (!matcher.matches()) {
            return ""
        } else {
            return null
        }
    }
}
