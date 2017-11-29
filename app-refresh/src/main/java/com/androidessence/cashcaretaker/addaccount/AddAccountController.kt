package com.androidessence.cashcaretaker.addaccount

import com.androidessence.cashcaretaker.core.DataController

/**
 * Controller that handles the view for adding an account.
 */
interface AddAccountController : DataController {
    /**
     * Insert the account into the database.
     *
     * @param[accountName] The name of the account to be inserted.
     * @param[accountBalance] The starting balance of the account to be inserted.
     */
    fun addAccount(accountName: String, accountBalance: String)

    /**
     * Handles the specific error case where an account can't be inserted due to a conflict
     * in the name.
     */
    fun onInsertConflict()

    /**
     * Displays an error to the user when the account name is invalid.
     */
    fun showAccountNameError()

    /**
     * Displays an error to the user when the starting balance is invalid.
     */
    fun showAccountBalanceError()

    /**
     * Callback of the identifiers of the accounts that were inserted.
     */
    fun onInserted(ids: List<Long>)
}