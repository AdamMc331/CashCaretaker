package com.adammcneilly.cashcaretaker.entity

import com.adammcneilly.cashcaretaker.core.BasePresenter

/**
 * Presenter for pulling a list of entities from somewhere.
 */
interface EntityPresenter<in T>: BasePresenter {
    fun onFetched(entities: List<T>)
}