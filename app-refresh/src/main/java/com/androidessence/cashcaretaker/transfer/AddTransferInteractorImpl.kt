package com.androidessence.cashcaretaker.transfer

import com.androidessence.cashcaretaker.account.Account
import com.androidessence.cashcaretaker.data.CCRepository
import java.util.*

/**
 * Implementation of [AddTransferInteractor]
 */
class AddTransferInteractorImpl : AddTransferInteractor {
    override fun addTransfer(fromAccount: Account, toAccount: Account, amount: Double, date: Date): Boolean =
            CCRepository.transfer(fromAccount, toAccount, amount, date)
}