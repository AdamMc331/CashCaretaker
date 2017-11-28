package com.androidessence.cashcaretaker.addtransaction

import com.androidessence.cashcaretaker.transaction.Transaction

/**
 * Interface for adding a transaction in the database.
 */
interface AddTransactionInteractor {
    fun insert(transactions: List<Transaction>): List<Long>
}