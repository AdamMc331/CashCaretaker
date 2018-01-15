package com.androidessence.cashcaretaker.account

import com.androidessence.cashcaretaker.data.CCRepository
import io.reactivex.Flowable

/**
 * Handles the actual logic behind account fetching.
 */
class AccountInteractorImpl : AccountInteractor {

    override fun getAll(): Flowable<List<Account>> = CCRepository.getAllAccounts()

    override fun delete(account: Account): Int = CCRepository.deleteAccount(account)
}