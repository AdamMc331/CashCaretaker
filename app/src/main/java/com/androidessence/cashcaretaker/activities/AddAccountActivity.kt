package com.androidessence.cashcaretaker.activities

import android.os.Bundle

import com.androidessence.cashcaretaker.R
import com.androidessence.cashcaretaker.core.CoreActivity

/**
 * Activity that allows a user to add a new account.

 * Created by adam.mcneilly on 9/5/16.
 */
class AddAccountActivity : CoreActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_account)

        setupToolbar(true)
    }
}
