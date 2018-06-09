package com.androidessence.cashcaretaker.account

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
import timber.log.Timber

/**
 * LifeCycle aware class that fetches accounts from the database and exposes them through the [state].
 */
class AccountViewModel(private val repository: CCRepository) : BaseViewModel() {
    val state = MutableLiveData<DataViewState>()

    @Bindable
    fun getShowAccounts(): Boolean {
        return state.value is DataViewState.Success<*>
    }

    @Bindable
    fun getShowEmptyMessage(): Boolean {
        return state.value is DataViewState.Empty
    }

    @Bindable
    fun getShowLoading(): Boolean {
        return state.value == null || state.value is DataViewState.Loading
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

    //region Data Interactions
    /**
     * Fetches a list of accounts from the [repository] as long as we haven't already.
     */
    fun fetchAccounts() {
        if (state.value !is DataViewState.Success<*>) {
            postState(DataViewState.Loading())

            repository
                    .getAllAccounts()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                            this::postState,
                            Timber::e
                    )
                    .addToComposite()
        }
    }

    /**
     * Deletes whatever account was selected by the user in the [actionMode].
     */
    private fun deleteSelectedAccount() {
        selectedAccount?.let { account ->
            Single.fromCallable { repository.deleteAccount(account) }
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                            {
                                clearActionMode()
                                notifyChange()
                            },
                            Timber::e
                    )
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
        selectedAccount = null
    }
}