package com.androidessence.cashcaretaker.main

import android.os.Bundle
import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
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
        initializeBackButton()

        if (savedInstanceState == null) {
            showAccounts()
        }
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
        initializeBackButton()

        when (supportFragmentManager.getBackStackEntryAt(supportFragmentManager.backStackEntryCount - 1).name) {
            AccountFragment.FRAGMENT_NAME -> supportActionBar?.setTitle(R.string.app_name)
        }
    }

    /**
     * Checks the size of the back stack and sets the visibility of the back button if necessary.
     */
    private fun initializeBackButton() {
        val shouldShowUp = supportFragmentManager.backStackEntryCount > 1
        supportActionBar?.setDisplayHomeAsUpEnabled(shouldShowUp)
    }
}
