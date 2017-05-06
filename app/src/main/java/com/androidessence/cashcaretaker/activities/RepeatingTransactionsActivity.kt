package com.androidessence.cashcaretaker.activities

import android.os.Bundle

import com.androidessence.cashcaretaker.R
import com.androidessence.cashcaretaker.core.CoreActivity

/**
 * Activity that allows the user to view their repeating transactions.

 * Created by adam.mcneilly on 9/5/16.
 */
class RepeatingTransactionsActivity : CoreActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_repeating_transactions)

        setupToolbar(true)
    }
}
