package com.androidessence.cashcaretaker

import android.app.Application
import com.androidessence.cashcaretaker.core.di.dataModule
import com.androidessence.cashcaretaker.core.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import timber.log.Timber

/**
 * Core application class that keeps a context and initializes Timber.
 */
class App : Application() {

    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }

        startKoin {
            androidContext(this@App)
            modules(listOf(dataModule, viewModelModule))
        }
    }
}
