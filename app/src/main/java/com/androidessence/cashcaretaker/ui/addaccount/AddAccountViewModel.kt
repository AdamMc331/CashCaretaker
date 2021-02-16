package com.androidessence.cashcaretaker.ui.addaccount

import android.database.sqlite.SQLiteConstraintException
import androidx.lifecycle.viewModelScope
import com.adammcneilly.cashcaretaker.analytics.AnalyticsTracker
import com.androidessence.cashcaretaker.R
import com.androidessence.cashcaretaker.core.BaseViewModel
import com.androidessence.cashcaretaker.core.models.Account
import com.androidessence.cashcaretaker.data.CCRepository
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class AddAccountViewModel(
    private val repository: CCRepository,
    private val analyticsTracker: AnalyticsTracker,
) : BaseViewModel() {
    private val _viewState: MutableStateFlow<AddAccountViewState> = MutableStateFlow(
        AddAccountViewState()
    )

    val viewState: StateFlow<AddAccountViewState> = _viewState

    private val dismissEventChannel: Channel<Boolean> = Channel()
    val dismissEvents: Flow<Boolean> = dismissEventChannel.receiveAsFlow()

    /**
     * Checks that the information passed in is valid, and inserts an account if it is.
     */
    fun addAccount(name: String?, balanceString: String?) {
        if (name == null || name.isEmpty()) {
            _viewState.value = _viewState.value.copy(
                accountNameErrorTextRes = R.string.err_account_name_invalid
            )
            return
        }

        val balance = balanceString?.toDoubleOrNull()
        if (balance == null) {
            _viewState.value = _viewState.value.copy(
                accountBalanceErrorTextRes = R.string.err_account_balance_invalid
            )
            return
        }

        val account = Account(name, balance)

        viewModelScope.launch {
            try {
                repository.insertAccount(account)
                analyticsTracker.trackAccountAdded()
                dismissEventChannel.send(true)
                dismissEventChannel.close()
            } catch (constraintException: SQLiteConstraintException) {
                _viewState.value = _viewState.value.copy(
                    accountNameErrorTextRes = R.string.err_account_name_exists
                )
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        dismissEventChannel.close()
    }
}
