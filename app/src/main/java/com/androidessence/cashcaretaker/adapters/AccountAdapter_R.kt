package com.androidessence.cashcaretaker.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.adammcneilly.CoreRecyclerViewAdapter
import com.adammcneilly.CoreViewHolder
import com.androidessence.cashcaretaker.R
import com.androidessence.cashcaretaker.dataTransferObjects.Account
import com.androidessence.utility.asCurrency

/**
 * Adapter for displaying a list of accounts.
 */
class AccountAdapter_R(accounts: MutableList<Account> = ArrayList()) : CoreRecyclerViewAdapter<Account, AccountAdapter_R.AccountViewHolder>(accounts) {

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): AccountViewHolder {
        val context = parent?.context
        val view = LayoutInflater.from(context).inflate(R.layout.list_item_account_refresh, parent, false)
        return AccountViewHolder(view)
    }

    class AccountViewHolder(view: View): CoreViewHolder<Account>(view) {
        val nameTextView: TextView = view.findViewById(R.id.account_name) as TextView
        val balanceTextView: TextView = view.findViewById(R.id.account_balance) as TextView

        override fun bindItem(item: Account?) {
            nameTextView.text = item?.name
            balanceTextView.text = item?.balance?.asCurrency()
        }
    }
}