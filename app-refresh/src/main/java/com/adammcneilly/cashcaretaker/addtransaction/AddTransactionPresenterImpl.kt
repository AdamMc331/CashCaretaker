package com.adammcneilly.cashcaretaker.addtransaction

import com.adammcneilly.cashcaretaker.transaction.Transaction
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
import java.util.*

/**
 * Implementation for the AddTransactionPresenter.
 */
class AddTransactionPresenterImpl(private var controller: AddTransactionView?, private val interactor: AddTransactionInteractor) : AddTransactionPresenter {
    override fun onAttach() {
        //TODO: Not used, refactor later?
    }

    override fun onDestroy() {
        controller = null
    }

    override fun onInserted(ids: List<Long>) {
        controller?.hideProgress()
        controller?.onInserted(ids)
    }

    override fun onTransactionDescriptionError() {
        controller?.hideProgress()
        controller?.showTransactionDescriptionError()
    }

    override fun onTransactionAmountError() {
        controller?.hideProgress()
        controller?.showTransactionAmountError()
    }

    override fun insert(accountName: String, transactionDescription: String, transactionAmount: String, withdrawal: Boolean, date: Date) {
        if (transactionDescription.isEmpty()) {
            onTransactionDescriptionError()
            return
        }

        val amount = transactionAmount.toDoubleOrNull()
        if (amount == null) {
            onTransactionAmountError()
            return
        }

        controller?.showProgress()

        val transaction = Transaction(accountName, transactionDescription, amount, withdrawal, date)
        Single.fromCallable { interactor.insert(listOf(transaction)) }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        { items -> onInserted(items) },
                        { error -> Timber.e(error) }
                )
    }
}