package com.androidessence.cashcaretaker.ui.addtransaction

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.androidessence.cashcaretaker.R
import com.androidessence.cashcaretaker.core.BaseViewModel
import com.androidessence.cashcaretaker.core.models.Transaction
import com.androidessence.cashcaretaker.data.CCRepository
import com.androidessence.cashcaretaker.data.analytics.AnalyticsTracker
import java.util.Date
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

/**
 * ViewModel for adding a transaction.
 *
 * @property[repository] A repository that is used to insert/update accounts.
 */
class AddTransactionViewModel(
    private val repository: CCRepository,
    private val analyticsTracker: AnalyticsTracker
) : BaseViewModel() {
    val transactionDescriptionError = MutableLiveData<Int>()
    val transactionAmountError = MutableLiveData<Int>()

    private val dismissEventChannel: Channel<Boolean> = Channel()
    val dismissEvents: Flow<Boolean> = dismissEventChannel.receiveAsFlow()

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

        viewModelScope.launch(Dispatchers.IO) {
            repository.insertTransaction(transaction)
            analyticsTracker.trackTransactionAdded()
            dismissEventChannel.send(true)
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

        viewModelScope.launch(Dispatchers.IO) {
            repository.updateTransaction(transaction)
            analyticsTracker.trackTransactionEdited()
            dismissEventChannel.send(true)
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
