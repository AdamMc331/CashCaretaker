package com.androidessence.cashcaretaker.logging

import timber.log.Timber

class AndroidLogger : Logger {
    override fun debug(message: String) {
        Timber.d(message)
    }
}
