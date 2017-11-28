package com.androidessence.cashcaretaker.core

/**
 * Base presenter that knows we need to handle onDestroy.
 */
interface BasePresenter {
    fun onAttach()
    fun onDestroy()
}