package com.androidessence.cashcaretaker.util.logging

import timber.log.Timber

class AndroidLogger : Logger {
    override fun debug(message: String) {
        Timber.d(message)
    }
}
