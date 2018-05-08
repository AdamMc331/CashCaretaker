package com.androidessence.cashcaretaker.transaction

import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.androidessence.cashcaretaker.R
import com.androidessence.utility.asCurrency
import com.androidessence.utility.asUIString

/**
 * Adapter for displaying Transactions in a RecyclerView.
 */
class TransactionAdapter(private val controller: TransactionController?, items: List<Transaction> = ArrayList()): RecyclerView.Adapter<TransactionAdapter.TransactionViewHolder>() {

    var items: List<Transaction> = items
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransactionViewHolder {
        val context = parent.context
        val view = LayoutInflater.from(context).inflate(R.layout.list_item_transaction, parent, false)
        return TransactionViewHolder(view)
    }

    override fun onBindViewHolder(holder: TransactionViewHolder, position: Int) {
        holder.bindItem(items[position])
    }

    override fun getItemCount(): Int = items.size

    inner class TransactionViewHolder(view: View?): RecyclerView.ViewHolder(view) {
        private val description = view?.findViewById<TextView>(R.id.transactionDescription) as TextView
        private val amount = view?.findViewById<TextView>(R.id.transactionAmount) as TextView
        private val date = view?.findViewById<TextView>(R.id.transactionDate) as TextView
        private val withdrawalIndicator = view?.findViewById<View>(R.id.withdrawal_indicator) as View
        private val green = ContextCompat.getColor(view?.context!!, R.color.mds_green_500) //TODO:
        private val red = ContextCompat.getColor(view?.context!!, R.color.mds_red_500) //TODO:

        init {
            view?.setOnLongClickListener {
                controller?.onTransactionLongClicked(items[adapterPosition])
                true
            }
        }

        fun bindItem(item: Transaction?) {
            description.text = item?.description
            amount.text = item?.amount?.asCurrency()
            date.text = item?.date.asUIString()

            val isWithdrawal = item?.withdrawal ?: false
            val color = if (isWithdrawal) red else green
            withdrawalIndicator.setBackgroundColor(color)
        }
    }
}