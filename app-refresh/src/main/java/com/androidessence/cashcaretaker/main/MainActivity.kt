package com.androidessence.cashcaretaker.main

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.navigation.NavController
import androidx.navigation.Navigation.findNavController
import com.androidessence.cashcaretaker.R
import com.androidessence.cashcaretaker.addaccount.AddAccountDialog
import com.crashlytics.android.Crashlytics
import io.fabric.sdk.android.Fabric

/**
 * Main entry point into the application.
 */
class MainActivity : AppCompatActivity(), MainController {

    private val navController: NavController
        get() = findNavController(this, R.id.container)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Fabric.with(this, Crashlytics())

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
    }

    override fun navigateToAddAccount() {
        val dialog = AddAccountDialog()
        dialog.show(supportFragmentManager, AddAccountDialog.FRAGMENT_NAME)
    }

    override fun showTransactions(accountName: String) {
        navController.navigate(R.id.action_accountFragment_to_transactionFragment)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean = when (item?.itemId) {
        android.R.id.home -> {
            onBackPressed()
            true
        }
        else -> super.onOptionsItemSelected(item)
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp()
    }
}
