package com.androidessence.cashcaretaker.transaction

import com.androidessence.cashcaretaker.core.DataController

/**
 * View for displaying a list of transactions.
 */
interface TransactionController : DataController {
    fun setTransactions(transactions: List<Transaction>)
}