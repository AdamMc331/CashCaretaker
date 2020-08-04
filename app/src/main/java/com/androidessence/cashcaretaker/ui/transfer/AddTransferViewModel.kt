package com.androidessence.cashcaretaker.ui.transfer

import androidx.lifecycle.viewModelScope
import com.adammcneilly.cashcaretaker.analytics.AnalyticsTracker
import com.androidessence.cashcaretaker.R
import com.androidessence.cashcaretaker.core.BaseViewModel
import com.androidessence.cashcaretaker.core.models.Account
import com.androidessence.cashcaretaker.data.CCRepository
import java.util.Date
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

@ExperimentalCoroutinesApi
class AddTransferViewModel(
    private val repository: CCRepository,
    private val analyticsTracker: AnalyticsTracker
) : BaseViewModel() {
    private val _viewState: MutableStateFlow<AddTransferViewState> =
        MutableStateFlow(AddTransferViewState())

    val viewState: StateFlow<AddTransferViewState> = _viewState

    private val dismissEventChannel: Channel<Boolean> = Channel()
    val dismissEvents: Flow<Boolean> = dismissEventChannel.receiveAsFlow()

    init {
        viewModelScope.launch {
            repository.fetchAllAccounts().collect { accounts ->
                _viewState.value = _viewState.value.copy(accounts = accounts)
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

            _viewState.value = _viewState.value.copy(
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
            dismissEventChannel.close()
        }
    }

    override fun onCleared() {
        super.onCleared()
        dismissEventChannel.close()
    }
}
