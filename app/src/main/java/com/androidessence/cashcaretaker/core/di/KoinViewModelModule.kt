package com.androidessence.cashcaretaker.core.di

import com.androidessence.cashcaretaker.ui.accountlist.AccountListViewModel
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
}
