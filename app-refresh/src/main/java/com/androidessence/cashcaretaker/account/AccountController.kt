package com.androidessence.cashcaretaker.account

import com.androidessence.cashcaretaker.core.DataController
import com.androidessence.cashcaretaker.data.DataViewState

/**
 * Controller responsible for displaying a list of accounts.
 */
interface AccountController : DataController {
    /**
     * The state the controller should be in while data is being fetched.
     */
    var viewState: DataViewState

    /**
     * A callback method for when one of the transaction buttons on the account view is clicked.
     *
     * @param[account] The account in the row for the button being clicked.
     * @param[withdrawal] Flag defining whether the transaction to create is a withdrawal or not.
     */
    fun onTransactionButtonClicked(account: Account, withdrawal: Boolean)

    /**
     * A callback method for when one of the accounts in the list is clicked.
     *
     * @param[account] The account that the user clicked on.
     */
    fun onAccountSelected(account: Account)

    /**
     * The controller that is displaying a list of accounts must also support navigating to the view
     * to add new ones.
     */
    fun showAddAccountView()
}