package com.androidessence.cashcaretaker.addtransaction

import com.androidessence.cashcaretaker.core.BasePresenter
import java.util.*

/**
 * Presenter for adding an account.
 */
interface AddTransactionPresenter: BasePresenter {
    fun onInserted(ids: List<Long>)
    fun onTransactionDescriptionError()
    fun onTransactionAmountError()

    fun insert(accountName: String, transactionDescription: String, transactionAmount: String, withdrawal: Boolean, date: Date)
}