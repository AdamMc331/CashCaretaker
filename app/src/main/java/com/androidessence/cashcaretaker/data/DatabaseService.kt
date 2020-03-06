package com.androidessence.cashcaretaker.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.androidessence.cashcaretaker.account.Account
import com.androidessence.cashcaretaker.account.toAccount
import com.androidessence.cashcaretaker.database.CCDatabase
import com.androidessence.cashcaretaker.database.PersistableAccount
import com.androidessence.cashcaretaker.database.PersistableTransaction
import com.androidessence.cashcaretaker.transaction.Transaction
import com.androidessence.cashcaretaker.transaction.toTransaction
import java.util.Date

class DatabaseService(
    private val database: CCDatabase
) : CCRepository {
    override fun getAllAccounts(): LiveData<List<Account>> {
        return database.fetchAllAccounts().map(PersistableAccount::toAccount)
    }

    override suspend fun insertAccount(account: Account): Long {
        return database.insertAccount(account.toPersistableAccount())
    }

    override suspend fun deleteAccount(account: Account): Int {
        return database.deleteAccount(account.toPersistableAccount())
    }

    override fun getTransactionsForAccount(accountName: String): LiveData<List<Transaction>> {
        return database.fetchTransactionsForAccount(accountName)
            .map(PersistableTransaction::toTransaction)
    }

    override suspend fun insertTransaction(transaction: Transaction): Long {
        return database.insertTransaction(transaction.toPersistableTransaction())
    }

    override suspend fun updateTransaction(transaction: Transaction): Int {
        return database.updateTransaction(transaction.toPersistableTransaction())
    }

    override suspend fun deleteTransaction(transaction: Transaction): Int {
        return database.deleteTransaction(transaction.toPersistableTransaction())
    }

    override suspend fun transfer(
        fromAccount: Account,
        toAccount: Account,
        amount: Double,
        date: Date
    ) {
        val withdrawal = Transaction(
            fromAccount.name,
            "Transfer to ${toAccount.name}.",
            amount,
            true,
            date
        ).toPersistableTransaction()

        val deposit = Transaction(
            toAccount.name,
            "Transfer from ${fromAccount.name}.",
            amount,
            false,
            date
        ).toPersistableTransaction()

        database.transferFunds(withdrawal = withdrawal, deposit = deposit)
    }
}

private fun <T, R> LiveData<List<T>>.map(transform: ((T) -> R)): LiveData<List<R>> {
    return Transformations.map(this) { items ->
        items.map {
            transform(it)
        }
    }
}
