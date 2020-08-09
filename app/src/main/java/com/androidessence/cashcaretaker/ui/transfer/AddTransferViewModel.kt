package com.androidessence.cashcaretaker.ui.transfer

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.adammcneilly.cashcaretaker.analytics.AnalyticsTracker
import com.androidessence.cashcaretaker.R
import com.androidessence.cashcaretaker.core.BaseViewModel
import com.androidessence.cashcaretaker.core.models.Account
import com.androidessence.cashcaretaker.data.CCRepository
import java.util.Date
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class AddTransferViewModel(
    private val repository: CCRepository,
    private val analyticsTracker: AnalyticsTracker
) : BaseViewModel() {
    private val _viewState: MutableLiveData<AddTransferViewState> = MutableLiveData()
    val viewState: LiveData<AddTransferViewState> = _viewState

    val dismissEventChannel: Channel<Boolean> = Channel()

    private val currentState: AddTransferViewState
        get() = _viewState.value ?: AddTransferViewState()

    init {
        viewModelScope.launch {
            repository.fetchAllAccounts().collect { accounts ->
                _viewState.value = currentState.copy(accounts = accounts)
            }
        }
    }

    fun addTransfer(fromAccount: Account?, toAccount: Account?, amount: String, date: Date) {
        val transferAmount = amount.toDoubleOrNull()

        if (fromAccount != null && toAccount != null && transferAmount != null) {
            insertValidatedTransfer(fromAccount, toAccount, transferAmount, date)
        } else {
            val fromAccountError = if (fromAccount == null) R.string.from_account_invalid else null
            val toAccountError = if (toAccount == null) R.string.to_account_invalid else null
            val amountError = if (transferAmount == null) R.string.amount_invalid else null

            _viewState.value = currentState.copy(
                fromAccountErrorRes = fromAccountError,
                toAccountErrorRes = toAccountError,
                amountErrorRes = amountError
            )
        }
    }

    /**
     * This will insert a transfer and emit a dismissal event. This should be called after the
     * supplies transfer information has been validated.
     */
    private fun insertValidatedTransfer(
        fromAccount: Account,
        toAccount: Account,
        transferAmount: Double,
        date: Date
    ) {
        viewModelScope.launch {
            repository.transfer(fromAccount, toAccount, transferAmount, date)
            analyticsTracker.trackTransferAdded()
            dismissEventChannel.send(true)
        }
    }

    override fun onCleared() {
        super.onCleared()
        dismissEventChannel.close()
    }
}
