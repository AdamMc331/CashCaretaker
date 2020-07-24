package com.androidessence.cashcaretaker

import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.androidessence.cashcaretaker.data.DispatcherProvider
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

class TestObserver<T> : Observer<T> {
    var observedValue: T? = null

    override fun onChanged(t: T) {
        observedValue = t
    }
}

fun <T> LiveData<T>.testObserver() = TestObserver<T>().also {
    observeForever(it)
}

class TestDispatcherProvider : DispatcherProvider {
    override val ioDispatcher: CoroutineDispatcher
        get() = Dispatchers.Unconfined

    override val mainDispatcher: CoroutineDispatcher
        get() = Dispatchers.Unconfined
}
