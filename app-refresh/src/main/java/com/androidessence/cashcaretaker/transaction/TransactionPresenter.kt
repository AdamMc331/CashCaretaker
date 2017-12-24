package com.androidessence.cashcaretaker.transaction

import android.support.v7.view.ActionMode
import com.androidessence.cashcaretaker.account.Account
import com.androidessence.cashcaretaker.core.BasePresenter

/**
 * Defines the behavior for a transaction view.
 */
interface TransactionPresenter : BasePresenter {
    /**
     * A transaction that the user has selected at the moment.
     */
    var selectedTransaction: Transaction?

    /**
     * Action mode that displays when a transaction is selected.
     */
    var actionMode: ActionMode?

    /**
     * A callback for when the user interacts with the Action Mode.
     */
    var actionModeCallback: ActionMode.Callback

    /**
     * Deletes a transaction from the database.
     */
    fun deleteSelectedTransaction()
}