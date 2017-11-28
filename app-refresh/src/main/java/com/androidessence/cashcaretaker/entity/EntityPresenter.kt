package com.androidessence.cashcaretaker.entity

import com.androidessence.cashcaretaker.core.BasePresenter

/**
 * Presenter for pulling a list of entities from somewhere.
 */
interface EntityPresenter<in T>: BasePresenter {
    fun onFetched(entities: List<T>)
}