package com.androidessence.cashcaretaker


import android.util.Log
import android.util.Log.*
import com.crashlytics.android.Crashlytics
import timber.log.Timber

/**
 * An extension of a [Timber.Tree] which logs errors to Crashlytics.
 *
 * This is only used on production, whereas debug builds will just log with a debug tree.
 *
 * @see [App.onCreate]
 */
class CrashlyticsTree : Timber.Tree() {
    override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {

        when(priority) {
            WARN, ERROR, ASSERT -> {
                Crashlytics.logException(t)
            }
            INFO -> Log.println(priority, tag, message)
        }
    }
}