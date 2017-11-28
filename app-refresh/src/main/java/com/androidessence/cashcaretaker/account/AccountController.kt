package com.androidessence.cashcaretaker.account

import com.androidessence.cashcaretaker.core.DataController

/**
 * View for displaying a list of accounts.
 */
interface AccountController : DataController {
    fun setAccounts(accounts: List<Account>)
    fun onWithdrawalButtonClicked(account: Account)
    fun onDepositButtonClicked(account: Account)
    fun onAccountSelected(account: Account)
}