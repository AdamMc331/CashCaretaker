package com.androidessence.cashcaretaker.core

import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar

import com.androidessence.cashcaretaker.R

/**
 * Core class to handle all of the Activity functions.

 * Created by adam.mcneilly on 9/5/16.
 */
open class CoreActivity : AppCompatActivity() {

    protected fun setupToolbar(displayHome: Boolean) {
        val toolbar = findViewById(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)

        supportActionBar?.setDisplayHomeAsUpEnabled(displayHome)
    }

    protected fun setToolbarTitle(title: CharSequence) {
        supportActionBar?.title = title
    }
}
