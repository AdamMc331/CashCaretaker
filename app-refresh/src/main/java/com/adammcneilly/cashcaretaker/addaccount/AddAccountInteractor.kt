package com.adammcneilly.cashcaretaker.addaccount

import com.adammcneilly.cashcaretaker.account.Account

/**
 * Interface for adding an account in the database.
 */
interface AddAccountInteractor {
    fun insert(accounts: List<Account>): List<Long>
}