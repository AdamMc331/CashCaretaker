package com.androidessence.cashcaretaker.addtransaction

import com.androidessence.cashcaretaker.transaction.Transaction

/**
 * Interface for adding a transaction in the database.
 */
interface AddTransactionInteractor {

    /**
     * Inserts a list of transactions into the database.
     *
     * @param[transactions] The transactions to be inserted.
     * @return The identifiers of all the transactions that were inserted.
     */
    fun insert(transactions: List<Transaction>): List<Long>
}