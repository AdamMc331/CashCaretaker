package com.androidessence.cashcaretaker.transfer

import com.androidessence.cashcaretaker.account.Account
import java.util.*

/**
 * Implementation of the transfer presentation layer.
 */
class AddTransferPresenterImpl(private var controller: AddTransferController?, private val interactor: AddTransferInteractor) : AddTransferPresenter {

    override fun onAttach() {
        //TODO:
    }

    override fun onDestroy() {
        controller = null
    }

    override fun addTransfer(fromAccount: Account?, toAccount: Account?, amount: String, date: Date) {
        if (fromAccount == null) {
            controller?.showFromAccountError()
            return
        }

        if (toAccount == null) {
            controller?.showToAccountError()
            return
        }

        val amountDouble = amount.toDoubleOrNull()
        if (amountDouble == null) {
            controller?.showAmountError()
            return
        }

        controller?.showProgress()
        interactor.addTransfer(fromAccount, toAccount, amountDouble, date)
    }

    override fun onInserted() {
        controller?.hideProgress()
        controller?.onInserted()
    }

    override fun onError() {
        controller?.hideProgress()
        controller?.onError()
    }
}