package com.androidessence.cashcaretaker.addaccount

import com.androidessence.cashcaretaker.core.BasePresenter

/**
 * Presenter for adding an account.
 */
interface AddAccountPresenter: BasePresenter {
    fun onInserted(ids: List<Long>)
    fun onInsertConflict()
    fun onAccountNameError()
    fun onAccountBalanceError()

    fun insert(accountName: String, accountBalance: String)
}