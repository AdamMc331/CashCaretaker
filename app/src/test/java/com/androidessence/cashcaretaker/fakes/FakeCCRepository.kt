package com.androidessence.cashcaretaker.fakes

import com.androidessence.cashcaretaker.core.models.Account
import com.androidessence.cashcaretaker.core.models.Transaction
import com.androidessence.cashcaretaker.data.CCRepository
import java.util.Date
import kotlinx.coroutines.flow.Flow

class FakeCCRepository : CCRepository {
    private var insertAccountCallCount = 0
    private var insertTransactionCallCount = 0
    private var updateTransactionCallCount = 0

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
        insertTransactionCallCount++
        return 0L
    }

    override suspend fun updateTransaction(transaction: Transaction): Int {
        updateTransactionCallCount++
        return 0
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

    fun getInsertTransactionCallCount(): Int {
        return insertTransactionCallCount
    }

    fun getUpdateTransactionCallCount(): Int {
        return updateTransactionCallCount
    }
}
