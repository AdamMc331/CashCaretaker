package com.androidessence.cashcaretaker.transaction

import android.support.v7.view.ActionMode
import android.view.Menu
import android.view.MenuItem
import com.androidessence.cashcaretaker.R
import com.androidessence.cashcaretaker.core.BasePresenter
import com.androidessence.cashcaretaker.data.DataViewState
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * Implementation of the presenter for transactions.
 */
class TransactionPresenterImpl(private var controller: TransactionController?, private val transactionInteractor: TransactionInteractor, private val accountName: String) : TransactionPresenter {

    override var selectedTransaction: Transaction? = null

    override var actionMode: ActionMode? = null

    override var actionModeCallback: ActionMode.Callback = object : ActionMode.Callback {
        override fun onActionItemClicked(mode: ActionMode?, item: MenuItem?): Boolean {
            when (item?.itemId) {
                R.id.action_delete -> deleteSelectedTransaction()
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

    override fun onDestroy() {
        actionMode = null
        selectedTransaction = null
        controller = null
    }

    override fun onAttach() {
        controller?.showProgress()
        transactionInteractor.getForAccount(accountName)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        { controller?.viewState = DataViewState.ListSuccess(it) },
                        { controller?.viewState = DataViewState.Error(it) }
                )
    }

    override fun deleteSelectedTransaction() {
        selectedTransaction?.let {
            controller?.viewState = DataViewState.Loading()
            Single.fromCallable { transactionInteractor.delete(it) }
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                            { controller?.viewState = DataViewState.ItemsRemoved(it) },
                            { controller?.viewState = DataViewState.Error(it) }
                    )
        }
    }
}