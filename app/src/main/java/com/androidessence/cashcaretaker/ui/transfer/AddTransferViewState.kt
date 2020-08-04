package com.androidessence.cashcaretaker.ui.transfer

import com.androidessence.cashcaretaker.core.models.Account

data class AddTransferViewState(
    val fromAccountErrorRes: Int? = null,
    val toAccountErrorRes: Int? = null,
    val amountErrorRes: Int? = null,
    val accounts: List<Account>? = null
)
