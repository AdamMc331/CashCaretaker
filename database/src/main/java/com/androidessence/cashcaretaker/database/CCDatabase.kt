package com.androidessence.cashcaretaker.database

import android.content.Context
import androidx.lifecycle.LiveData
import kotlinx.coroutines.flow.Flow

interface CCDatabase {
    fun fetchAllAccountsFlow(): Flow<List<PersistableAccount>>
    suspend fun insertAccount(account: PersistableAccount): Long
    suspend fun deleteAccount(account: PersistableAccount): Int
    suspend fun deleteAllAccounts(): Int

    fun fetchTransactionsForAccount(accountName: String): LiveData<List<PersistableTransaction>>
    suspend fun insertTransaction(transaction: PersistableTransaction): Long
    suspend fun updateTransaction(transaction: PersistableTransaction): Int
    suspend fun deleteTransaction(transaction: PersistableTransaction): Int

    suspend fun transferFunds(
        withdrawal: PersistableTransaction,
        deposit: PersistableTransaction
    )
}

class RoomDatabase(context: Context) : CCDatabase {
    @Suppress("MemberNameEqualsClassName")
    private val roomDatabase = CashCaretakerRoomDatabase.getInMemoryDatabase(context)

    override fun fetchAllAccountsFlow(): Flow<List<PersistableAccount>> {
        return roomDatabase.accountDao().fetchAllAccounts()
    }

    override suspend fun insertAccount(account: PersistableAccount): Long {
        return roomDatabase.accountDao().insert(account)
    }

    override suspend fun deleteAccount(account: PersistableAccount): Int {
        return roomDatabase.accountDao().delete(account)
    }

    override suspend fun deleteAllAccounts(): Int {
        return roomDatabase.accountDao().deleteAll()
    }

    override fun fetchTransactionsForAccount(
        accountName: String
    ): LiveData<List<PersistableTransaction>> {
        return roomDatabase.transactionDao().getAllForAccount(accountName)
    }

    override suspend fun insertTransaction(transaction: PersistableTransaction): Long {
        return roomDatabase.transactionDao().insert(transaction)
    }

    override suspend fun updateTransaction(transaction: PersistableTransaction): Int {
        return roomDatabase.transactionDao().update(transaction)
    }

    override suspend fun deleteTransaction(transaction: PersistableTransaction): Int {
        return roomDatabase.transactionDao().delete(transaction)
    }

    override suspend fun transferFunds(
        withdrawal: PersistableTransaction,
        deposit: PersistableTransaction
    ) {
        return roomDatabase.transactionDao().transfer(withdrawal, deposit)
    }
}
