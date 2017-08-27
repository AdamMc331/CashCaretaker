package com.adammcneilly.cashcaretaker.account

import com.adammcneilly.cashcaretaker.core.DataView

/**
 * View for displaying a list of accounts.
 */
interface AccountView: DataView {
    fun setAccounts(accounts: List<Account>)
    fun navigateToAddAccount()
}