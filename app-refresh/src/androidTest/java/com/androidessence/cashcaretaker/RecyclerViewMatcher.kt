package com.androidessence.cashcaretaker

import android.content.res.Resources
import androidx.appcompat.widget.RecyclerView
import android.view.View
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.TypeSafeMatcher

/**
 * Matcher class for RecyclerView assertions.
 */
class RecyclerViewMatcher(private val recyclerViewId: Int) {

    fun atPositionOnView(position: Int, targetViewId: Int): Matcher<View> {
        return  object : TypeSafeMatcher<View>() {
            var childView: View? = null
            var resources: Resources? = null

            override fun matchesSafely(item: View?): Boolean {
                this.resources = item?.resources

                if (childView == null) {
                    val recyclerView = item?.rootView?.findViewById(recyclerViewId) as? RecyclerView

                    if (recyclerView != null && recyclerView.id == recyclerViewId) {
                        childView = recyclerView.findViewHolderForAdapterPosition(position).itemView
                    } else {
                        return false
                    }
                }

                return if (targetViewId == -1) {
                    item == childView
                } else {
                    item == childView?.findViewById(targetViewId)
                }
            }

            override fun describeTo(description: Description?) {
                var idDescription = recyclerViewId.toString()

                if (this.resources != null) {
                    idDescription = try {
                        this.resources?.getResourceName(recyclerViewId).orEmpty()
                    } catch (e: Resources.NotFoundException) {
                        "$recyclerViewId (resource name not found)"
                    }
                }

                description?.appendText("with id: $idDescription")
            }
        }
    }
}
