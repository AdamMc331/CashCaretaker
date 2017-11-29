package com.androidessence.cashcaretaker.account

import com.androidessence.cashcaretaker.core.BasePresenter
import com.androidessence.cashcaretaker.data.DataViewState
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * Implementation of the presenter for accounts.
 */
class AccountPresenterImpl(private var accountView: AccountController?, private val accountInteractor: AccountInteractor) : BasePresenter {

    override fun onAttach() {
        accountView?.showProgress()
        accountInteractor.getAll()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        { accountView?.viewState = DataViewState.ListSuccess(it) },
                        { accountView?.viewState = DataViewState.Error(it) }
                )
    }

    override fun onDestroy() {
        accountView = null
    }
}