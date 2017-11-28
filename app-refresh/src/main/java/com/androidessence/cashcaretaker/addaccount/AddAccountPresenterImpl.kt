package com.androidessence.cashcaretaker.addaccount

import android.database.sqlite.SQLiteConstraintException
import com.androidessence.cashcaretaker.account.Account
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * Implementation of [AddAccountPresenter]
 */
class AddAccountPresenterImpl(private var addAccountView: AddAccountView?, private val interactor: AddAccountInteractor) : AddAccountPresenter {

    override fun onAttach() {
        //TODO: Not implemented here, refactor later?
    }

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
                .subscribe(
                        { items -> onInserted(items) },
                        { error -> if (error is SQLiteConstraintException) onInsertConflict() else throw(error) }
                )
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