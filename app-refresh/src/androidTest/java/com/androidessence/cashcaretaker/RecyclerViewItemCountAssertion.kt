package com.androidessence.cashcaretaker

import android.support.test.espresso.NoMatchingViewException
import android.support.test.espresso.ViewAssertion
import android.support.v7.widget.RecyclerView
import android.view.View

import android.support.test.espresso.matcher.ViewMatchers.assertThat
import org.hamcrest.Matchers.`is`

/**
 * Asserts the number of items in a RecyclerView.
 */
class RecyclerViewItemCountAssertion(private val expectedCount: Int) : ViewAssertion {

    override fun check(view: View, noViewFoundException: NoMatchingViewException?) {
        if (noViewFoundException != null) {
            throw noViewFoundException
        }

        val recyclerView = view as RecyclerView
        val adapter = recyclerView.adapter
        assertThat(adapter.itemCount, `is`(expectedCount))
    }

    companion object {
        fun withItemCount(expectedCount: Int): RecyclerViewItemCountAssertion =
                RecyclerViewItemCountAssertion(expectedCount)
    }
}
