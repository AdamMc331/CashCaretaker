package com.androidessence.cashcaretaker.addaccount

import android.database.sqlite.SQLiteConstraintException
import com.androidessence.cashcaretaker.R
import com.androidessence.cashcaretaker.account.Account
import com.androidessence.cashcaretaker.base.BaseViewModel
import com.androidessence.cashcaretaker.data.CCRepository
import com.crashlytics.android.Crashlytics
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject

class AddAccountViewModel(private val repository: CCRepository) : BaseViewModel() {
    val accountInserted: PublishSubject<Long> = PublishSubject.create()
    val accountNameError: PublishSubject<Int> = PublishSubject.create()
    val accountBalanceError: PublishSubject<Int> = PublishSubject.create()

    /**
     * Checks that the information passed in is valid, and inserts an account if it is.
     *
     * TODO: String resources?
     */
    fun addAccount(name: String?, balanceString: String?) {
        if (name == null || name.isEmpty()) {
            accountNameError.onNext(R.string.err_account_name_invalid)
            return
        }

        val balance = balanceString?.toDoubleOrNull()
        if (balance == null) {
            accountBalanceError.onNext(R.string.err_account_balance_invalid)
            return
        }

        val account = Account(name, balance)

        Single.fromCallable { repository.insertAccount(account) }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        accountInserted::onNext,
                        { error ->
                            if (error is SQLiteConstraintException) {
                                accountNameError.onNext(R.string.err_account_name_exists)
                            } else {
                                Crashlytics.logException(error)
                            }
                        }
                )
                .addToComposite()
    }
}