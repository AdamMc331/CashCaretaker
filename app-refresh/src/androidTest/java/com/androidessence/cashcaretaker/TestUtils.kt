package com.androidessence.cashcaretaker

import android.view.View
import android.widget.Checkable
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.isCompletelyDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.Matchers.allOf
import org.hamcrest.Matchers.isA

/**
 * Utility methods for testing.
 */
class TestUtils {
    companion object {
        fun <T : RecyclerView.ViewHolder> matchTextInRecyclerView(
            text: String,
            recyclerViewId: Int,
            textViewId: Int,
            position: Int
        ) {
            onView(allOf(withId(recyclerViewId), isCompletelyDisplayed())).perform(
                RecyclerViewActions.actionOnItemAtPosition<T>(position, scrollTo())
            )

            onView(
                allOf(
                    withRecyclerView(recyclerViewId).atPositionOnView(position, textViewId),
                    isCompletelyDisplayed()
                )
            ).check(matches(withText(text)))
        }

        fun <T : RecyclerView.ViewHolder> clickItemInRecyclerView(
            recyclerViewId: Int,
            targetViewId: Int,
            position: Int
        ) {
            onView(allOf(withId(recyclerViewId), isCompletelyDisplayed())).perform(
                RecyclerViewActions.actionOnItemAtPosition<T>(position, scrollTo())
            )

            onView(
                allOf(
                    withRecyclerView(recyclerViewId).atPositionOnView(position, targetViewId),
                    isCompletelyDisplayed()
                )
            ).perform(click())
        }

        private fun scrollTo(): ViewAction = ScrollToAction()

        private fun withRecyclerView(recyclerViewId: Int): RecyclerViewMatcher =
            RecyclerViewMatcher(recyclerViewId)

        fun setChecked(checked: Boolean): ViewAction {
            return object : ViewAction {
                override fun getConstraints(): Matcher<View> {
                    return object : Matcher<View> {
                        override fun matches(item: Any): Boolean =
                            isA(Checkable::class.java).matches(item)

                        override fun describeMismatch(
                            item: Any,
                            mismatchDescription: Description
                        ) {
                        }

                        override fun describeTo(description: Description) {}

                        override fun _dont_implement_Matcher___instead_extend_BaseMatcher_() {
                        }
                    }
                }

                override fun getDescription(): String? = null

                override fun perform(uiController: UiController, view: View) {
                    val checkableView = view as Checkable
                    checkableView.isChecked = checked
                }
            }
        }
    }
}

class TestObserver<T> : Observer<T> {
    var observedValue: T? = null

    override fun onChanged(t: T) {
        observedValue = t
    }
}

fun <T> LiveData<T>.testObserver() = TestObserver<T>().also {
    observeForever(it)
}
