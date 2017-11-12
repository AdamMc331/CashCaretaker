package com.adammcneilly.cashcaretaker.transaction

import com.adammcneilly.cashcaretaker.core.DataController

/**
 * View for displaying a list of transactions.
 */
interface TransactionController : DataController {
    fun setTransactions(transactions: List<Transaction>)
}