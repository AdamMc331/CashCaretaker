package com.androidessence.cashcaretaker.core.di

import android.content.Context
import com.androidessence.cashcaretaker.data.CCRepository
import com.androidessence.cashcaretaker.data.DataDispatcherProvider
import com.androidessence.cashcaretaker.data.DispatcherProvider
import com.androidessence.cashcaretaker.data.analytics.AnalyticsTracker
import com.androidessence.cashcaretaker.data.analytics.FirebaseAnalyticsTracker
import com.androidessence.cashcaretaker.data.local.DatabaseService
import com.androidessence.cashcaretaker.database.RoomDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Qualifier

@Module
@InstallIn(ActivityRetainedComponent::class)
object HiltDataModule {

    @CCDispatcherProvider
    @Provides
    fun provideDispatcherProvider(): DispatcherProvider {
        return DataDispatcherProvider()
    }

    @Provides
    fun provideRepository(
        @ApplicationContext context: Context,
        @CCDispatcherProvider dispatcherProvider: DispatcherProvider
    ): CCRepository {
        val database = RoomDatabase(context)
        return DatabaseService(database, dispatcherProvider)
    }

    @Provides
    fun provideAnalyticsTracker(): AnalyticsTracker {
        return FirebaseAnalyticsTracker()
    }
}

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class CCDispatcherProvider
