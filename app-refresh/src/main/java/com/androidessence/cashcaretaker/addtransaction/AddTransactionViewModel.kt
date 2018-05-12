package com.androidessence.cashcaretaker.addtransaction

import android.arch.lifecycle.ViewModel
import com.androidessence.cashcaretaker.R
import com.androidessence.cashcaretaker.data.CCRepository
import com.androidessence.cashcaretaker.transaction.Transaction
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import timber.log.Timber
import java.util.*

class AddTransactionViewModel(private val repository: CCRepository) : ViewModel() {
    private val compositeDisposable = CompositeDisposable()
    val transactionInserted: PublishSubject<Long> = PublishSubject.create()
    val transactionUpdated: PublishSubject<Int> = PublishSubject.create()
    val transactionDescriptionError: PublishSubject<Int> = PublishSubject.create()
    val transactionAmountError: PublishSubject<Int> = PublishSubject.create()

    /**
     * Checks that the information passed in is valid, and inserts an account if it is.
     *
     * TODO: String resources?
     */
    fun addTransaction(accountName: String, transactionDescription: String, transactionAmount: String, withdrawal: Boolean, date: Date) {
        if (transactionDescription.isEmpty()) {
            transactionDescriptionError.onNext(R.string.error_invalid_description)
            return
        }

        val amount = transactionAmount.toDoubleOrNull()
        if (amount == null) {
            transactionAmountError.onNext(R.string.error_invalid_amount)
            return
        }

        val transaction = Transaction(accountName, transactionDescription, amount, withdrawal, date)
        val subscription = Single.fromCallable { repository.insertTransaction(transaction) }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        transactionInserted::onNext,
                        Timber::e
                )

        compositeDisposable.add(subscription)
    }

    fun updateTransaction(id: Long, accountName: String, transactionDescription: String, transactionAmount: String, withdrawal: Boolean, date: Date) {
        if (transactionDescription.isEmpty()) {
            transactionDescriptionError.onNext(R.string.error_invalid_description)
            return
        }

        val amount = transactionAmount.toDoubleOrNull()
        if (amount == null) {
            transactionAmountError.onNext(R.string.error_invalid_amount)
            return
        }

        val transaction = Transaction(accountName, transactionDescription, amount, withdrawal, date, id)
        val subscription = Single.fromCallable { repository.updateTransaction(transaction) }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        transactionUpdated::onNext,
                        Timber::e
                )

        compositeDisposable.add(subscription)
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }
}