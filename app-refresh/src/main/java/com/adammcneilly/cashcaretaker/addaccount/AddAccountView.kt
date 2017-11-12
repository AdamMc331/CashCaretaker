package com.adammcneilly.cashcaretaker.addaccount

import com.adammcneilly.cashcaretaker.core.DataController

/**
 * View to add an account.
 */
interface AddAccountView: DataController {
    fun addAccount(accountName: String, accountBalance: String)

    fun onInsertConflict()

    fun showAccountNameError()

    fun showAccountBalanceError()

    fun onInserted(ids: List<Long>)
}