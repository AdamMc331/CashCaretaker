package com.androidessence.cashcaretaker.activities

import android.os.Bundle
import com.androidessence.cashcaretaker.R
import com.androidessence.cashcaretaker.core.CoreActivity

class ManageCategoriesActivity : CoreActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_manage_categories)

        setupToolbar(true)
    }

}
