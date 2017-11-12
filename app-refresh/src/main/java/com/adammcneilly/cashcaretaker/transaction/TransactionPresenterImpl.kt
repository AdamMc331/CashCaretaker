package com.adammcneilly.cashcaretaker.transaction

import com.adammcneilly.cashcaretaker.entity.EntityPresenter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * Implementation of the presenter for transactions.
 */
class TransactionPresenterImpl(private var transactionController: TransactionController?, private val transactionInteractor: TransactionInteractor, private val accountName: String) : EntityPresenter<Transaction> {

    override fun onDestroy() {
        transactionController = null
    }

    override fun onAttach() {
        transactionController?.showProgress()
        transactionInteractor.getForAccount(accountName)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { onFetched(it) }
    }

    override fun onFetched(entities: List<Transaction>) {
        transactionController?.hideProgress()
        transactionController?.setTransactions(entities)
    }
}