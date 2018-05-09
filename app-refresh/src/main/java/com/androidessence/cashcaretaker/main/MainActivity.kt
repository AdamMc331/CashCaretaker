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


class MainActivity : AppCompatActivity(), MainController, FragmentManager.OnBackStackChangedListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

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
     *
     * @param[fragment] The fragment that needs to be displayed.
     * @param[tag] The tag used to identify this fragment in the backstack.
     * @param[replace] Flag for whether we should be replacing a fragment (if true), or just add to the activity state (if false).
     * @param[addToBackStack] Flag for adding this transaction to the back stack so the entry is remembered (if true), or simply ignored after being committed (if false).
     * @param[container] Identifier of the container view that we are adding/replacing the fragment of.
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
            //TODO: Clean
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
