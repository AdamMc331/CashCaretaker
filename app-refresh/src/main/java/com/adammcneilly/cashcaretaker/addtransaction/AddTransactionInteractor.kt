package com.adammcneilly.cashcaretaker.addtransaction

import com.adammcneilly.cashcaretaker.transaction.Transaction

/**
 * Interface for adding a transaction in the database.
 */
interface AddTransactionInteractor {
    fun insert(transactions: List<Transaction>): List<Long>
}