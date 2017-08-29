package com.adammcneilly.cashcaretaker.main

import android.content.Context
import android.os.Bundle
import android.support.v4.app.FragmentManager
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.MenuItem
import android.view.inputmethod.InputMethodManager
import com.adammcneilly.cashcaretaker.R
import com.adammcneilly.cashcaretaker.account.AccountFragment
import com.adammcneilly.cashcaretaker.addaccount.AddAccountDialog
import com.adammcneilly.cashcaretaker.addaccount.AddAccountFragment
import timber.log.Timber


class MainActivity : AppCompatActivity(), MainView, FragmentManager.OnBackStackChangedListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        supportFragmentManager.addOnBackStackChangedListener(this)

        supportFragmentManager.beginTransaction()
                .add(R.id.container, AccountFragment.newInstance(), AccountFragment.FRAGMENT_NAME)
                .addToBackStack(AccountFragment.FRAGMENT_NAME)
                .commit()
    }

    override fun navigateToAddAccount() {
        val dialog = AddAccountDialog()
        dialog.show(supportFragmentManager, AddAccountDialog.FRAGMENT_NAME)
//        supportFragmentManager.beginTransaction()
//                .add(R.id.container, AddAccountFragment.newInstance(), AddAccountFragment.FRAGMENT_NAME)
//                .addToBackStack(AddAccountFragment.FRAGMENT_NAME)
//                .commit()
    }

    override fun onAccountInserted() {
        Timber.d("onAccountInserted")

        hideKeyboard()
        supportFragmentManager.popBackStackImmediate(AccountFragment.FRAGMENT_NAME, 0)
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
            supportFragmentManager.popBackStackImmediate()
        } else {
            finish()
        }
    }

    override fun onBackStackChanged() {
        val shouldShowUp = supportFragmentManager.backStackEntryCount > 1
        supportActionBar?.setDisplayHomeAsUpEnabled(shouldShowUp)

        val titleResource = when (supportFragmentManager.getBackStackEntryAt(supportFragmentManager.backStackEntryCount - 1).name) {
            AddAccountFragment.FRAGMENT_NAME -> R.string.add_account
            else -> R.string.app_name
        }
        supportActionBar?.setTitle(titleResource)
    }

    fun showKeyboard() {
        val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY)
    }

    fun hideKeyboard() {
        val inputManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

        // check if no view has focus:
        val v = currentFocus ?: return
        inputManager.hideSoftInputFromWindow(v.windowToken, 0)
    }
}
