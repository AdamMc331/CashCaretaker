package com.androidessence.cashcaretaker.addaccount

import android.arch.lifecycle.ViewModel
import android.database.sqlite.SQLiteConstraintException
import com.androidessence.cashcaretaker.account.Account
import com.androidessence.cashcaretaker.data.CCRepository
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import timber.log.Timber

class AddAccountViewModel(private val repository: CCRepository) : ViewModel() {
    private val compositeDisposable = CompositeDisposable()
    val accountInserted: PublishSubject<Long> = PublishSubject.create()
    val accountNameError: PublishSubject<String> = PublishSubject.create()
    val accountBalanceError: PublishSubject<String> = PublishSubject.create()

    /**
     * Checks that the information passed in is valid, and inserts an account if it is.
     *
     * TODO: String resources?
     */
    fun addAccount(name: String?, balanceString: String?) {
        if (name == null || name.isEmpty()) {
            accountNameError.onNext("Account name is invalid.")
            return
        }

        val balance = balanceString?.toDoubleOrNull()
        if (balance == null) {
            accountBalanceError.onNext("Account balance is invalid.")
            return
        }

        val account = Account(name, balance)

        val subscription = Single.fromCallable { repository.insertAccount(account) }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        accountInserted::onNext,
                        { error ->
                            if (error is SQLiteConstraintException) {
                                accountNameError.onNext("An account with this name already exists.")
                            } else {
                                Timber.e(error)
                            }
                        }
                )

        compositeDisposable.add(subscription)
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }
}