package com.androidessence.cashcaretaker.account

import com.androidessence.cashcaretaker.core.DataController
import com.androidessence.cashcaretaker.data.DataViewState

/**
 * View for displaying a list of accounts.
 */
interface AccountController : DataController {
    var viewState: DataViewState
    fun onTransactionButtonClicked(account: Account, withdrawal: Boolean)
    fun onAccountSelected(account: Account)
}