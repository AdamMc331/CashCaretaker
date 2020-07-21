package com.androidessence.cashcaretaker.ui.addtransfer

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.androidessence.cashcaretaker.R
import com.androidessence.cashcaretaker.core.models.Account
import com.androidessence.cashcaretaker.core.BaseViewModel
import com.androidessence.cashcaretaker.data.CCRepository
import java.util.Date
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class AddTransferViewModel(
    private val repository: CCRepository,
    private val transferInserted: (Boolean) -> Unit
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
                job = CoroutineScope(Dispatchers.IO).launch {
                    repository.transfer(fromAccount, toAccount, transferAmount, date)
                    transferInserted.invoke(true)
                }
            }
        }
    }
}
