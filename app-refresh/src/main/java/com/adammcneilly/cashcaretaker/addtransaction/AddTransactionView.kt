package com.adammcneilly.cashcaretaker.addtransaction

import com.adammcneilly.cashcaretaker.core.DataController


/**
 * View to add a transaction.
 */
interface AddTransactionView: DataController {
    fun showTransactionDescriptionError()
    fun showTransactionAmountError()
    fun onInserted(ids: List<Long>)
}