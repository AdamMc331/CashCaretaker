package com.androidessence.cashcaretaker.account

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
import timber.log.Timber

class AccountViewModel(private val repository: CCRepository) : ViewModel() {
    private val compositeDisposable: CompositeDisposable by lazy { CompositeDisposable() }
    val accountList: BehaviorSubject<List<Account>> = BehaviorSubject.create()

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

    //region Data Interactions
    fun fetchAccounts() {
        val subscription = repository
                .getAllAccounts()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        accountList::onNext,
                        Timber::e
                )

        compositeDisposable.add(subscription)
    }

    private fun deleteSelectedAccount() {
        selectedAccount?.let { account ->
            Single.fromCallable { repository.deleteAccount(account) }
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
        selectedAccount = null
    }
}