package com.adammcneilly.cashcaretaker.transaction

import com.adammcneilly.cashcaretaker.core.BasePresenter

/**
 * Presenter that handles transaction business logic.
 */
interface TransactionPresenter: BasePresenter {
    fun onResume()
    fun onFetched(transactions: List<Transaction>)
}