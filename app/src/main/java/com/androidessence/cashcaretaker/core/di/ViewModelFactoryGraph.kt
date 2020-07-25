@file:Suppress("UNCHECKED_CAST")

package com.androidessence.cashcaretaker.core.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.androidessence.cashcaretaker.ui.transactionlist.TransactionListViewModel

interface ViewModelFactoryGraph {
    fun transactionListViewModelFactory(
        accountName: String
    ): ViewModelProvider.Factory
}

class BaseViewModelFactoryGraph(
    private val dataGraph: DataGraph
) : ViewModelFactoryGraph {

    override fun transactionListViewModelFactory(
        accountName: String
    ): ViewModelProvider.Factory {
        return TransactionListViewModelFactory(
            dataGraph = dataGraph,
            accountName = accountName
        )
    }
}

private class TransactionListViewModelFactory(
    private val dataGraph: DataGraph,
    private val accountName: String
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return TransactionListViewModel(
            repository = dataGraph.repository,
            accountName = accountName,
            analyticsTracker = dataGraph.analyticsTracker,
            dispatcherProvider = dataGraph.dispatcherProvider
        ) as T
    }
}
