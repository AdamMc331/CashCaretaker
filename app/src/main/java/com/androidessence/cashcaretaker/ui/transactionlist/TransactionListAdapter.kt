package com.androidessence.cashcaretaker.ui.transactionlist

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.androidessence.cashcaretaker.core.models.Transaction
import com.androidessence.cashcaretaker.databinding.ListItemTransactionBinding

/**
 * Adapter for displaying Transactions in a RecyclerView.
 */
class TransactionListAdapter(
    private val transactionLongClicked: (Transaction) -> Unit
) : RecyclerView.Adapter<TransactionListAdapter.TransactionViewHolder>() {

    var items: List<Transaction> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransactionViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ListItemTransactionBinding.inflate(inflater, parent, false)
        return TransactionViewHolder(
            binding,
            transactionLongClicked
        )
    }

    override fun onBindViewHolder(holder: TransactionViewHolder, position: Int) {
        holder.bindItem(items[position])
    }

    override fun getItemCount(): Int = items.size

    class TransactionViewHolder(
        private val binding: ListItemTransactionBinding,
        private val transactionLongClicked: (Transaction) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {
        private val viewModel = TransactionViewModel()

        init {
            binding.viewModel = viewModel
            itemView.setOnLongClickListener {
                viewModel.transaction?.let(transactionLongClicked::invoke)
                true
            }
        }

        fun bindItem(item: Transaction?) {
            viewModel.transaction = item
            binding.executePendingBindings()
        }
    }
}
