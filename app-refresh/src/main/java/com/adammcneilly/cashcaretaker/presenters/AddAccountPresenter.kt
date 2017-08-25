package com.adammcneilly.cashcaretaker.presenters

/**
 * Presenter for adding an account.
 */
interface AddAccountPresenter: BasePresenter {
    fun insert(accountName: String, accountBalance: String)
}