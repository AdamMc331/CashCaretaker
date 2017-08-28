package com.adammcneilly.cashcaretaker.addaccount

import com.adammcneilly.cashcaretaker.core.BasePresenter

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