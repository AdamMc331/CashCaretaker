package com.adammcneilly.cashcaretaker.views

import com.adammcneilly.cashcaretaker.entities.Account

/**
 * View for displaying a list of accounts.
 */
interface AccountView {
    fun showProgress()

    fun hideProgress()

    fun setAccounts(accounts: List<Account>)
}