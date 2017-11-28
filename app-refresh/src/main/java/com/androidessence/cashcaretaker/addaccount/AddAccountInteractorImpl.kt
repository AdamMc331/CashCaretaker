package com.androidessence.cashcaretaker.addaccount

import com.androidessence.cashcaretaker.App
import com.androidessence.cashcaretaker.data.CCDatabase
import com.androidessence.cashcaretaker.account.Account

/**
 * Implementation of [AddAccountInteractor].
 */
class AddAccountInteractorImpl: AddAccountInteractor {
    override fun insert(accounts: List<Account>): List<Long> {
        return CCDatabase.getInMemoryDatabase(App.instance)
                .accountDao()
                .insert(accounts)
    }
}