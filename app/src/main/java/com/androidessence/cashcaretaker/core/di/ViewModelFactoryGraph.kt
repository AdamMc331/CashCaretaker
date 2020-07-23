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

    fun addTransactionViewModelFactory(
        transactionInserted: (Long) -> Unit,
        transactionUpdated: (Int) -> Unit
    ): ViewModelProvider.Factory

    fun addTransferViewModelFactory(
        transferInserted: (Boolean) -> Unit
    ): ViewModelProvider.Factory

    fun addAccountViewModelFactory(
        accountInserted: (Long) -> Unit
    ): ViewModelProvider.Factory
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

    override fun addTransactionViewModelFactory(
        transactionInserted: (Long) -> Unit,
        transactionUpdated: (Int) -> Unit
    ): ViewModelProvider.Factory {
        return AddTransactionViewModelFactory(
            dataGraph = dataGraph,
            transactionInserted = transactionInserted,
            transactionUpdated = transactionUpdated
        )
    }

    override fun addTransferViewModelFactory(
        transferInserted: (Boolean) -> Unit
    ): ViewModelProvider.Factory {
        return AddTransferViewModelFactory(
            dataGraph = dataGraph,
            transferInserted = transferInserted
        )
    }

    override fun addAccountViewModelFactory(
        accountInserted: (Long) -> Unit
    ): ViewModelProvider.Factory {
        return AddAccountViewModelFactory(
            dataGraph = dataGraph,
            accountInserted = accountInserted
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
            editClicked = editClicked
        ) as T
    }
}

private class AddTransactionViewModelFactory(
    private val dataGraph: DataGraph,
    private val transactionInserted: (Long) -> Unit,
    private val transactionUpdated: (Int) -> Unit
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return AddTransactionViewModel(
            repository = dataGraph.repository,
            transactionInserted = transactionInserted,
            transactionUpdated = transactionUpdated
        ) as T
    }
}

private class AddTransferViewModelFactory(
    private val dataGraph: DataGraph,
    private val transferInserted: (Boolean) -> Unit
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return AddTransferViewModel(
            repository = dataGraph.repository,
            transferInserted = transferInserted
        ) as T
    }
}

private class AddAccountViewModelFactory(
    private val dataGraph: DataGraph,
    private val accountInserted: (Long) -> Unit
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return AddAccountViewModel(
            repository = dataGraph.repository,
            accountInserted = accountInserted,
            analyticsTracker = dataGraph.analyticsTracker
        ) as T
    }
}
