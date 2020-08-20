package com.androidessence.cashcaretaker.ui.transactionlist

import android.view.View
import com.androidessence.cashcaretaker.core.models.Transaction
import com.androidessence.cashcaretaker.databinding.ListItemTransactionBinding
import com.androidessence.cashcaretaker.ui.transaction.TransactionViewModel
import me.ibrahimyilmaz.kiel.core.RecyclerViewHolder

class TransactionViewHolder(
    view: View
) : RecyclerViewHolder<Transaction>(view) {
    val binding = ListItemTransactionBinding.bind(view)
    val viewModel = TransactionViewModel()

    init {
        binding.viewModel = viewModel
    }

    override fun bind(position: Int, item: Transaction) {
        super.bind(position, item)
        viewModel.transaction = item
        binding.executePendingBindings()
    }
}
