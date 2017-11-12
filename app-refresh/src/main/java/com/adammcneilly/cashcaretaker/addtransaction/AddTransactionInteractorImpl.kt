package com.adammcneilly.cashcaretaker.addtransaction

import com.adammcneilly.cashcaretaker.App
import com.adammcneilly.cashcaretaker.data.CCDatabase
import com.adammcneilly.cashcaretaker.transaction.Transaction

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