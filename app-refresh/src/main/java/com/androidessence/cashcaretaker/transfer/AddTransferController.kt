package com.androidessence.cashcaretaker.transfer

import com.androidessence.cashcaretaker.account.Account
import com.androidessence.cashcaretaker.core.DataController
import java.util.*

/**
 * Controller that defines the behavior of a view for adding a transfer.
 */
interface AddTransferController : DataController {
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
     * Displays an error if the to account and from account are equal.
     */
    fun showSameAccountError()

    /**
     * Displays an error if the from account is invalid.
     */
    fun showFromAccountError()

    /**
     * Displays an error if the to account is invalid.
     */
    fun showToAccountError()

    /**
     * Displays an error if the amount is invalid.
     */
    fun showAmountError()

    /**
     * Callback method when a transfer is inserted successfully.
     */
    fun onInserted()

    /**
     * Callback method when a transfer is not inserted successfully.
     */
    fun onError()
}