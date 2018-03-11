package com.androidessence.cashcaretaker.transfer

import com.androidessence.cashcaretaker.account.Account
import com.androidessence.cashcaretaker.data.CCRepository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.*

/**
 * Implementation of [AddTransferInteractor]
 */
class AddTransferInteractorImpl(private var controller: AddTransferController?) : AddTransferInteractor {
    override fun addTransfer(fromAccount: Account, toAccount: Account, amount: Double, date: Date) {
        CCRepository.transfer(fromAccount, toAccount, amount, date)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        {
                            onInserted()
                        },
                        {
                            onError(it)
                        }
                )
    }

    override fun onInserted() {
        controller?.hideProgress()
        controller?.onInserted()
    }

    override fun onError(error: Throwable) {
        controller?.hideProgress()
        controller?.onError(error)
    }
}