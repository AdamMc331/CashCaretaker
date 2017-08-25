package com.adammcneilly.cashcaretaker.views

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
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
        fab.setOnClickListener {
            presenter.onAddClicked()
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
        recyclerView.hide()
        progressBar.show()
    }

    override fun hideProgress() {
        progressBar.hide()
        recyclerView.show()
    }

    override fun setAccounts(accounts: List<Account>) {
        adapter.items = accounts
    }

    override fun navigateToAddAccount() {
        val intent = Intent(this, AddAccountActivity::class.java)
        startActivity(intent)
    }
}
