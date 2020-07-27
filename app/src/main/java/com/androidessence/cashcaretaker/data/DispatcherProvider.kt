package com.androidessence.cashcaretaker.data

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

interface DispatcherProvider {
    val ioDispatcher: CoroutineDispatcher
    val mainDispatcher: CoroutineDispatcher
}

/**
 * This class will provide the [CoroutineDispatcher]s that are used in production, and
 * run the necessary [ioDispatcher] and [mainDispatcher] threads for networking and other
 * data requests.
 */
class ProductionDispatcherProvider : DispatcherProvider {
    override val ioDispatcher: CoroutineDispatcher
        get() = Dispatchers.IO

    override val mainDispatcher: CoroutineDispatcher
        get() = Dispatchers.Main
}
