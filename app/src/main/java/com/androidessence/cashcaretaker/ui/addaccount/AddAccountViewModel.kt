package com.androidessence.cashcaretaker.ui.addaccount

import android.database.sqlite.SQLiteConstraintException
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.androidessence.cashcaretaker.R
import com.androidessence.cashcaretaker.core.BaseViewModel
import com.androidessence.cashcaretaker.core.models.Account
import com.androidessence.cashcaretaker.data.CCRepository
import com.androidessence.cashcaretaker.data.analytics.AnalyticsTracker
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class AddAccountViewModel(
    private val repository: CCRepository,
    private val analyticsTracker: AnalyticsTracker
) : BaseViewModel() {
    val accountNameError = MutableLiveData<Int>()
    val accountBalanceError = MutableLiveData<Int>()

    private val dismissEventChannel: Channel<Boolean> = Channel()
    val dismissEvents: Flow<Boolean> = dismissEventChannel.receiveAsFlow()

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

        viewModelScope.launch {
            try {
                repository.insertAccount(account)
                analyticsTracker.trackAccountAdded()
                dismissEventChannel.send(true)
            } catch (constraintException: SQLiteConstraintException) {
                accountNameError.value = R.string.err_account_name_exists
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        dismissEventChannel.close()
    }
}
