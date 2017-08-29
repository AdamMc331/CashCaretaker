package com.adammcneilly.cashcaretaker

import android.app.Application
import android.content.Context
import android.view.inputmethod.InputMethodManager
import timber.log.Timber

/**
 * Core application class that keeps a context and initializes Timber.
 */
class App : Application() {

    override fun onCreate() {
        super.onCreate()
        instance = this

        Timber.plant(Timber.DebugTree())
    }

    companion object {
        lateinit var instance: Application
            private set
    }
}