package com.adammcneilly.cashcaretaker.adapters

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.adammcneilly.cashcaretaker.R
import com.adammcneilly.cashcaretaker.entities.Account
import com.androidessence.utility.asCurrency

/**
 * Adapter for displaying Accounts in a RecyclerView.
 */
class AccountAdapter(items: List<Account> = ArrayList()): RecyclerView.Adapter<AccountAdapter.AccountViewHolder>() {

    var items: List<Account> = items
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): AccountViewHolder {
        val context = parent?.context
        val view = LayoutInflater.from(context).inflate(R.layout.list_item_account, parent, false)
        return AccountViewHolder(view)
    }

    override fun onBindViewHolder(holder: AccountViewHolder?, position: Int) {
        holder?.bindItem(items[position])
    }

    override fun getItemCount(): Int = items.size

    class AccountViewHolder(view: View?): RecyclerView.ViewHolder(view) {
        private val name = view?.findViewById<TextView>(R.id.account_name)
        private val balance = view?.findViewById<TextView>(R.id.account_balance)

        fun bindItem(item: Account?) {
            name?.text = item?.name
            balance?.text = item?.balance?.asCurrency()
        }
    }
}