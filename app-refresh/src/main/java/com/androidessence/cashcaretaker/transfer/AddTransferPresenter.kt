package com.androidessence.cashcaretaker.transfer

import com.androidessence.cashcaretaker.account.Account
import com.androidessence.cashcaretaker.core.BasePresenter
import java.util.*

/**
 * Defines the behavior for a presentation layer when adding a transfer.
 */
interface AddTransferPresenter : BasePresenter {
    /**
     * Transfers money from one account to another.
     *
     * @param[fromAccount] The account that has the money to be transferred.
     * @param[toAccount] The account that we are transferring money to.
     * @param[amount] The amount of money to transfer.
     * @param[date] The date of the transfer.
     */
    fun addTransfer(fromAccount: Account?, toAccount: Account?, amount: String, date: Date)

    /**
     * Callback method when a transfer is inserted successfully.
     */
    fun onInserted()

    /**
     * Callback method when a transfer is not inserted successfully.
     */
    fun onError()
}