package com.androidessence.cashcaretaker.ui.accountlist

import com.androidessence.cashcaretaker.core.models.Account

interface AccountListItemClickListener {
    fun onAccountClicked(account: Account)
    fun onAccountLongClicked(account: Account)
    fun onDepositClicked(account: Account)
    fun onWithdrawalClicked(account: Account)
}
