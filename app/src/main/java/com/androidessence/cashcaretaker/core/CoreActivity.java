package com.androidessence.cashcaretaker.core;

import android.annotation.SuppressLint;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.androidessence.cashcaretaker.R;

/**
 * Core class to handle all of the Activity functions.
 *
 * Created by adam.mcneilly on 9/5/16.
 */
@SuppressLint("Registered")
public class CoreActivity extends AppCompatActivity {

    protected void setupToolbar(boolean displayHome) {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if(getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(displayHome);
        }
    }

    protected void setToolbarTitle(CharSequence title) {
        if(getSupportActionBar() != null) {
            getSupportActionBar().setTitle(title);
        }
    }
}
