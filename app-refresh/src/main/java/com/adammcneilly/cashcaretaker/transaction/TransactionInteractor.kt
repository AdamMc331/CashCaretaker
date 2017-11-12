package com.adammcneilly.cashcaretaker.transaction

import io.reactivex.Flowable

/**
 * Interface for transaction data.
 */
interface TransactionInteractor {
    fun getForAccount(accountName: String): Flowable<List<Transaction>>
}