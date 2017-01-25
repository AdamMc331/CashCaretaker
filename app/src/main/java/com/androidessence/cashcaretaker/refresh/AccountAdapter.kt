package com.androidessence.cashcaretaker.refresh

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.androidessence.cashcaretaker.R
import com.androidessence.utility.Utility
import java.util.*

/**
 * Displays a list of accounts.
 *
 * Created by adam.mcneilly on 1/25/17.
 */
open class AccountAdapter(): RecyclerView.Adapter<AccountAdapter.AccountViewHolder>() {
    private var items: List<Account> = ArrayList()

    constructor(items: List<Account>): this() {
        this.items = items
    }

    override fun onBindViewHolder(holder: AccountViewHolder?, position: Int) {
        holder?.bindAccount(items[position])
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): AccountViewHolder {
        val context = parent?.context
        val view = LayoutInflater.from(context).inflate(R.layout.list_item_account, parent, false)
        return AccountViewHolder(view)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    open class AccountViewHolder(view: View): RecyclerView.ViewHolder(view) {
        private var name: TextView? = null
        private var balance: TextView? = null

        init {
            name = view.findViewById(R.id.account_name) as? TextView
            balance = view.findViewById(R.id.account_balance) as? TextView
        }

        fun bindAccount(account: Account) {
            name?.text = account.name
            balance?.text = Utility.getCurrencyString(account.balance)
        }
    }
}