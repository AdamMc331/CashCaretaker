package com.androidessence.cashcaretaker.account

import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.view.ActionMode
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.viewModelScope
import com.androidessence.cashcaretaker.R
import com.androidessence.cashcaretaker.base.BaseViewModel
import com.androidessence.cashcaretaker.data.CCRepository
import com.androidessence.cashcaretaker.data.DataViewState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

/**
 * LifeCycle aware class that fetches accounts from the database and exposes them through the [_state].
 */
class AccountFragmentViewModel(
        private val repository: CCRepository
) : BaseViewModel() {
    private val _state: MutableLiveData<DataViewState> = MutableLiveData<DataViewState>().apply {
        value = DataViewState.Loading
    }

    val accounts: LiveData<List<Account>> = Transformations.map(_state) { state ->
        (state as? DataViewState.Success<*>)?.result?.filterIsInstance(Account::class.java).orEmpty()
    }

    val allowTransfers: Boolean
        get() {
            @Suppress("UNCHECKED_CAST")
            val count = (_state.value as? DataViewState.Success<Account>)?.result?.count() ?: 0
            return count >= 2
        }

    val showAccounts: LiveData<Boolean> = Transformations.map(_state) { state ->
        state is DataViewState.Success<*>
    }

    val showEmptyMessage: LiveData<Boolean> = Transformations.map(_state) { state ->
        state is DataViewState.Empty
    }

    val showLoading: LiveData<Boolean> = Transformations.map(_state) { state ->
        state == null || state is DataViewState.Loading
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
        viewModelScope.launch {
            repository.fetchAllAccounts().collect { accounts ->
                val dataViewState = when {
                    accounts.isEmpty() -> DataViewState.Empty
                    else -> DataViewState.Success(accounts)
                }

                _state.value = dataViewState
            }
        }
    }

    /**
     * Deletes whatever account was selected by the user in the [actionMode].
     */
    private fun deleteSelectedAccount() {
        selectedAccount?.let { account ->
            job = CoroutineScope(Dispatchers.IO).launch {
                repository.deleteAccount(account)
                clearActionMode()
                notifyChange()
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        actionMode = null
        selectedAccount = null
    }
}
