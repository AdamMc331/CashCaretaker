package com.androidessence.cashcaretaker.account

import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.androidessence.cashcaretaker.R
import com.androidessence.utility.asCurrency
import com.androidessence.utility.isNegative

/**
 * Adapter for displaying Accounts in a RecyclerView.
 *
 * @property[controller] A controller supplied to handle callbacks for item selection.
 * @property[items] The list of accounts to display.
 */
class AccountAdapter(private val controller: AccountController?, items: List<Account> = ArrayList()): RecyclerView.Adapter<AccountAdapter.AccountViewHolder>() {

    var items: List<Account> = items
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AccountViewHolder {
        val context = parent.context
        val view = LayoutInflater.from(context).inflate(R.layout.list_item_account, parent, false)
        return AccountViewHolder(view)
    }

    override fun onBindViewHolder(holder: AccountViewHolder, position: Int) {
        holder.bindItem(items[position])
    }

    override fun getItemCount(): Int = items.size

    inner class AccountViewHolder(view: View): RecyclerView.ViewHolder(view) {
        private val name = view.findViewById<TextView>(R.id.accountName)
        private val balance = view.findViewById<TextView>(R.id.accountBalance)
        private val withdrawalButton = view.findViewById<ImageView>(R.id.withdrawal_button)
        private val depositButton = view.findViewById<ImageView>(R.id.deposit_button)
        private val black = ContextCompat.getColor(view.context, R.color.mds_black)
        private val red = ContextCompat.getColor(view.context, R.color.mds_red_500)

        init {
            withdrawalButton?.setOnClickListener { controller?.onTransactionButtonClicked(items[adapterPosition], true) }
            depositButton?.setOnClickListener { controller?.onTransactionButtonClicked(items[adapterPosition], false) }
            view.setOnClickListener { controller?.onAccountSelected(items[adapterPosition]) }
            view.setOnLongClickListener {
                controller?.onAccountLongClicked(items[adapterPosition])
                true
            }
        }

        /**
         * Binds an Account object to the row view for display.
         */
        fun bindItem(item: Account?) {
            name?.text = item?.name
            balance?.text = item?.balance?.asCurrency()
            val isNegative = item?.balance?.isNegative() ?: false
            val balanceColor = if (isNegative) red else black
            balance?.setTextColor(balanceColor)
        }
    }
}