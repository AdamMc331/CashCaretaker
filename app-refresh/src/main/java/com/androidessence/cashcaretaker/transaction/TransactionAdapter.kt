package com.androidessence.cashcaretaker.transaction

import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.androidessence.cashcaretaker.R
import com.androidessence.cashcaretaker.databinding.ListItemTransactionBinding
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
        val inflater = LayoutInflater.from(parent.context)
        val binding = ListItemTransactionBinding.inflate(inflater, parent, false)
        return TransactionViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TransactionViewHolder, position: Int) {
        holder.bindItem(items[position])
    }

    override fun getItemCount(): Int = items.size

    inner class TransactionViewHolder(private val binding: ListItemTransactionBinding): RecyclerView.ViewHolder(binding.root) {
        private val viewModel = TransactionDataModel()

        init {
            binding.viewModel = viewModel
        }

        fun bindItem(item: Transaction?) {
            viewModel.transaction = item
            binding.executePendingBindings()
        }
    }
}