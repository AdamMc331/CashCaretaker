package com.adammcneilly.cashcaretaker.account

import com.adammcneilly.cashcaretaker.core.DataView

/**
 * View for displaying a list of accounts.
 */
interface AccountController : DataView {
    fun setAccounts(accounts: List<Account>)
    fun onWithdrawalButtonClicked(account: Account)
}