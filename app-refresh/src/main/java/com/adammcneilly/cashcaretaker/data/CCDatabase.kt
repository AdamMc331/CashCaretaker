package com.adammcneilly.cashcaretaker.data

import android.arch.persistence.db.SupportSQLiteDatabase
import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.content.Context
import com.adammcneilly.cashcaretaker.account.Account
import com.adammcneilly.cashcaretaker.transaction.Transaction


/**
 * Database class for the CC application.
 */
@Database(entities = arrayOf(Account::class, Transaction::class), version = 1)
abstract class CCDatabase : RoomDatabase() {
    abstract fun accountDao(): AccountDAO
    abstract fun transactionDao(): TransactionDAO

    companion object {
        private var INSTANCE: CCDatabase? = null
            private set

        fun getInMemoryDatabase(context: Context): CCDatabase {
            if (INSTANCE == null) {
                INSTANCE = Room.databaseBuilder(context,
                        CCDatabase::class.java, "cashcaretaker.db")
                        .addCallback(CALLBACK)
                        .build()
            }

            return INSTANCE!!
        }

        private val CALLBACK = object : RoomDatabase.Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)

                db.execSQL(
                        "CREATE TRIGGER update_balance_for_withdrawal " +
                                "AFTER INSERT ON transactionTable " +
                                "WHEN new.withdrawal " +
                                "BEGIN " +
                                "UPDATE account " +
                                "SET balance = balance - new.amount " +
                                "WHERE name = new.accountName; END;")

                db.execSQL(
                        "CREATE TRIGGER update_balance_for_deposit " +
                                "AFTER INSERT ON transactionTable " +
                                "WHEN NOT new.withdrawal " +
                                "BEGIN " +
                                "UPDATE account " +
                                "SET balance = balance + new.amount " +
                                "WHERE name = new.accountName; END;"
                )

                db.execSQL(
                        "CREATE TRIGGER update_balance_for_withdrawal_delete " +
                                "AFTER DELETE ON transactionTable " +
                                "WHEN old.withdrawal " +
                                "BEGIN " +
                                "UPDATE account " +
                                "SET balance = balance + old.amount " +
                                "WHERE name = old.accountName; END;"
                )

                db.execSQL(
                        "CREATE TRIGGER update_balance_for_deposit_delete " +
                                "AFTER DELETE ON transactionTable " +
                                "WHEN NOT old.withdrawal " +
                                "BEGIN " +
                                "UPDATE account " +
                                "SET balance = balance - old.amount " +
                                "WHERE name = old.accountName; END;"
                )
            }
        }
    }
}