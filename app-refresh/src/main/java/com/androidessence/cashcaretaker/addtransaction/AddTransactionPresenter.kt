package com.androidessence.cashcaretaker.addtransaction

import com.androidessence.cashcaretaker.core.BasePresenter
import java.util.*

/**
 * Presenter for adding an account.
 */
interface AddTransactionPresenter: BasePresenter {

    /**
     * Callback used when a list of transactions are inserted.
     */
    fun onInserted(ids: List<Long>)

    /**
     * Callback used if the transaction description is invalid.
     */
    fun onTransactionDescriptionError()

    /**
     * Callback used if the transaction amount is invalid.
     */
    fun onTransactionAmountError()

    /**
     * Validates and inserts a transaction into the database.
     *
     * @param[accountName] The name of the account for this transaction.
     * @param[transactionDescription] The description of the purchase.
     * @param[transactionAmount] The amount of the transaction.
     * @param[withdrawal] Flag defining whether this transaction is a withdrawal.
     * @param[date] The date of the purchase.
     */
    fun insert(accountName: String, transactionDescription: String, transactionAmount: String, withdrawal: Boolean, date: Date)
}