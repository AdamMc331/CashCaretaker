package com.androidessence.cashcaretaker.transaction

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

class TransactionFragmentViewModel(
    private val repository: CCRepository,
    accountName: String,
    private val editClicked: (Transaction) -> Unit
) : BaseViewModel() {
    private val _state: MutableLiveData<DataViewState> = MutableLiveData<DataViewState>().apply {
        value = DataViewState.Loading
    }

    val transactions: LiveData<List<Transaction>> = Transformations.map(_state) { state ->
        (state as? DataViewState.Success<*>)?.result?.filterIsInstance(Transaction::class.java)
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
                    selectedTransaction?.let(editClicked::invoke)
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
        viewModelScope.launch {
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
            job = CoroutineScope(Dispatchers.IO).launch {
                repository.deleteTransaction(transaction)
                clearActionMode()
                notifyChange()
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        actionMode = null
        selectedTransaction = null
    }
}
