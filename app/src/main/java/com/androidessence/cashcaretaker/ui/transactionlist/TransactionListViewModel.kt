package com.androidessence.cashcaretaker.ui.transactionlist

import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.view.ActionMode
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.viewModelScope
import com.androidessence.cashcaretaker.R
import com.androidessence.cashcaretaker.core.BaseViewModel
import com.androidessence.cashcaretaker.core.models.Transaction
import com.androidessence.cashcaretaker.data.CCRepository
import com.androidessence.cashcaretaker.data.DataViewState
import com.androidessence.cashcaretaker.data.DispatcherProvider
import com.androidessence.cashcaretaker.data.analytics.AnalyticsTracker
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class TransactionListViewModel(
    accountName: String,
    private val repository: CCRepository,
    private val analyticsTracker: AnalyticsTracker,
    private val dispatcherProvider: DispatcherProvider
) : BaseViewModel() {
    private val _state: MutableLiveData<DataViewState> = MutableLiveData<DataViewState>().apply {
        value = DataViewState.Loading
    }

    private val editClickedChannel: Channel<Transaction> = Channel()
    val editClickedFlow: Flow<Transaction> = editClickedChannel.receiveAsFlow()

    val transactions: LiveData<List<Transaction>> = Transformations.map(_state) { state ->
        (state as? DataViewState.Success<*>)
            ?.result
            ?.filterIsInstance(Transaction::class.java)
            .orEmpty()
    }

    val showTransactions: LiveData<Boolean> = Transformations.map(_state) { state ->
        state is DataViewState.Success<*>
    }

    val showEmptyMessage: LiveData<Boolean> = Transformations.map(_state) { state ->
        state is DataViewState.Empty
    }

    val showLoading: LiveData<Boolean> = Transformations.map(_state) { state ->
        state is DataViewState.Loading
    }

    //region Action Mode
    private var selectedTransaction: Transaction? = null
    private var actionMode: ActionMode? = null
    private var actionModeCallback: ActionMode.Callback = object : ActionMode.Callback {
        override fun onActionItemClicked(mode: ActionMode?, item: MenuItem?): Boolean {
            when (item?.itemId) {
                R.id.action_delete -> deleteSelectedTransaction()
                R.id.action_edit -> {
                    selectedTransaction?.let { transaction ->
                        viewModelScope.launch(dispatcherProvider.mainDispatcher) {
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
        viewModelScope.launch(dispatcherProvider.mainDispatcher) {
            repository.fetchTransactionsForAccount(accountName).collect { transactions ->
                val dataViewState = when {
                    transactions.isEmpty() -> DataViewState.Empty
                    else -> DataViewState.Success(transactions)
                }

                _state.value = dataViewState
            }
        }
    }

    private fun deleteSelectedTransaction() {
        selectedTransaction?.let { transaction ->
            viewModelScope.launch(dispatcherProvider.mainDispatcher) {
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
