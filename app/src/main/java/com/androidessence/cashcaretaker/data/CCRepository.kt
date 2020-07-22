package com.androidessence.cashcaretaker.data

import com.androidessence.cashcaretaker.core.models.Account
import com.androidessence.cashcaretaker.core.models.Transaction
import java.util.Date
import kotlinx.coroutines.flow.Flow

interface CCRepository {
    fun fetchAllAccounts(): Flow<List<Account>>
    suspend fun insertAccount(account: Account): Long
    suspend fun deleteAccount(account: Account): Int

    fun fetchTransactionsForAccount(accountName: String): Flow<List<Transaction>>
    suspend fun insertTransaction(transaction: Transaction): Long
    suspend fun updateTransaction(transaction: Transaction): Int
    suspend fun deleteTransaction(transaction: Transaction): Int

    suspend fun transfer(fromAccount: Account, toAccount: Account, amount: Double, date: Date)
}
