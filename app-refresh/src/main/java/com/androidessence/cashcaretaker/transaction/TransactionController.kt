package com.androidessence.cashcaretaker.transaction

import com.androidessence.cashcaretaker.core.DataController
import com.androidessence.cashcaretaker.data.DataViewState

/**
 * View for displaying a list of transactions.
 */
interface TransactionController : DataController {
    /**
     * The state the controller should be in while data is being fetched.
     */
    var viewState: DataViewState

    /**
     * Callback method for long clicking on a transaction in the list.
     *
     * @param[transaction] The specific transaction that was pressed.
     */
    fun onTransactionLongClicked(transaction: Transaction)

    /**
     * Displays a view to add a new transaction.
     */
    fun showAddTransaction()

    /**
     * Displays a view to edit an existing transaction.
     *
     * @param[transaction] The transaction to edit.
     */
    fun showEditTransaction(transaction: Transaction)
}