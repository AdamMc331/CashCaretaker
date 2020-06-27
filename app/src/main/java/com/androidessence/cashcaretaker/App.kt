package com.androidessence.cashcaretaker

import android.app.Application
import android.content.Context
import com.androidessence.cashcaretaker.di.BaseCashCaretakerGraph
import com.androidessence.cashcaretaker.di.CashCaretakerGraph
import com.androidessence.cashcaretaker.di.CashCaretakerGraphProvider
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
    }
}

fun Context.graph(): CashCaretakerGraph {
    return (this.applicationContext as CashCaretakerGraphProvider).graph
}
