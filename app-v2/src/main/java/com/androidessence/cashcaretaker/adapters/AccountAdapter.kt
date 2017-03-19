package com.androidessence.cashcaretaker.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.adammcneilly.CoreRecyclerViewAdapter
import com.adammcneilly.CoreViewHolder
import com.androidessence.cashcaretaker.R
import com.androidessence.cashcaretaker.models.Account
import com.androidessence.utility.asCurrency

/**
 * Adapter that displays a list of account object.
 *
 * Created by adam.mcneilly on 3/19/17.
 */
open class AccountAdapter() : CoreRecyclerViewAdapter<Account, AccountAdapter.AccountViewHolder>() {

    constructor(items: List<Account>): this() {
        this.items = items
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): AccountViewHolder {
        val context = parent?.context
        val view = LayoutInflater.from(context).inflate(R.layout.list_item_account, parent, false)
        return AccountViewHolder(view)
    }

    open class AccountViewHolder(view: View?) : CoreViewHolder<Account>(view) {
        private val accountName: TextView? = view?.findViewById(R.id.account_name) as? TextView
        private val accountBalance: TextView? = view?.findViewById(R.id.account_balance) as? TextView

        override fun bindItem(item: Account?) {
            accountName?.text = item?.name
            accountBalance?.text = item?.balance?.asCurrency()
        }
    }
}