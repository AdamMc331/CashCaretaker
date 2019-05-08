package com.androidessence.cashcaretaker.addtransaction

import androidx.lifecycle.MutableLiveData
import com.androidessence.cashcaretaker.R
import com.androidessence.cashcaretaker.base.BaseViewModel
import com.androidessence.cashcaretaker.data.CCRepository
import com.androidessence.cashcaretaker.transaction.Transaction
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
import java.util.Date

/**
 * ViewModel for adding a transaction.
 *
 * @property[repository] A repository that is used to insert/update accounts.
 */
class AddTransactionViewModel(
    private val repository: CCRepository,
    private val transactionInserted: (Long) -> Unit,
    private val transactionUpdated: (Int) -> Unit
) : BaseViewModel() {
    val transactionDescriptionError = MutableLiveData<Int>()
    val transactionAmountError = MutableLiveData<Int>()

    /**
     * Checks that the information passed in is valid, and inserts an account if it is.
     */
    fun addTransaction(accountName: String, transactionDescription: String, transactionAmount: String, withdrawal: Boolean, date: Date) {
        val amount = transactionAmount.toDoubleOrNull()
        if (amount == null) {
            transactionAmountError.value = R.string.error_invalid_amount
            return
        }

        val transaction = Transaction(accountName, transactionDescription, amount, withdrawal, date)
        Single.fromCallable { repository.insertTransaction(transaction) }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        transactionInserted::invoke,
                        Timber::e
                )
                .addToComposite()
    }

    /**
     * Checks that the information passed in is valid, and updates the transaction if it is.
     */
    fun updateTransaction(id: Long, accountName: String, transactionDescription: String, transactionAmount: String, withdrawal: Boolean, date: Date) {
        val amount = transactionAmount.toDoubleOrNull()
        if (amount == null) {
            transactionAmountError.value = R.string.error_invalid_amount
            return
        }

        val transaction = Transaction(accountName, transactionDescription, amount, withdrawal, date, id)
        Single.fromCallable { repository.updateTransaction(transaction) }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        transactionUpdated::invoke,
                        Timber::e
                )
                .addToComposite()
    }
}