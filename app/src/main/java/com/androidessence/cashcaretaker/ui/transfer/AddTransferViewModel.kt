package com.androidessence.cashcaretaker.ui.transfer

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.androidessence.cashcaretaker.R
import com.androidessence.cashcaretaker.core.BaseViewModel
import com.androidessence.cashcaretaker.core.models.Account
import com.androidessence.cashcaretaker.data.CCRepository
import com.androidessence.cashcaretaker.data.analytics.AnalyticsTracker
import java.util.Date
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class AddTransferViewModel(
    private val repository: CCRepository,
    private val transferInserted: (Boolean) -> Unit,
    private val analyticsTracker: AnalyticsTracker
) : BaseViewModel() {
    private val _accounts: MutableLiveData<List<Account>> = MutableLiveData()
    val accounts: LiveData<List<Account>> = _accounts

    val fromAccountError = MutableLiveData<Int>()
    val toAccountError = MutableLiveData<Int>()
    val amountError = MutableLiveData<Int>()

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
                viewModelScope.launch(Dispatchers.IO) {
                    repository.transfer(fromAccount, toAccount, transferAmount, date)
                    analyticsTracker.trackTransferAdded()
                    transferInserted.invoke(true)
                }
            }
        }
    }
}
