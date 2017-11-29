package com.androidessence.cashcaretaker

import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.ViewAction
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.contrib.RecyclerViewActions
import android.support.test.espresso.matcher.ViewMatchers.*
import android.support.v7.widget.RecyclerView
import org.hamcrest.Matchers.allOf

/**
 * Utility methods for testing.
 */
class TestUtils {
    companion object {
        fun <T: RecyclerView.ViewHolder> matchTextInRecyclerView(text: String, recyclerViewId: Int, textViewId: Int, position: Int) {
            onView(allOf(withId(recyclerViewId), isCompletelyDisplayed())).perform(RecyclerViewActions.actionOnItemAtPosition<T>(position, scrollTo()))

            onView(allOf(withRecylerView(recyclerViewId).atPositionOnView(position, textViewId), isCompletelyDisplayed())).check(matches(withText(text)))
        }

        private fun scrollTo(): ViewAction = ScrollToAction()

        private fun withRecylerView(recyclerViewId: Int): RecyclerViewMatcher =
                RecyclerViewMatcher(recyclerViewId)
    }
}