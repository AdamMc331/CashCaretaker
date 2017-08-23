package com.adammcneilly.cashcaretaker.views

import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.widget.ProgressBar
import com.adammcneilly.cashcaretaker.R
import com.adammcneilly.cashcaretaker.adapters.AccountAdapter
import com.adammcneilly.cashcaretaker.entities.Account
import com.adammcneilly.cashcaretaker.interactors.AccountInteractorImpl
import com.adammcneilly.cashcaretaker.presenters.AccountPresenter
import com.adammcneilly.cashcaretaker.presenters.AccountPresenterImpl
import com.androidessence.utility.hide
import com.androidessence.utility.show
import timber.log.Timber

class AccountsActivity : AppCompatActivity(), AccountView {
    private val adapter = AccountAdapter()
    private val progressBar: ProgressBar by lazy { findViewById<ProgressBar>(R.id.progress) }
    private val recyclerView: RecyclerView by lazy { findViewById<RecyclerView>(R.id.accounts) }
    private val presenter: AccountPresenter by lazy { AccountPresenterImpl(this, AccountInteractorImpl()) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_accounts)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        val layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = layoutManager

        val fab = findViewById<FloatingActionButton>(R.id.fab)
        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
        }
    }

    override fun onResume() {
        super.onResume()
        presenter.onResume()
        Timber.d("onResume")
    }

    override fun onDestroy() {
        presenter.onDestroy()
        super.onDestroy()
        Timber.d("onDestroy")
    }

    override fun showProgress() {
        recyclerView.hide()
        progressBar.show()
        Timber.d("Showing Progress")
    }

    override fun hideProgress() {
        progressBar.hide()
        recyclerView.show()
        Timber.d("Hiding Progress")
    }

    override fun setAccounts(accounts: List<Account>) {
        adapter.items = accounts
        Timber.d("Setting accounts: $accounts")
    }
}
