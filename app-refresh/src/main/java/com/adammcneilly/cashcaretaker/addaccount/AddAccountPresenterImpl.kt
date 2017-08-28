package com.adammcneilly.cashcaretaker.addaccount

import android.database.sqlite.SQLiteConstraintException
import com.adammcneilly.cashcaretaker.account.Account
import io.reactivex.Single
import io.reactivex.SingleObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import timber.log.Timber

/**
 * Implementation of [AddAccountPresenter]
 */
class AddAccountPresenterImpl(addAccountView: AddAccountView, private val interactor: AddAccountInteractor): AddAccountPresenter {
    private var addAccountView: AddAccountView? = addAccountView
        private set

    override fun onDestroy() {
        addAccountView = null
    }

    override fun insert(accountName: String, accountBalance: String) {
        if (accountName.isEmpty()) {
            onAccountNameError()
            return
        }

        val balance = accountBalance.toDoubleOrNull()
        if (balance == null) {
            onAccountBalanceError()
            return
        }

        addAccountView?.showProgress()

        val account = Account(accountName, balance)
        Single.fromCallable { interactor.insert(listOf(account)) }
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : SingleObserver<List<Long>> {
                    override fun onSuccess(t: List<Long>?) {
                        onInserted(t!!)
                    }

                    override fun onSubscribe(d: Disposable?) {
                        Timber.d("onSubscribe For Inserting Account")
                    }

                    override fun onError(e: Throwable?) {
                        if (e is SQLiteConstraintException) {
                            onInsertConflict()
                        } else {
                            throw(e!!)
                        }
                    }
                })
    }

    override fun onInserted(ids: List<Long>) {
        addAccountView?.onInserted(ids)
        addAccountView?.hideProgress()
    }

    override fun onInsertConflict() {
        addAccountView?.onInsertConflict()
        addAccountView?.hideProgress()
    }

    override fun onAccountNameError() {
        addAccountView?.showAccountNameError()
        addAccountView?.hideProgress()
    }

    override fun onAccountBalanceError() {
        addAccountView?.showAccountBalanceError()
        addAccountView?.hideProgress()
    }
}