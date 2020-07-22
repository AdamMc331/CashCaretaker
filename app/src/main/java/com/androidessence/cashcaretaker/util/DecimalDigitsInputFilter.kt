package com.androidessence.cashcaretaker.util

import android.text.InputFilter
import android.text.Spanned
import java.util.regex.Pattern

/**
 * Prevents an edit text from accepting more than two decimal digits.
 */
class DecimalDigitsInputFilter : InputFilter {
    private val pattern: Pattern = Pattern.compile("[0-9]*+((\\.[0-9]{0,2})?)||(\\.)?")

    /**
     * Filters the text based on the Regex pattern.
     */
    override fun filter(
        source: CharSequence,
        start: Int,
        end: Int,
        dest: Spanned,
        dstart: Int,
        dend: Int
    ): CharSequence? {
        val matcher = pattern.matcher(
            dest.subSequence(0, dstart).toString() + source.subSequence(
                start,
                end
            ).toString() + dest.subSequence(dend, dest.length)
        )

        return if (!matcher.matches()) {
            ""
        } else {
            null
        }
    }
}
