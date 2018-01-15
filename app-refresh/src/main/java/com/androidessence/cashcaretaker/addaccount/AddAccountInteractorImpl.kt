package com.androidessence.cashcaretaker.addaccount

import com.androidessence.cashcaretaker.account.Account
import com.androidessence.cashcaretaker.data.CCRepository

/**
 * Implementation of [AddAccountInteractor].
 */
class AddAccountInteractorImpl : AddAccountInteractor {
    override fun insert(accounts: List<Account>): List<Long> =
            CCRepository.insertAccounts(accounts)
}