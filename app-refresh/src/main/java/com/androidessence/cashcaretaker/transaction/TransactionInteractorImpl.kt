package com.androidessence.cashcaretaker.transaction

import com.androidessence.cashcaretaker.data.CCRepository
import io.reactivex.Flowable

/**
 * Implementation of TransactionInteractor.
 */
class TransactionInteractorImpl(private val repository: CCRepository) : TransactionInteractor {
    override fun getForAccount(accountName: String): Flowable<List<Transaction>> =
            repository.getTransactionsForAccount(accountName)

    override fun delete(transaction: Transaction): Int =
            repository.deleteTransaction(transaction)
}