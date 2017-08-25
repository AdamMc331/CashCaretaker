package com.adammcneilly.cashcaretaker.views

import android.os.Bundle
import android.support.design.widget.TextInputEditText
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import com.adammcneilly.cashcaretaker.R
import com.adammcneilly.cashcaretaker.interactors.AddAccountInteractorImpl
import com.adammcneilly.cashcaretaker.presenters.AddAccountPresenter
import com.adammcneilly.cashcaretaker.presenters.AddAccountPresenterImpl
import com.androidessence.utility.hide
import com.androidessence.utility.show

class AddAccountActivity : AppCompatActivity(), AddAccountView {
    private val accountName: TextInputEditText by lazy { findViewById<TextInputEditText>(R.id.account_name) }
    private val accountBalance: TextInputEditText by lazy { findViewById<TextInputEditText>(R.id.account_balance) }
    private val progressBar: ProgressBar by lazy { findViewById<ProgressBar>(R.id.progress) }
    private val presenter: AddAccountPresenter by lazy { AddAccountPresenterImpl(this, AddAccountInteractorImpl()) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_account)

        val toolbar = findViewById<View>(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        findViewById<Button>(R.id.submit).setOnClickListener { addAccount(accountName.text.toString(), accountBalance.text.toString()) }
    }

    override fun showProgress() {
        progressBar.show()
    }

    override fun hideProgress() {
        progressBar.hide()
    }

    override fun onInsertConflict() {
        accountName.error = getString(R.string.error_account_name_exists)
    }

    override fun addAccount(accountName: String, accountBalance: String) {
        presenter.insert(accountName, accountBalance)
    }

    override fun showAccountNameError() {
        accountName.error = getString(R.string.err_account_name_invalid)
    }

    override fun showAccountBalanceError() {
        accountBalance.error = getString(R.string.err_account_balance_invalid)
    }

    override fun onInserted(ids: List<Long>) {
        finish()
    }
}
