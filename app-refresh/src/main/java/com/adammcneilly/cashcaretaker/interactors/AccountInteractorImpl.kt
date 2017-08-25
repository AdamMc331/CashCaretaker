package com.adammcneilly.cashcaretaker.interactors

import com.adammcneilly.cashcaretaker.App
import com.adammcneilly.cashcaretaker.data.CCDatabase
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * Handles the actual logic behind account fetching.
 */
class AccountInteractorImpl : AccountInteractor {
    override fun getAll(listener: AccountInteractor.OnFinishedListener) {
        CCDatabase.getInMemoryDatabase(App.instance)
                .accountDao()
                .getAll()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ listener.onFetched(it) })
    }
}