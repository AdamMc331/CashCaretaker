package com.androidessence.cashcaretaker.ui.addtransaction

import androidx.lifecycle.MutableLiveData
import com.androidessence.cashcaretaker.R
import com.androidessence.cashcaretaker.core.BaseViewModel
import com.androidessence.cashcaretaker.core.models.Transaction
import com.androidessence.cashcaretaker.data.CCRepository
import com.androidessence.cashcaretaker.data.analytics.AnalyticsTracker
import java.util.Date
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * ViewModel for adding a transaction.
 *
 * @property[repository] A repository that is used to insert/update accounts.
 */
class AddTransactionViewModel(
    private val repository: CCRepository,
    private val transactionInserted: (Long) -> Unit,
    private val transactionUpdated: (Int) -> Unit,
    private val analyticsTracker: AnalyticsTracker
) : BaseViewModel() {
    val transactionDescriptionError = MutableLiveData<Int>()
    val transactionAmountError = MutableLiveData<Int>()

    /**
     * Checks that the information passed in is valid, and inserts an account if it is.
     */
    fun addTransaction(
        accountName: String,
        transactionDescription: String,
        transactionAmount: String,
        withdrawal: Boolean,
        date: Date
    ) {
        val amount = transactionAmount.toDoubleOrNull()
        if (amount == null) {
            transactionAmountError.value = R.string.error_invalid_amount
            return
        }

        val transaction = Transaction(accountName, transactionDescription, amount, withdrawal, date)

        job = CoroutineScope(Dispatchers.IO).launch {
            val transactionId = repository.insertTransaction(transaction)
            transactionInserted.invoke(transactionId)
            analyticsTracker.trackTransactionAdded()
        }
    }

    /**
     * Checks that the information passed in is valid, and updates the transaction if it is.
     */
    fun updateTransaction(
        input: TransactionInput
    ) {
        val amount = input.transactionAmount.toDoubleOrNull()
        if (amount == null) {
            transactionAmountError.value = R.string.error_invalid_amount
            return
        }

        val transaction = Transaction(
            input.accountName,
            input.transactionDescription,
            amount,
            input.withdrawal,
            input.date,
            input.id
        )

        job = CoroutineScope(Dispatchers.IO).launch {
            val updatedCount = repository.updateTransaction(transaction)
            transactionUpdated.invoke(updatedCount)
            analyticsTracker.trackTransactionEdited()
        }
    }

    /**
     * Data class defining all of the input required for updating a Transaction.
     */
    data class TransactionInput(
        val id: Long,
        val accountName: String,
        val transactionDescription: String,
        val transactionAmount: String,
        val withdrawal: Boolean,
        val date: Date
    )
}
