package com.androidessence.cashcaretaker.addaccount

import com.androidessence.cashcaretaker.account.Account
import com.androidessence.cashcaretaker.data.CCRepository

/**
 * Implementation of [AddAccountInteractor].
 */
class AddAccountInteractorImpl(private val repository: CCRepository) : AddAccountInteractor {
    override fun insert(accounts: List<Account>): List<Long> =
            repository.insertAccounts(accounts)
}