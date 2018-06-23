package com.androidessence.cashcaretaker.transaction

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.androidessence.cashcaretaker.databinding.ListItemTransactionBinding
import io.reactivex.subjects.PublishSubject

/**
 * Adapter for displaying Transactions in a RecyclerView.
 */
class TransactionAdapter(items: List<Transaction> = ArrayList()): RecyclerView.Adapter<TransactionAdapter.TransactionViewHolder>() {
    val transactionLongClicked: PublishSubject<Transaction> = PublishSubject.create()

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
            itemView.setOnLongClickListener {
                transactionLongClicked.onNext(items[adapterPosition])
                true
            }
        }

        fun bindItem(item: Transaction?) {
            viewModel.transaction = item
            binding.executePendingBindings()
        }
    }
}