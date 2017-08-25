package com.adammcneilly.cashcaretaker.views

import com.adammcneilly.cashcaretaker.entities.Account

/**
 * View for displaying a list of accounts.
 */
interface AccountView: DataView {
    fun setAccounts(accounts: List<Account>)
    fun navigateToAddAccount()
}