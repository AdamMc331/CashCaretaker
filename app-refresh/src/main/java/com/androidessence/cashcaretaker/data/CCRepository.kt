package com.androidessence.cashcaretaker.data

import com.androidessence.cashcaretaker.account.Account
import com.androidessence.cashcaretaker.transaction.Transaction
import io.reactivex.Flowable
import io.reactivex.Single
import java.util.*

/**
 * Repository that connects to a database to insert/update items.
 *
 * TODO: Break this up into an interface, and have a repository for accounts and transactions separately.
 */
open class CCRepository(private val database: CCDatabase) {
    private val accountDAO: AccountDAO = database.accountDao()
    private val transactionDAO: TransactionDAO = database.transactionDao()

    fun getAllAccounts(): Flowable<DataViewState> = accountDAO.getAll()
            .map {
                if (it.isEmpty()) {
                    DataViewState.Empty()
                } else {
                    DataViewState.Success(it)
                }
            }

    fun deleteAccount(account: Account): Int = accountDAO.delete(account)

    fun insertAccount(account: Account): Long = accountDAO.insert(account)

    fun insertTransaction(transaction: Transaction): Long = transactionDAO.insert(transaction)

    fun updateTransaction(transaction: Transaction): Int = transactionDAO.update(transaction)

    fun getTransactionsForAccount(accountName: String): Flowable<DataViewState> = transactionDAO.getAllForAccount(accountName)
            .map {
                if (it.isEmpty()) {
                    DataViewState.Empty()
                } else {
                    DataViewState.Success(it)
                }
            }

    fun deleteTransaction(transaction: Transaction): Int = transactionDAO.delete(transaction)

    /**
     * Transfers money from one account to another.
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
                database.transactionDao().insert(withdrawal)
                database.transactionDao().insert(deposit)
                database.setTransactionSuccessful()
            } finally {
                database.endTransaction()
            }
        }
    }
}