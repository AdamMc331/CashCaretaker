package com.androidessence.cashcaretaker

import android.app.Application
import timber.log.Timber

/**
 * Core application class that keeps a context and initializes Timber.
 */
class App : Application() {

    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        } else {
            Timber.plant(CrashlyticsTree())
        }

    }
}