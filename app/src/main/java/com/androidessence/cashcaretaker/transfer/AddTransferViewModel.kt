package com.androidessence.cashcaretaker.transfer

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.androidessence.cashcaretaker.R
import com.androidessence.cashcaretaker.account.Account
import com.androidessence.cashcaretaker.base.BaseViewModel
import com.androidessence.cashcaretaker.data.CCRepository
import java.util.Date
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AddTransferViewModel(
    private val repository: CCRepository,
    private val transferInserted: (Boolean) -> Unit
) : BaseViewModel() {
    val accounts: LiveData<List<Account>> = Transformations.map(repository.getAllAccounts()) {
        when {
            it.isNotEmpty() -> it
            else -> null
        }
    }
    val fromAccountError = MutableLiveData<Int>()
    val toAccountError = MutableLiveData<Int>()
    val amountError = MutableLiveData<Int>()

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
