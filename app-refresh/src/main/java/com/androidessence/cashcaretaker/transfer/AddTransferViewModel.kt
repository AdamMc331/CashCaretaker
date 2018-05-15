package com.androidessence.cashcaretaker.transfer

import com.androidessence.cashcaretaker.account.Account
import com.androidessence.cashcaretaker.base.BaseViewModel
import com.androidessence.cashcaretaker.data.CCRepository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import timber.log.Timber
import java.util.*

class AddTransferViewModel(private val repository: CCRepository) : BaseViewModel() {
    val accounts: PublishSubject<List<Account>> = PublishSubject.create()
    val fromAccountError: PublishSubject<String> = PublishSubject.create()
    val toAccountError: PublishSubject<String> = PublishSubject.create()
    val amountError: PublishSubject<String> = PublishSubject.create()
    val transferInserted: PublishSubject<Boolean> = PublishSubject.create()

    fun getAccounts() {
        repository.getAllAccounts()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        accounts::onNext,
                        Timber::e
                )
                .addToComposite()
    }

    fun addTransfer(fromAccount: Account?, toAccount: Account?, amount: String, date: Date) {
        if (fromAccount == null) {
            fromAccountError.onNext("From account is invalid.")
            return
        }

        if (toAccount == null) {
            toAccountError.onNext("To account is invalid.")
            return
        }

        val transferAmount = amount.toDoubleOrNull()
        if (transferAmount == null) {
            amountError.onNext("Amount is invalid.")
            return
        }

        repository.transfer(fromAccount, toAccount, transferAmount, date)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        { transferInserted.onNext(true) },
                        Timber::e
                )
                .addToComposite()
    }
}