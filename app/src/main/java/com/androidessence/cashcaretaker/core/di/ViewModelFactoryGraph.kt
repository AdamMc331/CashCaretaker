@file:Suppress("UNCHECKED_CAST")

package com.androidessence.cashcaretaker.core.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.androidessence.cashcaretaker.data.CCRepository
import com.androidessence.cashcaretaker.data.DispatcherProvider
import com.androidessence.cashcaretaker.data.analytics.AnalyticsTracker
import com.androidessence.cashcaretaker.ui.transactionlist.TransactionListViewModel
import org.koin.core.KoinComponent
import org.koin.core.context.KoinContextHandler.get
import org.koin.core.inject
import org.koin.java.KoinJavaComponent.inject

interface ViewModelFactoryGraph {
    fun transactionListViewModelFactory(
        accountName: String
    ): ViewModelProvider.Factory
}

class BaseViewModelFactoryGraph : ViewModelFactoryGraph {

    override fun transactionListViewModelFactory(
        accountName: String
    ): ViewModelProvider.Factory {
        return TransactionListViewModelFactory(
            accountName = accountName
        )
    }
}

private class TransactionListViewModelFactory(
    private val accountName: String
) : ViewModelProvider.Factory, KoinComponent {
    private val repository: CCRepository by inject()
    private val analyticsTracker: AnalyticsTracker by inject()
    private val dispatcherProvider: DispatcherProvider by inject()

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return TransactionListViewModel(
            repository = repository,
            accountName = accountName,
            analyticsTracker = analyticsTracker,
            dispatcherProvider = dispatcherProvider
        ) as T
    }
}
