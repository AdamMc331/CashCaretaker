package com.androidessence.cashcaretaker.account

import com.androidessence.cashcaretaker.core.BasePresenter
import com.androidessence.cashcaretaker.data.DataViewState
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * Implementation of the presenter for accounts.
 *
 * @property[controller] The controller that should be modified as data is fetched.
 * @property[interactor] The interactor responsible for fetching account data.
 */
class AccountPresenterImpl(private var controller: AccountController?, private val interactor: AccountInteractor) : BasePresenter {

    override fun onAttach() {
        controller?.showProgress()
        interactor.getAll()
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