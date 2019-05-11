package com.androidessence.cashcaretaker.base

import androidx.databinding.Observable
import androidx.databinding.Bindable
import androidx.databinding.PropertyChangeRegistry
import androidx.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import kotlinx.coroutines.Job

/**
 * Base ViewModel class that maintains a reference to a [CompositeDisposable].
 *
 * @property[compositeDisposable] Maintains a reference to any observable requests this ViewModel
 * is making, and clears them when the fragment is destroyed.
 */
open class BaseViewModel : ViewModel(), Observable {
    private val compositeDisposable = CompositeDisposable()
    protected var job: Job? = null

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
        job?.cancel()
    }

    /**
     * Extension method used for convenience to add a Disposable to our [compositeDisposable].
     */
    protected fun Disposable.addToComposite(): Disposable {
        compositeDisposable.add(this)
        return this
    }

    @Transient
    private var mCallbacks: PropertyChangeRegistry? = null

    override fun addOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback) {
        synchronized(this) {
            if (mCallbacks == null) {
                mCallbacks = PropertyChangeRegistry()
            }
        }
        mCallbacks!!.add(callback)
    }

    override fun removeOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback) {
        synchronized(this) {
            if (mCallbacks == null) {
                return
            }
        }
        mCallbacks!!.remove(callback)
    }

    /**
     * Notifies listeners that all properties of this instance have changed.
     */
    fun notifyChange() {
        synchronized(this) {
            if (mCallbacks == null) {
                return
            }
        }
        mCallbacks!!.notifyCallbacks(this, 0, null)
    }

    /**
     * Notifies listeners that a specific property has changed. The getter for the property
     * that changes should be marked with [Bindable] to generate a field in
     * `BR` to be used as `fieldId`.
     *
     * @param fieldId The generated BR id for the Bindable field.
     */
    fun notifyPropertyChanged(fieldId: Int) {
        synchronized(this) {
            if (mCallbacks == null) {
                return
            }
        }
        mCallbacks!!.notifyCallbacks(this, fieldId, null)
    }
}