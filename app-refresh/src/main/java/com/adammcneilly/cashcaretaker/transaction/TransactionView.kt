package com.adammcneilly.cashcaretaker.transaction

import com.adammcneilly.cashcaretaker.core.DataView

/**
 * View for displaying a list of transactions.
 */
interface TransactionView: DataView {
    fun setTransactions(transactions: List<Transaction>)
}