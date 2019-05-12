package com.androidessence.cashcaretaker.data

import com.androidessence.cashcaretaker.account.Account
import com.androidessence.cashcaretaker.transaction.Transaction
import io.reactivex.Flowable
import java.util.Date

interface CCRepository {
    fun getAllAccounts(): Flowable<DataViewState>
    suspend fun insertAccount(account: Account): Long
    suspend fun deleteAccount(account: Account): Int

    fun getTransactionsForAccount(accountName: String): Flowable<DataViewState>
    suspend fun insertTransaction(transaction: Transaction): Long
    suspend fun updateTransaction(transaction: Transaction): Int
    suspend fun deleteTransaction(transaction: Transaction): Int

    suspend fun transfer(fromAccount: Account, toAccount: Account, amount: Double, date: Date)
}