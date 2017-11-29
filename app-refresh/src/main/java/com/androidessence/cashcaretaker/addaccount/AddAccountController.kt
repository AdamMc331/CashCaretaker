package com.androidessence.cashcaretaker.addaccount

import com.androidessence.cashcaretaker.core.DataController

/**
 * View to add an account.
 */
interface AddAccountController : DataController {
    fun addAccount(accountName: String, accountBalance: String)

    fun onInsertConflict()

    fun showAccountNameError()

    fun showAccountBalanceError()

    fun onInserted(ids: List<Long>)
}