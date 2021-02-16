package com.androidessence.cashcaretaker.ui.transactionlist

import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.view.ActionMode
import androidx.lifecycle.viewModelScope
import com.adammcneilly.cashcaretaker.analytics.AnalyticsTracker
import com.androidessence.cashcaretaker.R
import com.androidessence.cashcaretaker.core.BaseViewModel
import com.androidessence.cashcaretaker.core.models.Transaction
import com.androidessence.cashcaretaker.data.CCRepository
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class TransactionListViewModel(
    accountName: String,
    private val repository: CCRepository,
    private val analyticsTracker: AnalyticsTracker,
) : BaseViewModel() {
    private val _viewState: MutableStateFlow<TransactionListViewState> = MutableStateFlow(
        TransactionListViewState(
            showLoading = true,
            accountName = accountName,
            transactions = emptyList(),
        )
    )

    val viewState: StateFlow<TransactionListViewState> = _viewState

    private val editClickedChannel: Channel<Transaction> = Channel()
    val editClickedFlow: Flow<Transaction> = editClickedChannel.receiveAsFlow()

    //region Action Mode
    private var selectedTransaction: Transaction? = null
    private var actionMode: ActionMode? = null
    private var actionModeCallback: ActionMode.Callback = object : ActionMode.Callback {
        override fun onActionItemClicked(mode: ActionMode?, item: MenuItem?): Boolean {
            when (item?.itemId) {
                R.id.action_delete -> deleteSelectedTransaction()
                R.id.action_edit -> {
                    selectedTransaction?.let { transaction ->
                        viewModelScope.launch {
                            editClickedChannel.send(transaction)
                        }
                    }
                    clearActionMode()
                }
            }

            return true
        }

        override fun onCreateActionMode(mode: ActionMode?, menu: Menu?): Boolean {
            mode?.menuInflater?.inflate(R.menu.menu_transaction_actions, menu)
            return true
        }

        override fun onPrepareActionMode(mode: ActionMode?, menu: Menu?): Boolean {
            mode?.title = selectedTransaction?.description
            return true
        }

        override fun onDestroyActionMode(mode: ActionMode?) {
            actionMode = null
        }
    }

    fun startActionModeForTransaction(transaction: Transaction, activity: AppCompatActivity) {
        this.selectedTransaction = transaction
        this.actionMode = activity.startSupportActionMode(actionModeCallback)
    }

    fun clearActionMode() = actionMode?.finish()
    //endregion

    init {
        repository.fetchTransactionsForAccount(accountName)
            .onEach { transactions ->
                _viewState.value = _viewState.value.copy(
                    showLoading = false,
                    transactions = transactions,
                )
            }
            .launchIn(viewModelScope)
    }

    private fun deleteSelectedTransaction() {
        selectedTransaction?.let { transaction ->
            viewModelScope.launch {
                repository.deleteTransaction(transaction)
                analyticsTracker.trackTransactionDeleted()
                clearActionMode()
                notifyChange()
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        actionMode = null
        selectedTransaction = null
        editClickedChannel.close()
    }
}
