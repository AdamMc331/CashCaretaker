package com.androidessence.cashcaretaker.refresh

import android.os.Bundle
import android.support.v7.app.AppCompatActivity

/**
 * Base Activity for Cash Caretaker.
 *
 * Created by adam.mcneilly on 2/19/17.
 */
open class CoreActivity: AppCompatActivity() {
    var dataSource: CCDataSource? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        dataSource = CCDataSource(this)
    }

    override fun onResume() {
        super.onResume()
        dataSource?.open()
    }

    override fun onPause() {
        super.onPause()
        dataSource?.close()
    }
}