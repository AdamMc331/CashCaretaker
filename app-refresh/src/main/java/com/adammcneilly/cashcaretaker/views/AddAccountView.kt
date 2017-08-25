package com.adammcneilly.cashcaretaker.views

import com.adammcneilly.cashcaretaker.entities.Account

/**
 * View to add an account.
 */
interface AddAccountView: DataView {
    fun addAccount(accountName: String, accountBalance: String)

    fun onInsertConflict()

    fun showAccountNameError()

    fun showAccountBalanceError()

    fun onInserted(ids: List<Long>)
}