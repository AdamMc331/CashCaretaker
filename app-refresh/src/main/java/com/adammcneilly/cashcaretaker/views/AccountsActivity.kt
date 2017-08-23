package com.adammcneilly.cashcaretaker.views

import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import com.adammcneilly.cashcaretaker.R
import com.adammcneilly.cashcaretaker.entities.Account
import com.adammcneilly.cashcaretaker.interactors.AccountInteractorImpl
import com.adammcneilly.cashcaretaker.presenters.AccountPresenter
import com.adammcneilly.cashcaretaker.presenters.AccountPresenterImpl
import timber.log.Timber

class AccountsActivity : AppCompatActivity(), AccountView {

    private val presenter: AccountPresenter by lazy { AccountPresenterImpl(this, AccountInteractorImpl()) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_accounts)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        val fab = findViewById<FloatingActionButton>(R.id.fab)
        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
        }
    }

    override fun onResume() {
        super.onResume()
        presenter.onResume()
    }

    override fun onDestroy() {
        presenter.onDestroy()
        super.onDestroy()
    }

    override fun showProgress() {
        Timber.d("Showing Progress")
    }

    override fun hideProgress() {
        Timber.d("Hiding Progress")
    }

    override fun setAccounts(accounts: List<Account>) {
        Timber.d("Setting accounts: $accounts")
    }
}
