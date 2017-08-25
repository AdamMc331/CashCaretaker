package com.adammcneilly.cashcaretaker.interactors

/**
 * Interface for adding an account in the database.
 */
interface AddAccountInteractor {
    interface OnInsertedListener {
        fun onInserted(ids: List<Long>)
        fun onInsertConflict()
        fun onAccountNameError()
        fun onAccountBalanceError()
    }

    fun insert(accountName: String, accountBalance: String, listener: OnInsertedListener)
}