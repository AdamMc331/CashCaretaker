package com.adammcneilly.cashcaretaker.account

/**
 * Interface for handling account loading.
 */
interface AccountInteractor {
    interface OnFinishedListener {
        fun onFetched(accounts: List<Account>)
    }

    fun getAll(listener: OnFinishedListener)
}