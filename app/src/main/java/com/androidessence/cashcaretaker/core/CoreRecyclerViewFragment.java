package com.androidessence.cashcaretaker.core;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.androidessence.cashcaretaker.DividerItemDecorationR;


/**
 * Base Fragment for any fragments that simply display a list in the RecyclerView.
 *
 * Created by adam.mcneilly on 9/8/16.
 */
public abstract class CoreRecyclerViewFragment extends CoreFragment {
    /**
     * The RecyclerView that displays data in this fragment.
     */
    protected RecyclerView recyclerView;

    /**
     * Sets up the boilerplate work for this RecyclerView such as the LayoutManager.
     */
    protected void setupRecyclerView(int orientation) {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(orientation);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.addItemDecoration(new DividerItemDecorationR(getActivity(), orientation));
    }
}
