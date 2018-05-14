package com.androidessence.cashcaretaker.main

import android.os.Bundle
import android.support.annotation.IdRes
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.MenuItem
import com.androidessence.cashcaretaker.R
import com.androidessence.cashcaretaker.account.AccountFragment
import com.androidessence.cashcaretaker.addaccount.AddAccountDialog
import com.androidessence.cashcaretaker.transaction.TransactionFragment
import com.crashlytics.android.Crashlytics
import io.fabric.sdk.android.Fabric
import timber.log.Timber

/**
 * Main entry point into the application.
 */
class MainActivity : AppCompatActivity(), MainController, FragmentManager.OnBackStackChangedListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Fabric.with(this, Crashlytics())

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        supportFragmentManager.addOnBackStackChangedListener(this)
        showAccounts()
    }

    override fun navigateToAddAccount() {
        val dialog = AddAccountDialog()
        dialog.show(supportFragmentManager, AddAccountDialog.FRAGMENT_NAME)
    }

    override fun showTransactions(accountName: String) {
        Timber.d("Showing Transactions")
        showFragment(TransactionFragment.newInstance(accountName), TransactionFragment.FRAGMENT_NAME)
    }

    override fun showAccounts() {
        showFragment(AccountFragment.newInstance(), AccountFragment.FRAGMENT_NAME)
    }

    /**
     * Displays a fragment inside this Activity.
     */
    private fun showFragment(fragment: Fragment, tag: String, @IdRes container: Int = R.id.container) {
        Timber.d("Adding fragment: $tag")
        supportFragmentManager
                .beginTransaction()
                .replace(container, fragment, tag)
                .addToBackStack(tag)
                .commit()
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean = when (item?.itemId) {
        android.R.id.home -> {
            onBackPressed()
            true
        }
        else -> super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        val backStackCount = supportFragmentManager.backStackEntryCount

        if (backStackCount > 1) {
            Timber.d("Popping BackStack")
            supportFragmentManager.popBackStackImmediate()
        } else {
            finish()
        }
    }

    override fun onBackStackChanged() {
        val shouldShowUp = supportFragmentManager.backStackEntryCount > 1
        supportActionBar?.setDisplayHomeAsUpEnabled(shouldShowUp)

        when (supportFragmentManager.getBackStackEntryAt(supportFragmentManager.backStackEntryCount - 1).name) {
            AccountFragment.FRAGMENT_NAME -> supportActionBar?.setTitle(R.string.app_name)
        }
    }
}
