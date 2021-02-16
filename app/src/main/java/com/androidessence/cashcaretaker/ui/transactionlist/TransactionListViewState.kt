package com.androidessence.cashcaretaker.ui.transactionlist

import com.androidessence.cashcaretaker.core.models.Transaction

data class TransactionListViewState(
    val showLoading: Boolean,
    val accountName: String,
    val transactions: List<Transaction>,
) {
    val showContent: Boolean
        get() = !showLoading && transactions.isNotEmpty()

    val showEmptyState: Boolean
        get() = !showLoading && transactions.isEmpty()
}
