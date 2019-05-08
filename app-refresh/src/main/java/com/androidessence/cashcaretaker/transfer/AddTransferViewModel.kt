package com.androidessence.cashcaretaker.transfer

import androidx.lifecycle.MutableLiveData
import com.androidessence.cashcaretaker.R
import com.androidessence.cashcaretaker.account.Account
import com.androidessence.cashcaretaker.base.BaseViewModel
import com.androidessence.cashcaretaker.data.CCRepository
import com.androidessence.cashcaretaker.data.DataViewState
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
import java.util.Date

class AddTransferViewModel(
    private val repository: CCRepository,
    private val transferInserted: (Boolean) -> Unit
) : BaseViewModel() {
    val accounts = MutableLiveData<List<Account>>()
    val fromAccountError = MutableLiveData<Int>()
    val toAccountError = MutableLiveData<Int>()
    val amountError = MutableLiveData<Int>()

    fun getAccounts() {
        repository.getAllAccounts()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        { state ->
                            if (state is DataViewState.Success<*>) {
                                @Suppress("UNCHECKED_CAST")
                                (state.result as? List<Account>)?.let { accountList ->
                                    accounts.value = accountList
                                }
                            }
                        },
                        Timber::e
                )
                .addToComposite()
    }

    fun addTransfer(fromAccount: Account?, toAccount: Account?, amount: String, date: Date) {
        if (fromAccount == null) {
            fromAccountError.value = R.string.from_account_invalid
            return
        }

        if (toAccount == null) {
            toAccountError.value = R.string.to_account_invalid
            return
        }

        val transferAmount = amount.toDoubleOrNull()
        if (transferAmount == null) {
            amountError.value = R.string.amount_invalid
            return
        }

        repository.transfer(fromAccount, toAccount, transferAmount, date)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        { transferInserted.invoke(true) },
                        Timber::e
                )
                .addToComposite()
    }
}