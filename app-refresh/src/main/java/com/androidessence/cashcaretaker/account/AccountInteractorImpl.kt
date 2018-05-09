package com.androidessence.cashcaretaker.account

import com.androidessence.cashcaretaker.data.CCRepository
import io.reactivex.Flowable

/**
 * Handles the actual logic behind account fetching.
 */
class AccountInteractorImpl(private val repository: CCRepository) : AccountInteractor {

    override fun getAll(): Flowable<List<Account>> = repository.getAllAccounts()

    override fun delete(account: Account): Int = repository.deleteAccount(account)
}