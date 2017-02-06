package com.androidessence.cashcaretaker.refresh

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import com.androidessence.cashcaretaker.DividerItemDecoration
import com.androidessence.cashcaretaker.R
import java.util.*

class AccountsActivity : AppCompatActivity() {
    private var testAccounts: List<Account> = ArrayList()

    init {
        val checking = Account()
        checking.name = "Checking"
        checking.balance = 100.0

        val savings = Account()
        savings.name = "Savings"
        savings.balance = 1000.0

        testAccounts = listOf(checking, savings)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_accounts_refresh)
        val toolbar = findViewById(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)

        val fab = findViewById(R.id.fab) as FloatingActionButton
        fab.setOnClickListener { view ->
            val intent = Intent(this, AddAccountActivity::class.java)
            startActivity(intent)
        }

        val adapter = AccountAdapter(testAccounts)
        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        val recyclerView = findViewById(R.id.account_list) as RecyclerView
        recyclerView.adapter = adapter
        recyclerView.layoutManager = layoutManager
        recyclerView.addItemDecoration(DividerItemDecoration(this, LinearLayoutManager.VERTICAL))
    }

}
