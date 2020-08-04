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
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class AddTransferViewModel(
    private val repository: CCRepository,
    private val analyticsTracker: AnalyticsTracker
) : BaseViewModel() {
    private val _accounts: MutableLiveData<List<Account>> = MutableLiveData()
    val accounts: LiveData<List<Account>> = _accounts

    val fromAccountError = MutableLiveData<Int>()
    val toAccountError = MutableLiveData<Int>()
    val amountError = MutableLiveData<Int>()

    private val dismissEventChannel: Channel<Boolean> = Channel()
    val dismissEvents: Flow<Boolean> = dismissEventChannel.receiveAsFlow()

    init {
        viewModelScope.launch {
            repository.fetchAllAccounts().collect { accounts ->
                this@AddTransferViewModel._accounts.value = accounts
            }
        }
    }

    fun addTransfer(fromAccount: Account?, toAccount: Account?, amount: String, date: Date) {
        val transferAmount = amount.toDoubleOrNull()

        when {
            fromAccount == null -> {
                fromAccountError.value = R.string.from_account_invalid
            }
            toAccount == null -> {
                toAccountError.value = R.string.to_account_invalid
            }
            transferAmount == null -> {
                amountError.value = R.string.amount_invalid
            }
            else -> {
                viewModelScope.launch {
                    repository.transfer(fromAccount, toAccount, transferAmount, date)
                    analyticsTracker.trackTransferAdded()
                    dismissEventChannel.send(true)
                    dismissEventChannel.close()
                }
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        dismissEventChannel.close()
    }
}
