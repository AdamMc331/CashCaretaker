package com.androidessence.cashcaretaker.addtransaction

import com.androidessence.cashcaretaker.data.CCRepository
import com.androidessence.cashcaretaker.transaction.Transaction

/**
 * Implementation for database interaction.
 */
class AddTransactionInteractorImpl(private val repository: CCRepository) : AddTransactionInteractor {
    override fun insert(transactions: List<Transaction>): List<Long> =
            repository.insertTransactions(transactions)

    override fun update(transaction: Transaction): Int =
            repository.updateTransaction(transaction)
}