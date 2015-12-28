package com.androidessence.cashcaretaker;

import android.content.Context;
import android.support.wearable.view.WearableListView;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by adammcneilly on 12/27/15.
 */
public class WearableAccountItemLayout extends LinearLayout implements WearableListView.OnCenterProximityListener {
    private TextView mAccountName;
    private TextView mAccountBalance;

    public WearableAccountItemLayout(Context context) {
        this(context, null);
    }

    public WearableAccountItemLayout(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public WearableAccountItemLayout(Context context, AttributeSet attributeSet, int defStyle) {
        super(context, attributeSet, defStyle);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        mAccountName = (TextView) findViewById(R.id.account_name);
        mAccountBalance = (TextView) findViewById(R.id.account_balance);
    }

    @Override
    public void onCenterPosition(boolean b) {

    }

    @Override
    public void onNonCenterPosition(boolean b) {

    }
}
