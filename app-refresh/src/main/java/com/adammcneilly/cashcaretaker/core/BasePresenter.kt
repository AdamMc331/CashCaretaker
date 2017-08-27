package com.adammcneilly.cashcaretaker.core

/**
 * Base presenter that knows we need to handle onDestroy.
 */
interface BasePresenter {
    fun onDestroy()
}