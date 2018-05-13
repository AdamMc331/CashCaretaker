package com.androidessence.cashcaretaker.base

import android.arch.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

/**
 * Base ViewModel class that maintains a reference to a [CompositeDisposable].
 *
 * @property[compositeDisposable] Maintains a reference to any observable requests this ViewModel
 * is making, and clears them when the fragment is destroyed.
 */
open class BaseViewModel : ViewModel() {
    private val compositeDisposable = CompositeDisposable()

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }

    /**
     * Extension method used for convenience to add a Disposable to our [compositeDisposable].
     */
    protected fun Disposable.addToComposite(): Disposable {
        compositeDisposable.add(this)
        return this
    }
}