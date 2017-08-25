package com.adammcneilly.cashcaretaker.presenters

/**
 * Base presenter that knows we need to handle onDestroy.
 */
interface BasePresenter {
    fun onDestroy()
}