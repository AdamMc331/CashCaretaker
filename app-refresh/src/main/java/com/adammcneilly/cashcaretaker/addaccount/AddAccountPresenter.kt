package com.adammcneilly.cashcaretaker.addaccount

import com.adammcneilly.cashcaretaker.core.BasePresenter

/**
 * Presenter for adding an account.
 */
interface AddAccountPresenter: BasePresenter {
    fun insert(accountName: String, accountBalance: String)
}