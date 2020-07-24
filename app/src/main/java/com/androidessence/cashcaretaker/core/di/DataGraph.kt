package com.androidessence.cashcaretaker.core.di

import android.content.Context
import com.androidessence.cashcaretaker.data.CCRepository
import com.androidessence.cashcaretaker.data.DispatcherProvider
import com.androidessence.cashcaretaker.data.analytics.AnalyticsTracker
import com.androidessence.cashcaretaker.data.analytics.FirebaseAnalyticsTracker
import com.androidessence.cashcaretaker.data.local.DatabaseService
import com.androidessence.cashcaretaker.database.RoomDatabase

interface DataGraph {
    val dispatcherProvider: DispatcherProvider
    val repository: CCRepository
    val analyticsTracker: AnalyticsTracker
}

class SQLiteDatabaseGraph(
    private val context: Context
) : DataGraph {
    override val dispatcherProvider: DispatcherProvider by lazy {
        DispatcherProvider()
    }

    override val repository: CCRepository by lazy {
        DatabaseService(
            database = RoomDatabase(context),
            dispatcherProvider = dispatcherProvider
        )
    }

    override val analyticsTracker: AnalyticsTracker by lazy {
        FirebaseAnalyticsTracker()
    }
}
