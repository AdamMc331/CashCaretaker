package com.androidessence.cashcaretaker.data

import com.androidessence.cashcaretaker.account.Account
import com.androidessence.cashcaretaker.transaction.Transaction
import io.reactivex.Flowable

/**
 * Repository singleton that is used to read and write from the database.
 *
 * @property[accountDAO] Database Access Object to interact with the account table.
 * @property[transactionDAO] Database Access Object to interact with the transaction table.
 */
object CCRepository {
    private lateinit var accountDAO: AccountDAO
    private lateinit var transactionDAO: TransactionDAO

    fun init(database: CCDatabase) {
        accountDAO = database.accountDao()
        transactionDAO = database.transactionDao()
    }

    fun getAllAccounts(): Flowable<List<Account>> = accountDAO.getAll()

    fun deleteAccount(account: Account): Int = accountDAO.delete(account)

    fun insertAccounts(accounts: List<Account>): List<Long> = accountDAO.insert(accounts)

    fun insertTransactions(transactions: List<Transaction>): List<Long> = transactionDAO.insert(transactions)

    fun updateTransaction(transaction: Transaction): Int = transactionDAO.update(transaction)

    fun getTransactionsForAccount(accountName: String): Flowable<List<Transaction>> = transactionDAO.getAllForAccount(accountName)

    fun deleteTransaction(transaction: Transaction): Int = transactionDAO.delete(transaction)
}