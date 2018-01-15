package com.androidessence.cashcaretaker.addtransaction

import com.androidessence.cashcaretaker.data.CCRepository
import com.androidessence.cashcaretaker.transaction.Transaction

/**
 * Implementation for database interaction.
 */
class AddTransactionInteractorImpl : AddTransactionInteractor {
    override fun insert(transactions: List<Transaction>): List<Long> =
            CCRepository.insertTransactions(transactions)

    override fun update(transaction: Transaction): Int =
            CCRepository.updateTransaction(transaction)
}