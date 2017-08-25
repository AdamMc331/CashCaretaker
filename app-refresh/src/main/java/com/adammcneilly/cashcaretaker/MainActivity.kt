package com.adammcneilly.cashcaretaker

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import com.adammcneilly.cashcaretaker.views.AccountFragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        supportFragmentManager.beginTransaction().replace(R.id.container, AccountFragment.newInstance()).commit()
    }
}
