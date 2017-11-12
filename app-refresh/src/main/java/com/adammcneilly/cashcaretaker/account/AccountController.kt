package com.adammcneilly.cashcaretaker.account

import com.adammcneilly.cashcaretaker.core.DataController

/**
 * View for displaying a list of accounts.
 */
interface AccountController : DataController {
    fun setAccounts(accounts: List<Account>)
    fun onWithdrawalButtonClicked(account: Account)
    fun onDepositButtonClicked(account: Account)
}