package com.androidessence.cashcaretaker.addaccount

import com.androidessence.cashcaretaker.account.Account

/**
 * Interface for adding an account in the database.
 */
interface AddAccountInteractor {

    /**
     * Inserts a list of accounts into the database.
     */
    fun insert(accounts: List<Account>): List<Long>
}