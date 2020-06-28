@file:Suppress("UNCHECKED_CAST")

package com.androidessence.cashcaretaker.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.androidessence.cashcaretaker.account.AccountListState
import com.androidessence.cashcaretaker.account.AccountListViewModel
import com.androidessence.cashcaretaker.data.CCRepository
import com.androidessence.cashcaretaker.redux.Store
import com.androidessence.cashcaretaker.transaction.Transaction
import com.androidessence.cashcaretaker.transaction.TransactionListViewModel

interface ViewModelFactoryGraph {
    fun accountListViewModelFactory(): ViewModelProvider.Factory
    fun transactionListViewModelFactory(
        accountName: String,
        editClicked: (Transaction) -> Unit
    ): ViewModelProvider.Factory
}

class BaseViewModelFactoryGraph(
    private val storeGraph: StoreGraph,
    private val dataGraph: DataGraph
) : ViewModelFactoryGraph {
    override fun accountListViewModelFactory(): ViewModelProvider.Factory {
        return AccountListViewModelFactory(storeGraph.accountListStore())
    }

    override fun transactionListViewModelFactory(
        accountName: String,
        editClicked: (Transaction) -> Unit
    ): ViewModelProvider.Factory {
        return TransactionListViewModelFactory(
            repository = dataGraph.repository,
            accountName = accountName,
            editClicked = editClicked
        )
    }
}

private class AccountListViewModelFactory(
    private val accountListStore: Store<AccountListState>
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return AccountListViewModel(accountListStore) as T
    }
}

private class TransactionListViewModelFactory(
    private val repository: CCRepository,
    private val accountName: String,
    private val editClicked: (Transaction) -> Unit
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return TransactionListViewModel(
            repository = repository,
            accountName = accountName,
            editClicked = editClicked
        ) as T
    }
}
