package com.androidessence.cashcaretaker.core.di

import com.androidessence.cashcaretaker.data.CCRepository
import com.androidessence.cashcaretaker.data.DataDispatcherProvider
import com.androidessence.cashcaretaker.data.DispatcherProvider
import com.androidessence.cashcaretaker.data.analytics.AnalyticsTracker
import com.androidessence.cashcaretaker.data.analytics.FirebaseAnalyticsTracker
import com.androidessence.cashcaretaker.data.local.DatabaseService
import com.androidessence.cashcaretaker.database.RoomDatabase
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val dataModule = module {
    single<DispatcherProvider> {
        DataDispatcherProvider()
    }

    single<AnalyticsTracker> {
        FirebaseAnalyticsTracker()
    }

    single<CCRepository> {
        DatabaseService(
            database = RoomDatabase(androidContext()),
            dispatcherProvider = get()
        )
    }
}
