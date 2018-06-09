package com.androidessence.cashcaretaker.transaction

import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.view.ActionMode
import androidx.databinding.Bindable
import androidx.lifecycle.MutableLiveData
import com.androidessence.cashcaretaker.R
import com.androidessence.cashcaretaker.base.BaseViewModel
import com.androidessence.cashcaretaker.data.CCRepository
import com.androidessence.cashcaretaker.data.DataViewState
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import timber.log.Timber

class TransactionViewModel(private val repository: CCRepository) : BaseViewModel() {
    val editClicked: PublishSubject<Transaction> = PublishSubject.create()
    val state = MutableLiveData<DataViewState>()

    @Bindable
    fun getShowTransactions(): Boolean {
        return state.value is DataViewState.Success<*>
    }

    @Bindable
    fun getShowEmptyMessage(): Boolean {
        return state.value is DataViewState.Empty
    }

    @Bindable
    fun getShowLoading(): Boolean {
        return state.value is DataViewState.Loading
    }

    //region Action Mode
    private var selectedTransaction: Transaction? = null
    private var actionMode: ActionMode? = null
    private var actionModeCallback: ActionMode.Callback = object : ActionMode.Callback {
        override fun onActionItemClicked(mode: ActionMode?, item: MenuItem?): Boolean {
            when (item?.itemId) {
                R.id.action_delete -> deleteSelectedTransaction()
                R.id.action_edit -> {
                    selectedTransaction?.let(editClicked::onNext)
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

    //region Data Interactions
    /**
     * Retrieves a number of transactions from the [repository] for a given [accountName].
     *
     * @param[accountName] The unique identifier for an account that we want to request transaction for.
     */
    fun fetchTransactionForAccount(accountName: String) {
        if (state.value !is DataViewState.Success<*>) {
            postState(DataViewState.Loading())

            repository
                    .getTransactionsForAccount(accountName)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                            this::postState,
                            Timber::e
                    )
                    .addToComposite()
        }
    }

    private fun deleteSelectedTransaction() {
        selectedTransaction?.let { transaction ->
            Single.fromCallable { repository.deleteTransaction(transaction) }
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                            {
                                clearActionMode()
                                notifyChange()
                            },
                            Timber::e
                    )
                    .addToComposite()
        }
    }

    /**
     * Consumes a [newState] and posts it to our [state] subject. Also notifies that the bindable
     * properties of this ViewModel have changed.
     */
    private fun postState(newState: DataViewState) {
        state.postValue(newState)
        notifyChange()
    }
    //endregion

    override fun onCleared() {
        super.onCleared()
        actionMode = null
        selectedTransaction = null
    }
}