package com.androidessence.cashcaretaker

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import com.adammcneilly.CoreDividerItemDecoration
import com.androidessence.cashcaretaker.adapters.AccountAdapter
import com.androidessence.cashcaretaker.models.Account
import kotlinx.android.synthetic.main.activity_accounts.*

class AccountsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_accounts)

        // Create linear layout manager
        val linearLayoutManager = LinearLayoutManager(this)
        linearLayoutManager.orientation = LinearLayoutManager.VERTICAL

        // Create adapter
        val accountAdapter = AccountAdapter(getSampleAccounts())

        // Get basic divider
        val itemDecoration = CoreDividerItemDecoration(this, LinearLayoutManager.VERTICAL)

        // Setup values for recyclerview
        account_recycler_view.layoutManager = linearLayoutManager
        account_recycler_view.addItemDecoration(itemDecoration)
        account_recycler_view.adapter = accountAdapter
    }

    private fun getSampleAccounts(): List<Account> {
        val checking = Account()
        checking.name = "Checking"
        checking.balance = 100.00

        val savings = Account()
        savings.name = "Savings"
        savings.balance = 10000.00

        return listOf(checking, savings)
    }
}
