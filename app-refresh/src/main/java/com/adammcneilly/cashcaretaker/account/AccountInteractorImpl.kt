package com.adammcneilly.cashcaretaker.account

import com.adammcneilly.cashcaretaker.App
import com.adammcneilly.cashcaretaker.data.CCDatabase
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