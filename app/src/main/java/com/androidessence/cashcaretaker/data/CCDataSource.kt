package com.androidessence.cashcaretaker.data

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import com.androidessence.cashcaretaker.dataTransferObjects.Account
import java.sql.SQLException

/**
 * Data source to read and write from the CC database.
 *
 * Created by adam.mcneilly on 5/21/17.
 */
class CCDataSource(val context: Context) {
    val openHelper = CCDatabaseHelper(context)
    var database: SQLiteDatabase? = null

    @Throws(SQLException::class)
    fun open() {
        database = openHelper.writableDatabase
    }

    fun close() {
        openHelper.close()
    }

    fun getAccounts(
            columns: Array<String>? = null,
            selection: String? = null,
            selectionArgs: Array<String>? = null,
            groupBy: String? = null,
            having: String? = null,
            orderBy: String? = null): List<Account> {

        val accounts = ArrayList<Account>()

        val cursor = database?.query(
                CCContract.AccountEntry.TABLE_NAME,
                columns,
                selection,
                selectionArgs,
                groupBy,
                having,
                orderBy
        )

        while (cursor?.moveToNext() ?: false) {
            accounts.add(Account(cursor))
        }

        cursor?.close()

        return accounts
    }
}