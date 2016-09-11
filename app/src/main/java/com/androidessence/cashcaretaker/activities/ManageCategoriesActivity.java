package com.androidessence.cashcaretaker.activities;

import android.os.Bundle;

import com.androidessence.cashcaretaker.R;
import com.androidessence.cashcaretaker.core.CoreActivity;

public class ManageCategoriesActivity extends CoreActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_categories);

        setupToolbar(true);
    }

}
