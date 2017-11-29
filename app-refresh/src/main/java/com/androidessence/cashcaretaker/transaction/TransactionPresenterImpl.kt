package com.androidessence.cashcaretaker.transaction

import com.androidessence.cashcaretaker.core.BasePresenter
import com.androidessence.cashcaretaker.data.DataViewState
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * Implementation of the presenter for transactions.
 */
class TransactionPresenterImpl(private var transactionController: TransactionController?, private val transactionInteractor: TransactionInteractor, private val accountName: String) : BasePresenter {

    override fun onDestroy() {
        transactionController = null
    }

    override fun onAttach() {
        transactionController?.showProgress()
        transactionInteractor.getForAccount(accountName)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        { transactionController?.viewState = DataViewState.ListSuccess(it) },
                        { transactionController?.viewState = DataViewState.Error(it) }
                )
    }
}