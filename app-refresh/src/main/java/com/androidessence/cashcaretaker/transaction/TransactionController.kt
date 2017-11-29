package com.androidessence.cashcaretaker.transaction

import com.androidessence.cashcaretaker.core.DataController
import com.androidessence.cashcaretaker.data.DataViewState

/**
 * View for displaying a list of transactions.
 */
interface TransactionController : DataController {
    var viewState: DataViewState
}