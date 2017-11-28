package com.androidessence.cashcaretaker.account

import io.reactivex.Flowable

/**
 * Interface for handling account loading.
 */
interface AccountInteractor {
    fun getAll(): Flowable<List<Account>>
}