package com.androidessence.refresh

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.view.View
import com.adammcneilly.CoreDividerItemDecoration

class AccountsActivity : AppCompatActivity(), View.OnClickListener {

    var adapter: AccountAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_accounts)

        val toolbar = findViewById(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)

        val fab = findViewById(R.id.fab) as FloatingActionButton
        fab.setOnClickListener(this)

        adapter = AccountAdapter()
        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        val recyclerView = findViewById(R.id.account_list) as RecyclerView
        recyclerView.adapter = adapter
        recyclerView.layoutManager = layoutManager
        recyclerView.addItemDecoration(CoreDividerItemDecoration(this, LinearLayoutManager.VERTICAL))
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.fab -> startActivity(Intent(this, AddAccountActivity::class.java))
        }
    }
}
