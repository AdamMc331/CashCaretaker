package com.androidessence.cashcaretaker.fakes

import com.androidessence.cashcaretaker.core.models.Account
import com.androidessence.cashcaretaker.core.models.Transaction
import com.androidessence.cashcaretaker.data.CCRepository
import java.util.*
import kotlinx.coroutines.flow.Flow

class FakeCCRepository : CCRepository {
    private var insertAccountCallCount = 0

    override fun fetchAllAccounts(): Flow<List<Account>> {
        TODO("Not yet implemented")
    }

    override suspend fun insertAccount(account: Account): Long {
        insertAccountCallCount++
        return 0L
    }

    override suspend fun deleteAccount(account: Account): Int {
        TODO("Not yet implemented")
    }

    override fun fetchTransactionsForAccount(accountName: String): Flow<List<Transaction>> {
        TODO("Not yet implemented")
    }

    override suspend fun insertTransaction(transaction: Transaction): Long {
        TODO("Not yet implemented")
    }

    override suspend fun updateTransaction(transaction: Transaction): Int {
        TODO("Not yet implemented")
    }

    override suspend fun deleteTransaction(transaction: Transaction): Int {
        TODO("Not yet implemented")
    }

    override suspend fun transfer(
        fromAccount: Account,
        toAccount: Account,
        amount: Double,
        date: Date
    ) {
        TODO("Not yet implemented")
    }

    fun getInsertAccountCallCount(): Int {
        return insertAccountCallCount
    }
}
