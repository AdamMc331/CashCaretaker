package com.androidessence.cashcaretaker.account

import com.androidessence.cashcaretaker.App
import com.androidessence.cashcaretaker.data.CCDatabase
import io.reactivex.Flowable

/**
 * Handles the actual logic behind account fetching.
 */
class AccountInteractorImpl : AccountInteractor {

    override fun getAll(): Flowable<List<Account>> {
        return CCDatabase.getInMemoryDatabase(App.instance)
                .accountDao()
                .getAll()
    }
}