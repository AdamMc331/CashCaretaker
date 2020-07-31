package com.androidessence.cashcaretaker.core.di

import com.androidessence.cashcaretaker.data.CCRepository
import com.androidessence.cashcaretaker.data.DispatcherProvider
import com.androidessence.cashcaretaker.data.ProductionDispatcherProvider
import com.androidessence.cashcaretaker.data.local.DatabaseService
import org.koin.dsl.module

val dataModule = module {
    single<DispatcherProvider> {
        ProductionDispatcherProvider()
    }

    single<CCRepository> {
        DatabaseService(
            database = get(),
            dispatcherProvider = get()
        )
    }
}
