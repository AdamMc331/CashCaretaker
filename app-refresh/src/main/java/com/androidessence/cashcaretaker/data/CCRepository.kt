package com.androidessence.cashcaretaker.data

import android.database.sqlite.SQLiteConstraintException
import com.androidessence.cashcaretaker.account.Account
import com.androidessence.cashcaretaker.transaction.Transaction
import io.reactivex.Flowable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.*

/**
 * Repository singleton that is used to read and write from the database.
 *
 * @property[database] The local database that this repository will interact with.
 * @property[accountDAO] Database Access Object to interact with the account table.
 * @property[transactionDAO] Database Access Object to interact with the transaction table.
 */
object CCRepository {
    private lateinit var database: CCDatabase

    private val accountDAO: AccountDAO
        get() = database.accountDao()
    private val transactionDAO: TransactionDAO
        get() = database.transactionDao()

    fun init(database: CCDatabase) {
        this.database = database
    }

    fun getAllAccounts(): Flowable<List<Account>> = accountDAO.getAll()

    fun deleteAccount(account: Account): Int = accountDAO.delete(account)

    fun insertAccounts(accounts: List<Account>): List<Long> = accountDAO.insert(accounts)

    fun insertTransactions(transactions: List<Transaction>): List<Long> = transactionDAO.insert(transactions)

    fun updateTransaction(transaction: Transaction): Int = transactionDAO.update(transaction)

    fun getTransactionsForAccount(accountName: String): Flowable<List<Transaction>> = transactionDAO.getAllForAccount(accountName)

    fun deleteTransaction(transaction: Transaction): Int = transactionDAO.delete(transaction)

    /**
     * Transfers money from one account to another.
     *
     * @param[fromAccount] The account that has the money to be transferred.
     * @param[toAccount] The account that we are transferring money to.
     * @param[amount] The amount of money to transfer.
     * @param[date] The date of the transfer.
     * @return True if creating the transfer transactions was successful, false otherwise.
     *
     * TODO: Use string template, don't hardcode english here.
     */
    fun transfer(fromAccount: Account, toAccount: Account, amount: Double, date: Date): Single<Unit> {
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

        // We can unwrap balance here because we return if it's null.
        return Single.fromCallable {
                    database.beginTransaction()
                    try {
                        database.transactionDao().insert(listOf(withdrawal, deposit))
                        database.setTransactionSuccessful()
                    } finally {
                        database.endTransaction()
                    }
                }
    }
}