package com.androidessence.cashcaretaker.addtransaction

import com.androidessence.cashcaretaker.App
import com.androidessence.cashcaretaker.data.CCDatabase
import com.androidessence.cashcaretaker.transaction.Transaction

/**
 * Implementation for database interaction.
 */
class AddTransactionInteractorImpl : AddTransactionInteractor {
    override fun insert(transactions: List<Transaction>): List<Long> {
        return CCDatabase.getInMemoryDatabase(App.instance)
                .transactionDao()
                .insert(transactions)
    }
}