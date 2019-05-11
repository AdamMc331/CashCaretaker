package com.androidessence.cashcaretaker.data

import com.androidessence.cashcaretaker.account.Account
import com.androidessence.cashcaretaker.transaction.Transaction
import org.junit.Assert.assertEquals

class CCDatabaseRobot(
        private val database: CCDatabase
) {
    suspend fun deleteAllAccounts(): Int {
        return database.accountDao().deleteAll()
    }

    suspend fun insertAccount(account: Account): Long {
        return database.accountDao().insert(account)
    }

    suspend fun deleteAccount(account: Account): Int {
        return database.accountDao().delete(account)
    }

    fun getFirstAccount(): Account {
        return database.accountDao().getAll().test().values().first().first()
    }

    fun assertAccountsEqual(expectedAccounts: List<Account>) {
        val actualAccounts = database.accountDao().getAll().test().values().first()
        assertEquals(expectedAccounts, actualAccounts)
    }

    suspend fun insertTransaction(transaction: Transaction): Long {
        return database.transactionDao().insert(transaction)
    }

    suspend fun deleteTransaction(transaction: Transaction): Int {
        return database.transactionDao().delete(transaction)
    }

    suspend fun updateTransaction(transaction: Transaction): Int {
        return database.transactionDao().update(transaction)
    }

    fun assertTransactionsForAccount(expectedTransactions: List<Transaction>, accountName: String) {
        val actualTransactions = database.transactionDao().getAllForAccount(accountName).test().values().first()
        assertEquals(expectedTransactions, actualTransactions)
    }

    fun closeDatabase() {
        database.close()
    }
}