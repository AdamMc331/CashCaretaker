package com.androidessence.cashcaretaker.core;

import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.view.View;

/**
 * Base Fragment class for all fragments in Cash Caretaker.
 *
 * Created by adam.mcneilly on 9/8/16.
 */
public abstract class CoreFragment extends Fragment {
    protected CoordinatorLayout root;

    /**
     * Retrieves all necessary elements for the Fragment.
     */
    protected abstract void getElements(View view);
}
