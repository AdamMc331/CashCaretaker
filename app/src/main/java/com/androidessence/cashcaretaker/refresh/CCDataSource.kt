package com.androidessence.cashcaretaker.refresh

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import com.androidessence.cashcaretaker.data.CCContract
import com.androidessence.cashcaretaker.data.CCDatabaseHelper
import java.util.*

/**
 * Data source to access the CC database.
 *
 * Created by adam.mcneilly on 2/19/17.
 */
class CCDataSource(context: Context) {

    private var database: SQLiteDatabase? = null
    private var helper : CCDatabaseHelper? = null

    init {
        helper = CCDatabaseHelper(context)
    }

    fun open() {
        database = helper?.writableDatabase
    }

    fun close() {
        helper?.close()
    }

    fun getAccounts(): List<Account> {
        val accounts = ArrayList<Account>()

        val cursor = database?.query(
                CCContract.AccountEntry.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                null
        )

        while (cursor?.moveToNext().orFalse()) {
            accounts.add(Account(cursor!!))
        }

        cursor?.close()

        return accounts
    }

    fun addAccount(account: Account): Account? {
        val id = database?.insertOrThrow(CCContract.AccountEntry.TABLE_NAME, null, account.getContentValues())

        account.id = id!!
        return account
    }

    fun deleteAccounts(): Int? {
        return database?.delete(CCContract.AccountEntry.TABLE_NAME, null, null)
    }

    fun getTransactions(account: Account): List<Transaction> {
        val transactions = ArrayList<Transaction>()

        var cursor = database?.query(
                CCContract.TransactionEntry.TABLE_NAME,
                null,
                CCContract.TransactionEntry.COLUMN_ACCOUNT + " = ?",
                arrayOf(account.id.toString()),
                null,
                null,
                null
        )

        while (cursor?.moveToNext().orFalse()) {
            transactions.add(Transaction(cursor!!))
        }

        cursor?.close()

        return transactions
    }

    fun addTransaction(transaction: Transaction): Long? {
        return database?.insert(CCContract.TransactionEntry.TABLE_NAME, null, transaction.getContentValues())
    }

    fun deleteTransaction(transaction: Transaction): Int? {
        return database?.delete(
                CCContract.TransactionEntry.TABLE_NAME,
                CCContract.TransactionEntry._ID + " = ?",
                arrayOf(transaction.id.toString())
        )
    }
}