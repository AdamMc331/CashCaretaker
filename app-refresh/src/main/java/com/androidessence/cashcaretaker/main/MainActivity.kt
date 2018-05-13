package com.androidessence.cashcaretaker.main

import android.os.Bundle
import android.support.annotation.IdRes
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import com.androidessence.cashcaretaker.R
import com.androidessence.cashcaretaker.account.AccountFragment
import com.androidessence.cashcaretaker.addaccount.AddAccountDialog
import com.androidessence.cashcaretaker.transaction.TransactionFragment
import com.androidessence.cashcaretaker.transfer.AddTransferDialog
import com.crashlytics.android.Crashlytics
import io.fabric.sdk.android.Fabric

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

    override fun onAccountInserted() {
        supportFragmentManager.popBackStackImmediate(AccountFragment.FRAGMENT_NAME, 0)
    }

    override fun showTransactions(accountName: String) {
        showFragment(TransactionFragment.newInstance(accountName), TransactionFragment.FRAGMENT_NAME)
    }

    override fun showAccounts() {
        showFragment(AccountFragment.newInstance(), AccountFragment.FRAGMENT_NAME, true)
    }

    /**
     * Displays a fragment inside this Activity.
     */
    private fun showFragment(fragment: Fragment, tag: String, replace: Boolean = false, addToBackStack: Boolean = true, @IdRes container: Int = R.id.container) {
        val transaction = supportFragmentManager.beginTransaction()

        if (replace) {
            transaction.replace(container, fragment, tag)
        } else {
            transaction.add(container, fragment, tag)
        }

        if (addToBackStack) {
            transaction.addToBackStack(tag)
        }

        transaction.commit()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater?.inflate(R.menu.menu_accounts, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean = when (item?.itemId) {
        android.R.id.home -> {
            onBackPressed()
            true
        }
        R.id.action_transfer -> {
            //TODO: Clean this up. It should only be in the account fragment and only when there's >= 2 accounts.
            val dialog = AddTransferDialog()
            dialog.show(supportFragmentManager, AddTransferDialog::class.java.simpleName)
            true
        }
        else -> super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        val backStackCount = supportFragmentManager.backStackEntryCount

        if (backStackCount > 1) {
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
