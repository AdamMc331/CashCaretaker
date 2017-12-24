package com.androidessence.cashcaretaker.transaction

import io.reactivex.Flowable

/**
 * Interface for transaction data.
 */
interface TransactionInteractor {
    /**
     * Retrieves all transactions for a particular account.
     *
     * @param[accountName] The name of the account to pull transactions for.
     * @return A Flowable that retrieves the list of transactions.
     */
    fun getForAccount(accountName: String): Flowable<List<Transaction>>

    /**
     * Deletes a transaction from the database.
     *
     * @param[transaction] The transaction that is being deleted.
     * @return The number of rows that were removed.
     */
    fun delete(transaction: Transaction): Int
}