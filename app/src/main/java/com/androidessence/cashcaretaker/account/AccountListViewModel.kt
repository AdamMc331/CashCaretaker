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
import com.androidessence.cashcaretaker.redux.Store
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

/**
 * LifeCycle aware class that fetches accounts from the database and exposes them through the [_state].
 */
@ExperimentalCoroutinesApi
class AccountListViewModel(
    private val store: Store<AccountListState>
) : BaseViewModel() {
    private val _state: MutableLiveData<AccountListState> = MutableLiveData<AccountListState>()

    val accounts: LiveData<List<Account>> = Transformations.map(_state) { state ->
        state?.data.orEmpty()
    }

    val allowTransfers: Boolean
        get() {
            @Suppress("UNCHECKED_CAST")
            val count = (_state.value)?.data?.count() ?: 0
            return count >= 2
        }

    val showAccounts: LiveData<Boolean> = Transformations.map(_state) { state ->
        !state.loading
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
        viewModelScope.launch {
            store.state.collect { newState ->
                _state.value = newState
            }
        }

        store.dispatch(AccountListAction.FetchAccounts(viewModelScope))
    }

    /**
     * Deletes whatever account was selected by the user in the [actionMode].
     */
    private fun deleteSelectedAccount() {
        selectedAccount?.let { account ->
            store.dispatch(AccountListAction.DeleteAccount(viewModelScope, account))
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
