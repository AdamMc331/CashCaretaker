package com.androidessence.cashcaretaker.core.di

import com.androidessence.cashcaretaker.ui.accountlist.AccountListViewModel
import com.androidessence.cashcaretaker.ui.addaccount.AddAccountViewModel
import com.androidessence.cashcaretaker.ui.addtransaction.AddTransactionViewModel
import com.androidessence.cashcaretaker.ui.transactionlist.TransactionListViewModel
import com.androidessence.cashcaretaker.ui.transfer.AddTransferViewModel
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel {
        AccountListViewModel(
            repository = get(),
            analyticsTracker = get(),
            dispatcherProvider = get()
        )
    }

    viewModel {
        AddTransactionViewModel(
            repository = get(),
            analyticsTracker = get(),
            dispatcherProvider = get()
        )
    }

    viewModel {
        AddTransferViewModel(
            repository = get(),
            analyticsTracker = get(),
            dispatcherProvider = get()
        )
    }

    viewModel {
        AddAccountViewModel(
            repository = get(),
            analyticsTracker = get(),
            dispatcherProvider = get()
        )
    }

    viewModel { (accountName: String) ->
        TransactionListViewModel(
            accountName = accountName,
            repository = get(),
            analyticsTracker = get(),
            dispatcherProvider = get()
        )
    }
}
