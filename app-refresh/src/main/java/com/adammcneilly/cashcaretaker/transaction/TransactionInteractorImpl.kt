package com.adammcneilly.cashcaretaker.transaction

import com.adammcneilly.cashcaretaker.App
import com.adammcneilly.cashcaretaker.data.CCDatabase
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
}