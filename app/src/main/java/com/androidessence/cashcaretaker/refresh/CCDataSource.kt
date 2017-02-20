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

    fun addAccount(account: Account): Long? {
        return database?.insertOrThrow(CCContract.AccountEntry.TABLE_NAME, null, account.getContentValues())
    }

    fun deleteAccounts(): Int? {
        return database?.delete(CCContract.AccountEntry.TABLE_NAME, null, null)
    }
}