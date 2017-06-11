package com.androidessence.cashcaretaker.activities

import android.support.v4.app.Fragment
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.androidessence.cashcaretaker.R
import com.androidessence.cashcaretaker.adapters.AccountAdapterK
import com.androidessence.cashcaretaker.data.CCDataSource

/**
 * A placeholder fragment containing a simple view.
 */
class AccountsActivityKFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater?.inflate(R.layout.fragment_accounts_activity_k, container, false)

        val dataSource = CCDataSource(context)
        val recyclerview = view?.findViewById(R.id.account_list) as RecyclerView
        val layoutManager = LinearLayoutManager(context)

        dataSource.open()
        val accounts = dataSource.getAccounts()
        dataSource.close()
        val adapter = AccountAdapterK(accounts.toMutableList())

        recyclerview.layoutManager = layoutManager
        recyclerview.adapter = adapter

        return view
    }

    companion object {
        fun newInstance(): AccountsActivityKFragment {
            val args = Bundle()

            val fragment = AccountsActivityKFragment()
            fragment.arguments = args

            return fragment
        }
    }
}
