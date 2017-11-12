package com.adammcneilly.cashcaretaker.transaction

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * Implementation of the presenter for transactions.
 */
class TransactionPresenterImpl(transactionView: TransactionView, private val transactionInteractor: TransactionInteractor, private val accountName: String) : TransactionPresenter {
    private var transactionView: TransactionView? = transactionView
        private set

    override fun onDestroy() {
        transactionView = null
    }

    override fun onResume() {
        transactionView?.showProgress()
        transactionInteractor.getForAccount(accountName)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { onFetched(it) }
    }

    override fun onFetched(transactions: List<Transaction>) {
        transactionView?.hideProgress()
        transactionView?.setTransactions(transactions)
    }
}