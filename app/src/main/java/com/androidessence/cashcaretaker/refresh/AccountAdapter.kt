package com.androidessence.cashcaretaker.refresh

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.adammcneilly.CoreRecyclerViewAdapter
import com.adammcneilly.CoreViewHolder
import com.androidessence.cashcaretaker.R
import com.androidessence.utility.Utility
import com.androidessence.utility.asCurrency

/**
 * Displays a list of accounts.
 *
 * Created by adam.mcneilly on 1/25/17.
 */
open class AccountAdapter(): CoreRecyclerViewAdapter<Account, AccountAdapter.AccountViewHolder>() {

    constructor(items: List<Account>): this() {
        this.items = items
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): AccountViewHolder {
        val context = parent?.context
        val view = LayoutInflater.from(context).inflate(R.layout.list_item_account_refresh, parent, false)
        return AccountViewHolder(view)
    }

    open class AccountViewHolder(view: View): CoreViewHolder<Account>(view) {
        private var name: TextView? = null
        private var balance: TextView? = null

        init {
            name = view.findViewById(R.id.account_name) as? TextView
            balance = view.findViewById(R.id.account_balance) as? TextView
        }

        override fun bindItem(item: Account?) {
            name?.text = item?.name
            balance?.text = item?.balance?.asCurrency()
        }
    }
}