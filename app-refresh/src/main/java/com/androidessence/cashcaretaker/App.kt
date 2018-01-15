package com.androidessence.cashcaretaker

import android.app.Application
import com.androidessence.cashcaretaker.data.CCDatabase
import com.androidessence.cashcaretaker.data.CCRepository
import timber.log.Timber

/**
 * Core application class that keeps a context and initializes Timber.
 */
class App : Application() {

    override fun onCreate() {
        super.onCreate()

        Timber.plant(Timber.DebugTree())
        CCRepository.init(CCDatabase.getInMemoryDatabase(this))
    }
}