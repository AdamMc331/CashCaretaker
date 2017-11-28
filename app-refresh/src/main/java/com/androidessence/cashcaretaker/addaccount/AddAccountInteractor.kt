package com.androidessence.cashcaretaker.addaccount

import com.androidessence.cashcaretaker.account.Account

/**
 * Interface for adding an account in the database.
 */
interface AddAccountInteractor {
    fun insert(accounts: List<Account>): List<Long>
}