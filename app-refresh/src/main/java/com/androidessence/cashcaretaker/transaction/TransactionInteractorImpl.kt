package com.androidessence.cashcaretaker.transaction

import com.androidessence.cashcaretaker.App
import com.androidessence.cashcaretaker.data.CCDatabase
import io.reactivex.Flowable

/**
 * Implmentation of TransactionInteractor.
 */
class TransactionInteractorImpl: TransactionInteractor {
    override fun getForAccount(accountName: String): Flowable<List<Transaction>> {
        return CCDatabase.getInMemoryDatabase(App.instance)
                .transactionDao()
                .getAllForAccount(accountName)
    }

    override fun delete(transaction: Transaction): Int {
        return CCDatabase.getInMemoryDatabase(App.instance)
                .transactionDao()
                .delete(transaction)
    }
}