package com.androidessence.cashcaretaker

import android.app.Application
import android.content.Context
import com.androidessence.cashcaretaker.core.di.*
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import timber.log.Timber

/**
 * Core application class that keeps a context and initializes Timber.
 */
class App : Application(), CashCaretakerGraphProvider {

    override val graph: CashCaretakerGraph by lazy {
        BaseCashCaretakerGraph(this)
    }

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

fun Context.graph(): CashCaretakerGraph {
    return (this.applicationContext as CashCaretakerGraphProvider).graph
}
