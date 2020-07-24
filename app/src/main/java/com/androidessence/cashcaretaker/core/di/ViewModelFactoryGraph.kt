@file:Suppress("UNCHECKED_CAST")

package com.androidessence.cashcaretaker.core.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.androidessence.cashcaretaker.core.models.Transaction
import com.androidessence.cashcaretaker.ui.accountlist.AccountListViewModel
import com.androidessence.cashcaretaker.ui.addaccount.AddAccountViewModel
import com.androidessence.cashcaretaker.ui.addtransaction.AddTransactionViewModel
import com.androidessence.cashcaretaker.ui.transactionlist.TransactionListViewModel
import com.androidessence.cashcaretaker.ui.transfer.AddTransferViewModel

interface ViewModelFactoryGraph {
    fun accountListViewModelFactory(): ViewModelProvider.Factory

    fun transactionListViewModelFactory(
        accountName: String,
        editClicked: (Transaction) -> Unit
    ): ViewModelProvider.Factory

    fun addTransactionViewModelFactory(): ViewModelProvider.Factory

    fun addTransferViewModelFactory(): ViewModelProvider.Factory

    fun addAccountViewModelFactory(): ViewModelProvider.Factory
}

class BaseViewModelFactoryGraph(
    private val dataGraph: DataGraph
) : ViewModelFactoryGraph {
    override fun accountListViewModelFactory(): ViewModelProvider.Factory {
        return AccountListViewModelFactory(
            dataGraph
        )
    }

    override fun transactionListViewModelFactory(
        accountName: String,
        editClicked: (Transaction) -> Unit
    ): ViewModelProvider.Factory {
        return TransactionListViewModelFactory(
            dataGraph = dataGraph,
            accountName = accountName,
            editClicked = editClicked
        )
    }

    override fun addTransactionViewModelFactory(): ViewModelProvider.Factory {
        return AddTransactionViewModelFactory(
            dataGraph = dataGraph
        )
    }

    override fun addTransferViewModelFactory(): ViewModelProvider.Factory {
        return AddTransferViewModelFactory(
            dataGraph = dataGraph
        )
    }

    override fun addAccountViewModelFactory(): ViewModelProvider.Factory {
        return AddAccountViewModelFactory(
            dataGraph = dataGraph
        )
    }
}

private class AccountListViewModelFactory(
    private val dataGraph: DataGraph
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return AccountListViewModel(
            repository = dataGraph.repository,
            analyticsTracker = dataGraph.analyticsTracker
        ) as T
    }
}

private class TransactionListViewModelFactory(
    private val dataGraph: DataGraph,
    private val accountName: String,
    private val editClicked: (Transaction) -> Unit
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return TransactionListViewModel(
            repository = dataGraph.repository,
            accountName = accountName,
            editClicked = editClicked,
            analyticsTracker = dataGraph.analyticsTracker
        ) as T
    }
}

private class AddTransactionViewModelFactory(
    private val dataGraph: DataGraph
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return AddTransactionViewModel(
            repository = dataGraph.repository,
            analyticsTracker = dataGraph.analyticsTracker
        ) as T
    }
}

private class AddTransferViewModelFactory(
    private val dataGraph: DataGraph
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return AddTransferViewModel(
            repository = dataGraph.repository,
            analyticsTracker = dataGraph.analyticsTracker
        ) as T
    }
}

private class AddAccountViewModelFactory(
    private val dataGraph: DataGraph
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return AddAccountViewModel(
            repository = dataGraph.repository,
            analyticsTracker = dataGraph.analyticsTracker,
            dispatcherProvider = dataGraph.dispatcherProvider
        ) as T
    }
}
