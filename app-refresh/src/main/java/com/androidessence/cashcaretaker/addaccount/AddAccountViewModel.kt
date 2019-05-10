package com.androidessence.cashcaretaker.addaccount

import android.database.sqlite.SQLiteConstraintException
import androidx.lifecycle.MutableLiveData
import com.androidessence.cashcaretaker.R
import com.androidessence.cashcaretaker.account.Account
import com.androidessence.cashcaretaker.base.BaseViewModel
import com.androidessence.cashcaretaker.data.CCRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AddAccountViewModel(
    private val repository: CCRepository,
    private val accountInserted: (Long) -> Unit
) : BaseViewModel() {
    val accountNameError = MutableLiveData<Int>()
    val accountBalanceError = MutableLiveData<Int>()

    /**
     * Checks that the information passed in is valid, and inserts an account if it is.
     */
    fun addAccount(name: String?, balanceString: String?) {
        if (name == null || name.isEmpty()) {
            accountNameError.value = R.string.err_account_name_invalid
            return
        }

        val balance = balanceString?.toDoubleOrNull()
        if (balance == null) {
            accountBalanceError.value = R.string.err_account_balance_invalid
            return
        }

        val account = Account(name, balance)

        job = CoroutineScope(Dispatchers.IO).launch {
            try {
                val accountId = repository.insertAccount(account)
                withContext(Dispatchers.Main) {
                    accountInserted.invoke(accountId)
                }
            } catch (constraintException: SQLiteConstraintException) {
                withContext(Dispatchers.Main) {
                    accountNameError.value = R.string.err_account_name_exists
                }
            }
        }
    }
}