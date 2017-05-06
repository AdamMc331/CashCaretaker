package com.androidessence.cashcaretaker.core

import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView

import com.androidessence.cashcaretaker.DividerItemDecoration


/**
 * Base Fragment for any fragments that simply display a list in the RecyclerView.

 * Created by adam.mcneilly on 9/8/16.
 */
abstract class CoreRecyclerViewFragment : CoreFragment() {
    /**
     * The RecyclerView that displays data in this fragment.
     */
    protected var recyclerView: RecyclerView? = null

    /**
     * Sets up the boilerplate work for this RecyclerView such as the LayoutManager.
     */
    protected open fun setupRecyclerView(orientation: Int, reverseLayout: Boolean = false) {
        val linearLayoutManager = LinearLayoutManager(activity, orientation, reverseLayout)

        recyclerView?.layoutManager = linearLayoutManager
        recyclerView?.addItemDecoration(DividerItemDecoration(activity, orientation))
    }
}
