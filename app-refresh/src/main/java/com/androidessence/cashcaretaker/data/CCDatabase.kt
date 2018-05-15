package com.androidessence.cashcaretaker.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import com.androidessence.cashcaretaker.account.Account
import com.androidessence.cashcaretaker.transaction.Transaction


/**
 * Database class for the CC application.
 */
@Database(entities = [(Account::class), (Transaction::class)], version = 1)
@TypeConverters(Converters::class)
abstract class CCDatabase : RoomDatabase() {
    abstract fun accountDao(): AccountDAO
    abstract fun transactionDao(): TransactionDAO

    companion object {
        private var INSTANCE: CCDatabase? = null

        fun getInMemoryDatabase(context: Context): CCDatabase {
            if (INSTANCE == null) {
                INSTANCE = Room.databaseBuilder(context,
                        CCDatabase::class.java, "cashcaretaker.db")
                        .addCallback(CALLBACK)
                        .build()
            }

            return INSTANCE!!
        }

        //TODO: Find a way to reference this in the test class too without making it public.
        /**
         * This is a database callback that creates the triggers to update account balance based on
         * actions to the transaction table.
         */
        val CALLBACK = object : RoomDatabase.Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)

                db.execSQL(
                        "CREATE TRIGGER update_balance_for_withdrawal " +
                                "AFTER INSERT ON transactionTable " +
                                "WHEN new.withdrawal " +
                                "BEGIN " +
                                "UPDATE account " +
                                "SET balance = balance - new.amount " +
                                "WHERE name = new.accountName; END;"
                )

                db.execSQL(
                        "CREATE TRIGGER update_balance_for_deposit " +
                                "AFTER INSERT ON transactionTable " +
                                "WHEN NOT new.withdrawal " +
                                "BEGIN " +
                                "UPDATE account " +
                                "SET balance = balanAccouce + new.amount " +
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

                db.execSQL(
                        "CREATE TRIGGER update_balance_for_withdrawal_update " +
                                "AFTER UPDATE ON transactionTable " +
                                "WHEN old.withdrawal AND new.withdrawal " +
                                "BEGIN " +
                                "UPDATE account " +
                                "SET balance = (balance + old.amount) - new.amount " +
                                "WHERE name = old.accountName; END;"
                )

                db.execSQL(
                        "CREATE TRIGGER update_balance_for_deposit_update " +
                                "AFTER UPDATE ON transactionTable " +
                                "WHEN NOT old.withdrawal AND NOT new.withdrawal " +
                                "BEGIN " +
                                "UPDATE account " +
                                "SET balance = (balance - old.amount) + new.amount " +
                                "WHERE name = old.accountName; END;"
                )

                db.execSQL(
                        "CREATE TRIGGER update_balance_for_deposit_to_withdrawal_change " +
                                "AFTER UPDATE ON transactionTable " +
                                "WHEN NOT old.withdrawal AND new.withdrawal " +
                                "BEGIN " +
                                "UPDATE account " +
                                "SET BALANCE = (balance - old.amount) - new.amount " +
                                "WHERE name = old.accountName; END;"
                )

                db.execSQL(
                        "CREATE TRIGGER update_balance_for_withdrawal_to_deposit_change " +
                                "AFTER UPDATE ON transactionTable " +
                                "WHEN old.withdrawal AND NOT new.withdrawal " +
                                "BEGIN " +
                                "UPDATE account " +
                                "SET BALANCE = (balance + old.amount) + new.amount " +
                                "WHERE name = old.accountName; END;"
                )
            }
        }
    }
}