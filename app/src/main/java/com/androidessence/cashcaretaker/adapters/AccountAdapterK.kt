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
 * Adapter that displays a list of Accounts.
 *
 * Created by adam.mcneilly on 5/21/17.
 */
class AccountAdapterK(items: MutableList<Account>) : CoreRecyclerViewAdapter<Account, AccountAdapterK.AccountViewHolder>(items) {

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): AccountViewHolder {
        val context = parent?.context
        val view = LayoutInflater.from(context)?.inflate(R.layout.list_item_account, parent, false)
        return AccountViewHolder(view)
    }

    inner class AccountViewHolder(view: View?) : CoreViewHolder<Account>(view) {
        val nameTextView = view?.findViewById(R.id.account_name) as? TextView
        val balanceTextView = view?.findViewById(R.id.account_balance) as? TextView

        override fun bindItem(item: Account?) {
            nameTextView?.text = item?.name
            balanceTextView?.text = item?.balance?.asCurrency()
        }
    }
}