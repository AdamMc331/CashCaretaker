package com.androidessence.cashcaretaker

import android.content.Context
import android.support.design.widget.CoordinatorLayout
import android.support.design.widget.FloatingActionButton
import android.support.v4.view.ViewCompat
import android.util.AttributeSet
import android.view.View

/**
 * Behavior for FloatingActionButtons that hides them when a RecyclerView is scrolled upward, and shows them when scrolling occurs the other way.

 * Created by adammcneilly on 11/7/15.
 */
class FABScrollBehavior(context: Context, attributeSet: AttributeSet) : FloatingActionButton.Behavior() {

    override fun onNestedScroll(coordinatorLayout: CoordinatorLayout?, child: FloatingActionButton?, target: View?, dxConsumed: Int, dyConsumed: Int, dxUnconsumed: Int, dyUnconsumed: Int) {
        super.onNestedScroll(coordinatorLayout, child, target, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed)
        if (dyConsumed > 0 && child!!.visibility == View.VISIBLE) {
            child.hide()
        } else if (dyConsumed < 0 && child!!.visibility == View.GONE) {
            child.show()
        }
    }

    override fun onStartNestedScroll(coordinatorLayout: CoordinatorLayout?, child: FloatingActionButton?, directTargetChild: View?, target: View?, nestedScrollAxes: Int): Boolean {
        return nestedScrollAxes == ViewCompat.SCROLL_AXIS_VERTICAL
    }
}
