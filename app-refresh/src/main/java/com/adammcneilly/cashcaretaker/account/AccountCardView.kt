package com.adammcneilly.cashcaretaker.account

import android.content.Context
import android.support.v7.widget.CardView
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import com.adammcneilly.cashcaretaker.R

/**
 * CardView for displaying a list of accounts.
 */
class AccountCardView(context: Context): CardView(context) {
    private val adapter = AccountAdapter()

    init {
        inflate(context, R.layout.card_account, this)

        val recyclerView = findViewById<RecyclerView>(R.id.account_list)
        val linearLayoutManager = LinearLayoutManager(context)
        recyclerView.layoutManager = linearLayoutManager
        recyclerView.adapter = adapter
    }

    fun setAccounts(accounts: List<Account>) {
        adapter.items = accounts
    }
}