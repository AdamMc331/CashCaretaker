package com.androidessence.cashcaretaker.data

import androidx.lifecycle.LiveData
import com.androidessence.cashcaretaker.account.Account
import com.androidessence.cashcaretaker.transaction.Transaction
import java.util.Date

/**
 * Repository that connects to a database to insert/update items.
 */
open class CCDatabaseService(
    database: CCDatabase
) : CCRepository {
    private val accountDAO: AccountDAO = database.accountDao()
    private val transactionDAO: TransactionDAO = database.transactionDao()

    override fun getAllAccounts(): LiveData<List<Account>> = accountDAO.getAll()

    override suspend fun deleteAccount(account: Account): Int = accountDAO.delete(account)

    override suspend fun insertAccount(account: Account): Long = accountDAO.insert(account)

    override suspend fun insertTransaction(transaction: Transaction): Long = transactionDAO.insert(transaction)

    override suspend fun updateTransaction(transaction: Transaction): Int = transactionDAO.update(transaction)

    override fun getTransactionsForAccount(accountName: String): LiveData<List<Transaction>> = transactionDAO.getAllForAccount(accountName)

    override suspend fun deleteTransaction(transaction: Transaction): Int = transactionDAO.delete(transaction)

    /**
     * Transfers money from one account to another.
     * TODO: Use string template, don't hardcode english here.
     */
    override suspend fun transfer(fromAccount: Account, toAccount: Account, amount: Double, date: Date) {
        val withdrawal = Transaction(
                fromAccount.name,
                "Transfer to ${toAccount.name}.",
                amount,
                true,
                date
        )

        val deposit = Transaction(
                toAccount.name,
                "Transfer from ${fromAccount.name}.",
                amount,
                false,
                date
        )

        transactionDAO.transfer(withdrawal, deposit)
    }
}