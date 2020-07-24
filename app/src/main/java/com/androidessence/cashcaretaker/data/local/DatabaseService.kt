package com.androidessence.cashcaretaker.data.local

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.androidessence.cashcaretaker.core.models.Account
import com.androidessence.cashcaretaker.core.models.Transaction
import com.androidessence.cashcaretaker.core.models.toAccount
import com.androidessence.cashcaretaker.core.models.toTransaction
import com.androidessence.cashcaretaker.data.CCRepository
import com.androidessence.cashcaretaker.data.DispatcherProvider
import com.androidessence.cashcaretaker.database.CCDatabase
import com.androidessence.cashcaretaker.database.PersistableAccount
import com.androidessence.cashcaretaker.database.PersistableTransaction
import java.util.Date
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class DatabaseService(
    private val database: CCDatabase,
    private val dispatcherProvider: DispatcherProvider
) : CCRepository {
    override fun fetchAllAccounts(): Flow<List<Account>> {
        return database.fetchAllAccountsFlow().map { persistableAccounts ->
            persistableAccounts.map(PersistableAccount::toAccount)
        }
            .flowOn(dispatcherProvider.ioDispatcher)
    }

    override suspend fun insertAccount(account: Account): Long {
        return withContext(dispatcherProvider.ioDispatcher) {
            database.insertAccount(account.toPersistableAccount())
        }
    }

    override suspend fun deleteAccount(account: Account): Int {
        return withContext(dispatcherProvider.ioDispatcher) {
            database.deleteAccount(account.toPersistableAccount())
        }
    }

    override fun fetchTransactionsForAccount(accountName: String): Flow<List<Transaction>> {
        return database.fetchTransactionsForAccount(accountName).map { transactions ->
            transactions.map(PersistableTransaction::toTransaction)
        }
            .flowOn(dispatcherProvider.ioDispatcher)
    }

    override suspend fun insertTransaction(transaction: Transaction): Long {
        return withContext(dispatcherProvider.ioDispatcher) {
            database.insertTransaction(transaction.toPersistableTransaction())
        }
    }

    override suspend fun updateTransaction(transaction: Transaction): Int {
        return withContext(dispatcherProvider.ioDispatcher) {
            database.updateTransaction(transaction.toPersistableTransaction())
        }
    }

    override suspend fun deleteTransaction(transaction: Transaction): Int {
        return withContext(dispatcherProvider.ioDispatcher) {
            database.deleteTransaction(transaction.toPersistableTransaction())
        }
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

        withContext(dispatcherProvider.ioDispatcher) {
            database.transferFunds(withdrawal = withdrawal, deposit = deposit)
        }
    }
}
