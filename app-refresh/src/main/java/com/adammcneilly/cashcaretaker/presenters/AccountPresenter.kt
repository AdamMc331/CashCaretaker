package com.adammcneilly.cashcaretaker.presenters

/**
 * Presenter that handles account business logic.
 */
interface AccountPresenter: BasePresenter {
    fun onResume()
    fun onAddClicked()
}