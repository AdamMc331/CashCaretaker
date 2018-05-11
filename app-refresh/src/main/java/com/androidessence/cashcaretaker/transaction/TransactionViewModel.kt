package com.androidessence.cashcaretaker.transaction

import android.arch.lifecycle.ViewModel
import android.support.v7.app.AppCompatActivity
import android.support.v7.view.ActionMode
import android.view.Menu
import android.view.MenuItem
import com.androidessence.cashcaretaker.R
import com.androidessence.cashcaretaker.data.CCRepository
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject
import timber.log.Timber

class TransactionViewModel(private val repository: CCRepository): ViewModel() {
    private val compositeDisposable: CompositeDisposable by lazy { CompositeDisposable() }
    val editClicked: PublishSubject<Transaction> = PublishSubject.create()
    val transactionList: BehaviorSubject<List<Transaction>> = BehaviorSubject.create()

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
    fun fetchTransactionForAccount(accountName: String) {
        val subscription = repository
                .getTransactionsForAccount(accountName)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        transactionList::onNext,
                        Timber::e
                )

        compositeDisposable.add(subscription)
    }

    private fun deleteSelectedTransaction() {
        selectedTransaction?.let { transaction ->
            Single.fromCallable { repository.deleteTransaction(transaction) }
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                            { clearActionMode() },
                            Timber::e
                    )
        }
    }
    //endregion

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
        actionMode = null
        selectedTransaction = null
    }
}