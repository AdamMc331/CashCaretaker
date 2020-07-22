@file:Suppress("UNCHECKED_CAST")

package com.androidessence.cashcaretaker.core.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.androidessence.cashcaretaker.core.models.Transaction
import com.androidessence.cashcaretaker.data.CCRepository
import com.androidessence.cashcaretaker.ui.account.AccountListViewModel
import com.androidessence.cashcaretaker.ui.transaction.TransactionListViewModel

interface ViewModelFactoryGraph {
    fun accountListViewModelFactory(): ViewModelProvider.Factory
    fun transactionListViewModelFactory(
        accountName: String,
        editClicked: (Transaction) -> Unit
    ): ViewModelProvider.Factory
}

class BaseViewModelFactoryGraph(
    private val dataGraph: DataGraph
) : ViewModelFactoryGraph {
    override fun accountListViewModelFactory(): ViewModelProvider.Factory {
        return AccountListViewModelFactory(dataGraph.repository)
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
    private val repository: CCRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return AccountListViewModel(repository) as T
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
