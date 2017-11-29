package com.androidessence.cashcaretaker.addaccount

import com.androidessence.cashcaretaker.core.BasePresenter

/**
 * Presenter for adding an account.
 */
interface AddAccountPresenter: BasePresenter {

    /**
     * Callback for the insertion of a list of accounts.
     */
    fun onInserted(ids: List<Long>)

    /**
     * Callback when an account is not inserted due to a name conflict.
     */
    fun onInsertConflict()

    /**
     * Callback when an account name is invalid.
     */
    fun onAccountNameError()

    /**
     * Callback when an account balance is invalid.
     */
    fun onAccountBalanceError()

    /**
     * Inserts an account into the database.
     *
     * @param[accountName] The name of the account to be inserted.
     * @param[accountBalance] The starting balance of the account to be inserted.
     */
    fun insert(accountName: String, accountBalance: String)
}