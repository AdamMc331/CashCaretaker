package com.androidessence.cashcaretaker.transaction

import com.androidessence.cashcaretaker.data.CCRepository
import io.reactivex.Flowable

/**
 * Implementation of TransactionInteractor.
 */
class TransactionInteractorImpl: TransactionInteractor {
    override fun getForAccount(accountName: String): Flowable<List<Transaction>> =
            CCRepository.getTransactionsForAccount(accountName)

    override fun delete(transaction: Transaction): Int =
            CCRepository.deleteTransaction(transaction)
}