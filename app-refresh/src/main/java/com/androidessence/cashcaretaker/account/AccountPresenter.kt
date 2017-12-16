package com.androidessence.cashcaretaker.account

import android.support.v7.view.ActionMode
import com.androidessence.cashcaretaker.core.BasePresenter

/**
 * Defines the behavior of the presentation layer for an account screen.
 */
interface AccountPresenter : BasePresenter {
    /**
     * An account that the user has selected at the moment.
     */
    var selectedAccount: Account?

    /**
     * Action mode that displays when an account is selected.
     */
    var actionMode: ActionMode?

    /**
     * A callback for when the user interacts with the Action Mode.
     */
    var actionModeCallback: ActionMode.Callback

    /**
     * Deletes an account from the database.
     */
    fun deleteSelectedAccount()
}