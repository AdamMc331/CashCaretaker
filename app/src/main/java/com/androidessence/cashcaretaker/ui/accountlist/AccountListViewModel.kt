package com.androidessence.cashcaretaker.ui.accountlist

import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.view.ActionMode
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.viewModelScope
import com.adammcneilly.cashcaretaker.analytics.AnalyticsTracker
import com.androidessence.cashcaretaker.R
import com.androidessence.cashcaretaker.core.BaseViewModel
import com.androidessence.cashcaretaker.core.models.Account
import com.androidessence.cashcaretaker.data.CCRepository
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

/**
 * LifeCycle aware class that fetches accounts from the database and exposes them through the [_state].
 */
class AccountListViewModel(
    private val repository: CCRepository,
    private val analyticsTracker: AnalyticsTracker
) : BaseViewModel() {
    private val _state: MutableLiveData<AccountListState> = MutableLiveData()
    val state: LiveData<AccountListState> = _state

    val accounts: LiveData<List<Account>> = Transformations.map(_state) { state ->
        state?.data.orEmpty()
    }

    val allowTransfers: Boolean
        get() {
            val count = (_state.value)?.data?.count() ?: 0
            return count >= 2
        }

    val showAccounts: LiveData<Boolean> = Transformations.map(_state) { state ->
        !state.loading && state.data.isNotEmpty()
    }

    val showEmptyMessage: LiveData<Boolean> = Transformations.map(_state) { state ->
        !state.loading && state.data.isEmpty()
    }

    val showLoading: LiveData<Boolean> = Transformations.map(_state) { state ->
        state.loading
    }

    //region Action Mode
    private var selectedAccount: Account? = null
    private var actionMode: ActionMode? = null
    private var actionModeCallback: ActionMode.Callback = object : ActionMode.Callback {
        override fun onActionItemClicked(mode: ActionMode?, item: MenuItem?): Boolean {
            when (item?.itemId) {
                R.id.action_delete -> deleteSelectedAccount()
            }

            return true
        }

        override fun onCreateActionMode(mode: ActionMode?, menu: Menu?): Boolean {
            mode?.menuInflater?.inflate(R.menu.menu_account_actions, menu)
            return true
        }

        override fun onPrepareActionMode(mode: ActionMode?, menu: Menu?): Boolean {
            mode?.title = selectedAccount?.name
            return true
        }

        override fun onDestroyActionMode(mode: ActionMode?) {
            actionMode = null
        }
    }

    fun startActionModeForAccount(account: Account, activity: AppCompatActivity) {
        this.selectedAccount = account
        this.actionMode = activity.startSupportActionMode(actionModeCallback)
    }

    fun clearActionMode() = actionMode?.finish()
    //endregion

    init {
        _state.value = AccountListState.loading()

        viewModelScope.launch {
            repository.fetchAllAccounts().collect { accounts ->
                _state.value = AccountListState.success(accounts)
            }
        }
    }

    /**
     * Deletes whatever account was selected by the user in the [actionMode].
     */
    private fun deleteSelectedAccount() {
        val account = selectedAccount ?: return

        viewModelScope.launch {
            repository.deleteAccount(account)
            analyticsTracker.trackAccountDeleted()

            clearActionMode()
            notifyChange()
        }
    }

    override fun onCleared() {
        super.onCleared()
        actionMode = null
        selectedAccount = null
    }
}
