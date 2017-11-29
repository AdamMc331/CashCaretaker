package com.androidessence.cashcaretaker.transaction

import com.androidessence.cashcaretaker.core.BasePresenter
import com.androidessence.cashcaretaker.data.DataViewState
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * Implementation of the presenter for transactions.
 */
class TransactionPresenterImpl(private var controller: TransactionController?, private val transactionInteractor: TransactionInteractor, private val accountName: String) : BasePresenter {

    override fun onDestroy() {
        controller = null
    }

    override fun onAttach() {
        controller?.showProgress()
        transactionInteractor.getForAccount(accountName)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        { controller?.viewState = DataViewState.ListSuccess(it) },
                        { controller?.viewState = DataViewState.Error(it) }
                )
    }
}