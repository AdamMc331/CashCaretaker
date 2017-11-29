package com.androidessence.cashcaretaker.account

import com.androidessence.cashcaretaker.core.BasePresenter
import com.androidessence.cashcaretaker.data.DataViewState
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * Implementation of the presenter for accounts.
 */
class AccountPresenterImpl(private var controller: AccountController?, private val accountInteractor: AccountInteractor) : BasePresenter {

    override fun onAttach() {
        controller?.showProgress()
        accountInteractor.getAll()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        { controller?.viewState = DataViewState.ListSuccess(it) },
                        { controller?.viewState = DataViewState.Error(it) }
                )
    }

    override fun onDestroy() {
        controller = null
    }
}