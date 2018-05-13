package com.androidessence.cashcaretaker.addtransaction

import com.androidessence.cashcaretaker.R
import com.androidessence.cashcaretaker.base.BaseViewModel
import com.androidessence.cashcaretaker.data.CCRepository
import com.androidessence.cashcaretaker.transaction.Transaction
import com.crashlytics.android.Crashlytics
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import java.util.*

/**
 * ViewModel for adding a transaction. It exposes the insert/update events, as well as errors through
 * various [PublishSubject]s.
 *
 * @property[repository] A repository that is used to insert/update accounts.
 */
class AddTransactionViewModel(private val repository: CCRepository) : BaseViewModel() {
    val transactionInserted: PublishSubject<Long> = PublishSubject.create()
    val transactionUpdated: PublishSubject<Int> = PublishSubject.create()
    val transactionDescriptionError: PublishSubject<Int> = PublishSubject.create()
    val transactionAmountError: PublishSubject<Int> = PublishSubject.create()

    /**
     * Checks that the information passed in is valid, and inserts an account if it is.
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
        Single.fromCallable { repository.insertTransaction(transaction) }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        transactionInserted::onNext,
                        Crashlytics::logException
                )
                .addToComposite()
    }

    /**
     * Checks that the information passed in is valid, and updates the transaction if it is.
     */
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
        Single.fromCallable { repository.updateTransaction(transaction) }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        transactionUpdated::onNext,
                        Crashlytics::logException
                )
                .addToComposite()
    }
}