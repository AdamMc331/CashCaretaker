package com.androidessence.cashcaretaker.addaccount

import android.database.sqlite.SQLiteConstraintException
import com.androidessence.cashcaretaker.account.Account
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * Implementation of [AddAccountPresenter]
 */
class AddAccountPresenterImpl(private var controller: AddAccountController?, private val interactor: AddAccountInteractor) : AddAccountPresenter {

    override fun onAttach() {
        //TODO: Not implemented here, refactor later?
    }

    override fun onDestroy() {
        controller = null
    }

    override fun insert(accountName: String, accountBalance: String) {
        var hasError = false

        if (accountName.isEmpty()) {
            onAccountNameError()
            hasError = true
        }

        val balance = accountBalance.toDoubleOrNull()
        if (balance == null) {
            onAccountBalanceError()
            hasError = true
        }

        if (hasError) return

        controller?.showProgress()

        // We can unwrap balance here because we return if it's null.
        val account = Account(accountName, balance!!)
        Single.fromCallable { interactor.insert(listOf(account)) }
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        { items -> onInserted(items) },
                        { error -> if (error is SQLiteConstraintException) onInsertConflict() else throw(error) }
                )
    }

    override fun onInserted(ids: List<Long>) {
        controller?.onInserted(ids)
        controller?.hideProgress()
    }

    override fun onInsertConflict() {
        controller?.onInsertConflict()
        controller?.hideProgress()
    }

    override fun onAccountNameError() {
        controller?.showAccountNameError()
        controller?.hideProgress()
    }

    override fun onAccountBalanceError() {
        controller?.showAccountBalanceError()
        controller?.hideProgress()
    }
}