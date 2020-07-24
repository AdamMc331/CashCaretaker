package com.androidessence.cashcaretaker.data

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

interface DispatcherProvider {
    val ioDispatcher: CoroutineDispatcher
    val mainDispatcher: CoroutineDispatcher
}

class DataDispatcherProvider : DispatcherProvider {
    override val ioDispatcher: CoroutineDispatcher
        get() = Dispatchers.IO

    override val mainDispatcher: CoroutineDispatcher
        get() = Dispatchers.Main
}
