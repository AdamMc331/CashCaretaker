package com.adammcneilly.cashcaretaker.transaction

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.adammcneilly.cashcaretaker.R
import com.androidessence.utility.asCurrency

/**
 * Adapter for displaying Transactions in a RecyclerView.
 */
class TransactionAdapter(items: List<Transaction> = ArrayList()): RecyclerView.Adapter<TransactionAdapter.TransactionViewHolder>() {

    var items: List<Transaction> = items
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): TransactionViewHolder {
        val context = parent?.context
        val view = LayoutInflater.from(context).inflate(R.layout.list_item_transaction, parent, false)
        return TransactionViewHolder(view)
    }

    override fun onBindViewHolder(holder: TransactionViewHolder?, position: Int) {
        holder?.bindItem(items[position])
    }

    override fun getItemCount(): Int = items.size

    class TransactionViewHolder(view: View?): RecyclerView.ViewHolder(view) {
        private val description = view?.findViewById<TextView>(R.id.transaction_description) as TextView
        private val amount = view?.findViewById<TextView>(R.id.transaction_amount) as TextView

        fun bindItem(item: Transaction?) {
            description.text = item?.description
            amount.text = item?.amount?.asCurrency()
        }
    }
}