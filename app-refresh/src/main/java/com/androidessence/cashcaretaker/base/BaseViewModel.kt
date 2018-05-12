package com.androidessence.cashcaretaker.base

import android.arch.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

/**
 * Base ViewModel class that maintains a reference to a [CompositeDisposable].
 */
open class BaseViewModel : ViewModel() {
    protected val compositeDisposable = CompositeDisposable()

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }

    protected fun Disposable.addToComposite(): Disposable {
        compositeDisposable.add(this)
        return this
    }
}