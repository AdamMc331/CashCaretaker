package com.adammcneilly.cashcaretaker.interactors

import android.database.sqlite.SQLiteConstraintException
import com.adammcneilly.cashcaretaker.App
import com.adammcneilly.cashcaretaker.data.CCDatabase
import com.adammcneilly.cashcaretaker.entities.Account
import io.reactivex.*
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import timber.log.Timber

/**
 * Implementation of [AddAccountInteractor].
 */
class AddAccountInteractorImpl: AddAccountInteractor {
    override fun insert(accountName: String, accountBalance: String, listener: AddAccountInteractor.OnInsertedListener) {
        if (accountName.isEmpty()) {
            listener.onAccountNameError()
            return
        }

        val balance = accountBalance.toDoubleOrNull()
        if (balance == null) {
            listener.onAccountBalanceError()
            return
        }

        val account = Account(accountName, balance)
        Single.fromCallable { CCDatabase.getInMemoryDatabase(App.instance)
                .accountDao()
                .insert(listOf(account)) }
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : SingleObserver<List<Long>> {
                    override fun onSuccess(t: List<Long>?) {
                        listener.onInserted(t!!)
                    }

                    override fun onSubscribe(d: Disposable?) {
                        Timber.d("onSubscribe For Inserting Account")
                    }

                    override fun onError(e: Throwable?) {
                        if (e is SQLiteConstraintException) {
                            listener.onInsertConflict()
                        } else {
                            throw(e!!)
                        }
                    }
                })
    }
}