package com.androidessence.cashcaretaker.transfer

import com.androidessence.cashcaretaker.account.Account
import java.util.*

/**
 * Interface defining the required behavior for adding a transfer.
 */
interface AddTransferInteractor {
    fun addTransfer(fromAccount: Account, toAccount: Account, amount: Double, date: Date): Boolean
}