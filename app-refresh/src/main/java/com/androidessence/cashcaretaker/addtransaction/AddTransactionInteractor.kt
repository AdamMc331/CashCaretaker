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

    /**
     * Updates a transaction in the database.
     *
     * @param[transaction] The transaction that needs to be updated.
     * @return The number of rows in the database that were updated.
     */
    fun update(transaction: Transaction): Int
}