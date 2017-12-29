package com.androidessence.cashcaretaker.account

import android.support.v7.view.ActionMode
import android.view.Menu
import android.view.MenuItem
import com.androidessence.cashcaretaker.R
import com.androidessence.cashcaretaker.data.DataViewState
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * Implementation of the presenter for accounts.
 *
 * @property[controller] The fingerprintController that should be modified as data is fetched.
 * @property[interactor] The interactor responsible for fetching account data.
 */
class AccountPresenterImpl(private var controller: AccountController?, private val interactor: AccountInteractor) : AccountPresenter {
    override var selectedAccount: Account? = null

    override var actionMode: ActionMode? = null

    override var actionModeCallback: ActionMode.Callback = object : ActionMode.Callback {
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

    override fun onAttach() {
        controller?.viewState = DataViewState.Loading()
        interactor.getAll()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        { controller?.viewState = DataViewState.ListSuccess(it) },
                        { controller?.viewState = DataViewState.Error(it) }
                )
    }

    override fun onDestroy() {
        actionMode = null
        selectedAccount = null
        controller = null
    }

    override fun deleteSelectedAccount() {
        selectedAccount?.let {
            controller?.viewState = DataViewState.Loading()
            Single.fromCallable { interactor.delete(it) }
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                            { controller?.viewState = DataViewState.ItemsRemoved(it) },
                            { controller?.viewState = DataViewState.Error(it) }
                    )
        }
    }
}