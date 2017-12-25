package com.androidessence.cashcaretaker.addtransaction

import com.androidessence.cashcaretaker.core.BasePresenter
import java.util.*

/**
 * Presenter for adding an account.
 */
interface AddTransactionPresenter: BasePresenter {

    /**
     * Callback used when a list of transactions are inserted.
     *
     * @param[ids] The list of identifiers for the inserted transactions.
     */
    fun onInserted(ids: List<Long>)

    /**
     * Callback after a transaction is updated.
     *
     * @param[count] The number of rows in the DB that were updated.
     */
    fun onUpdated(count: Int)

    /**
     * Callback used if the transaction description is invalid.
     */
    fun onTransactionDescriptionError()

    /**
     * Callback used if the transaction amount is invalid.
     */
    fun onTransactionAmountError()

    /**
     * Callback used if there's any error while performing data operations.
     */
    fun onError(error: Throwable)

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

    /**
     * Validates and updates a transaction in the database.
     *
     * @param[id] The unique identifier of the transaction we're updating.
     * @param[accountName] The name of the account for this transaction.
     * @param[transactionDescription] The description of the purchase.
     * @param[transactionAmount] The amount of the transaction.
     * @param[withdrawal] Flag defining whether this transaction is a withdrawal.
     * @param[date] The date of the purchase.
     */
    fun update(id: Long, accountName: String, transactionDescription: String, transactionAmount: String, withdrawal: Boolean, date: Date)
}