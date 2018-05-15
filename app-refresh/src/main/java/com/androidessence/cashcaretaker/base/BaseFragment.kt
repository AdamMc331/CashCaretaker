package com.androidessence.cashcaretaker.base

import androidx.fragment.app.Fragment
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

/**
 * Base fragment class that maintains a [CompositeDisposable].
 *
 * @property[compositeDisposable] Maintains a reference to any observable requests this Fragment
 * is making, and clears them when the fragment is destroyed.
 */
open class BaseFragment : androidx.fragment.app.Fragment() {
    private val compositeDisposable = CompositeDisposable()

    override fun onDestroy() {
        super.onDestroy()
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