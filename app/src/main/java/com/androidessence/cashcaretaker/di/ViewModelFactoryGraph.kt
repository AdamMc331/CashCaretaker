@file:Suppress("UNCHECKED_CAST")

package com.androidessence.cashcaretaker.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.androidessence.cashcaretaker.account.AccountListState
import com.androidessence.cashcaretaker.account.AccountListViewModel
import com.androidessence.cashcaretaker.redux.Store

interface ViewModelFactoryGraph {
    fun accountListViewModelFactory(): ViewModelProvider.Factory
}

class BaseViewModelFactoryGraph(
    private val storeGraph: StoreGraph
) : ViewModelFactoryGraph {
    override fun accountListViewModelFactory(): ViewModelProvider.Factory {
        return AccountListViewModelFactory(storeGraph.accountListStore())
    }
}

private class AccountListViewModelFactory(
    private val accountListStore: Store<AccountListState>
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return AccountListViewModel(accountListStore) as T
    }
}
