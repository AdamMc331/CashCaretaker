package com.androidessence.cashcaretaker.data

import androidx.lifecycle.LiveData
import com.androidessence.cashcaretaker.account.Account
import com.androidessence.cashcaretaker.transaction.Transaction
import java.util.Date

interface CCRepository {
    fun getAllAccounts(): LiveData<List<Account>>
    suspend fun insertAccount(account: Account): Long
    suspend fun deleteAccount(account: Account): Int

    fun getTransactionsForAccount(accountName: String): LiveData<List<Transaction>>
    suspend fun insertTransaction(transaction: Transaction): Long
    suspend fun updateTransaction(transaction: Transaction): Int
    suspend fun deleteTransaction(transaction: Transaction): Int

    suspend fun transfer(fromAccount: Account, toAccount: Account, amount: Double, date: Date)
}
