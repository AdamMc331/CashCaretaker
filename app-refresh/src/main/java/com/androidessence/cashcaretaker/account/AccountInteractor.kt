package com.androidessence.cashcaretaker.account

import io.reactivex.Flowable

/**
 * Interface for handling the data layer of account access.
 */
interface AccountInteractor {

    /**
     * Fetches all accounts in the database.
     *
     * @return An RxJava Flowable stream of the account list that was found.
     */
    fun getAll(): Flowable<List<Account>>

    /**
     * Deletes an account from the database.
     *
     * @param[account] The account that is being deleted.
     * @return The number of rows that were removed.
     */
    fun delete(account: Account): Int
}