package com.androidessence.cashcaretaker.data

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.provider.BaseColumns

import com.androidessence.cashcaretaker.R

/**
 * A class used for creating the Cash Caretaker database.

 * Created by adammcneilly on 10/30/15.
 */
class CCDatabaseHelper(private val context: Context) : SQLiteOpenHelper(context, CCDatabaseHelper.DATABASE_NAME, null, CCDatabaseHelper.DATABASE_VERSION) {

    override fun onCreate(db: SQLiteDatabase) {
        buildAccountTable(db)
        buildCategoryTable(db)
        buildTransactionTable(db)
        addUpdateBalanceForWithdrawalTrigger(db)
        addUpdateBalanceForDepositTrigger(db)
        addTransactionCascadeDeleteTrigger(db)
        addUpdateBalanceForWithdrawalDeleteTrigger(db)
        addUpdateBalanceForDepositDeleteTrigger(db)
        addUpdateBalanceForDepositChangeTrigger(db)
        addUpdateBalanceForWithdrawalChangeTrigger(db)
        addUpdateBalanceForDepositToWithdrawalChangeTrigger(db)
        addUpdateBalanceForWithdrawalToDepositChangeTrigger(db)
        buildRepeatingPeriodTable(db)
        buildRepeatingTransactionTable(db)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        // If we are upgrading to version 2, we need to add our new tables.
        when (newVersion) {
            2 -> {
                // These tables were added in version 2.
                buildRepeatingPeriodTable(db)
                buildRepeatingTransactionTable(db)
            }
            3 -> {
                // If user goes from 1-3, they need old tables
                if (oldVersion == 1) {
                    buildRepeatingPeriodTable(db)
                    buildRepeatingTransactionTable(db)
                }

                // Added editing a transaction, required new triggers.
                addUpdateBalanceForDepositChangeTrigger(db)
                addUpdateBalanceForWithdrawalChangeTrigger(db)
                addUpdateBalanceForDepositToWithdrawalChangeTrigger(db)
                addUpdateBalanceForWithdrawalToDepositChangeTrigger(db)
            }
            4 -> {
                db.execSQL("ALTER TABLE " + CCContract.CategoryEntry.TABLE_NAME + " ADD COLUMN " + CCContract.CategoryEntry.COLUMN_IS_DEFAULT + " INTEGER DEFAULT 0")

                // Update default
                val values = ContentValues()
                values.put(CCContract.CategoryEntry.COLUMN_IS_DEFAULT, 1)
                db.update(
                        CCContract.CategoryEntry.TABLE_NAME,
                        values,
                        CCContract.CategoryEntry.COLUMN_DESCRIPTION + " = ?",
                        arrayOf(context.getString(R.string.default_category))
                )
            }
            else -> {
            }
        }
    }

    //region Build Tables
    /**
     * Creates the Account table.
     */
    private fun buildAccountTable(db: SQLiteDatabase) {
        db.execSQL(
                "CREATE TABLE " + CCContract.AccountEntry.TABLE_NAME + " (" +
                        BaseColumns._ID + " INTEGER PRIMARY KEY, " +
                        CCContract.AccountEntry.COLUMN_NAME + " TEXT UNIQUE NOT NULL, " +
                        CCContract.AccountEntry.COLUMN_BALANCE + " REAL NOT NULL);"
        )
    }

    /**
     * Creates the Category table.
     */
    private fun buildCategoryTable(db: SQLiteDatabase) {
        db.execSQL(
                "CREATE TABLE " + CCContract.CategoryEntry.TABLE_NAME + " (" +
                        BaseColumns._ID + " INTEGER PRIMARY KEY, " +
                        CCContract.CategoryEntry.COLUMN_DESCRIPTION + " TEXT UNIQUE NOT NULL, " +
                        CCContract.CategoryEntry.COLUMN_IS_DEFAULT + " INTEGER DEFAULT 0);"
        )

        insertDefaultCategories(db)
    }

    /**
     * Inserts the initial categories into the database.
     */
    private fun insertDefaultCategories(db: SQLiteDatabase) {
        var colValues = "('" + context.getString(R.string.default_category) + "', 1), "
        val categories = context.resources.getStringArray(R.array.default_categories)
        for (i in categories.indices) {
            colValues += "('" + categories[i] + "', 0)"

            // If it's not the last row, add column
            if (i != categories.size - 1) {
                colValues += ", "
            }
        }

        db.execSQL("INSERT INTO " + CCContract.CategoryEntry.TABLE_NAME + " " +
                "(" + CCContract.CategoryEntry.COLUMN_DESCRIPTION + ", " + CCContract.CategoryEntry.COLUMN_IS_DEFAULT + ") " +
                "VALUES " +
                colValues + ";"
        )
    }

    /**
     * Creates the Transaction table.
     */
    private fun buildTransactionTable(db: SQLiteDatabase) {
        db.execSQL(
                "CREATE TABLE " + CCContract.TransactionEntry.TABLE_NAME + " (" +
                        BaseColumns._ID + " INTEGER PRIMARY KEY, " +
                        CCContract.TransactionEntry.COLUMN_DESCRIPTION + " TEXT NOT NULL, " +
                        CCContract.TransactionEntry.COLUMN_AMOUNT + " REAL NOT NULL, " +
                        CCContract.TransactionEntry.COLUMN_NOTES + " TEXT DEFAULT '', " +
                        CCContract.TransactionEntry.COLUMN_DATE + " TEXT NOT NULL, " +
                        CCContract.TransactionEntry.COLUMN_CATEGORY + " INTEGER NOT NULL, " +
                        CCContract.TransactionEntry.COLUMN_WITHDRAWAL + " INTEGER NOT NULL, " +
                        CCContract.TransactionEntry.COLUMN_ACCOUNT + " INTEGER NOT NULL, " +
                        "FOREIGN KEY (" + CCContract.TransactionEntry.COLUMN_CATEGORY + ") " +
                        "REFERENCES " + CCContract.CategoryEntry.TABLE_NAME + " (" + BaseColumns._ID + "), " +
                        "FOREIGN KEY (" + CCContract.TransactionEntry.COLUMN_ACCOUNT + ") " +
                        "REFERENCES " + CCContract.AccountEntry.TABLE_NAME + " (" + BaseColumns._ID + " ));"
        )
    }

    /**
     * Builds the repeating transaction table and inserts default values of monthly and yearly.
     */
    private fun buildRepeatingPeriodTable(db: SQLiteDatabase) {
        db.execSQL(
                "CREATE TABLE " + CCContract.RepeatingPeriodEntry.TABLE_NAME + " (" +
                        BaseColumns._ID + " INTEGER PRIMARY KEY, " +
                        CCContract.RepeatingPeriodEntry.COLUMN_NAME + " TEXT UNIQUE NOT NULL);"
        )

        db.execSQL(
                "INSERT INTO " + CCContract.RepeatingPeriodEntry.TABLE_NAME + " " +
                        "(" + BaseColumns._ID + ", " + CCContract.RepeatingPeriodEntry.COLUMN_NAME + ") " +
                        " VALUES " +
                        "(1, 'Monthly'), (2, 'Yearly');"
        )
    }

    /**
     * Builds the repeating transaction table.
     */
    private fun buildRepeatingTransactionTable(db: SQLiteDatabase) {
        db.execSQL(
                "CREATE TABLE " + CCContract.RepeatingTransactionEntry.TABLE_NAME + " (" +
                        BaseColumns._ID + " INTEGER PRIMARY KEY, " +
                        CCContract.RepeatingTransactionEntry.COLUMN_REPEATING_PERIOD + " INTEGER NOT NULL, " +
                        CCContract.RepeatingTransactionEntry.COLUMN_ACCOUNT + " INTEGER NOT NULL, " +
                        CCContract.RepeatingTransactionEntry.COLUMN_DESCRIPTION + " TEXT NOT NULL, " +
                        CCContract.RepeatingTransactionEntry.COLUMN_AMOUNT + " REAL NOT NULL, " +
                        CCContract.RepeatingTransactionEntry.COLUMN_NOTES + " TEXT DEFAULT '', " +
                        CCContract.RepeatingTransactionEntry.COLUMN_NEXT_DATE + " TEXT NOT NULL, " +
                        CCContract.RepeatingTransactionEntry.COLUMN_CATEGORY + " INTEGER NOT NULL, " +
                        CCContract.RepeatingTransactionEntry.COLUMN_WITHDRAWAL + " INTEGER NOT NULL, " +
                        "FOREIGN KEY (" + CCContract.RepeatingTransactionEntry.COLUMN_REPEATING_PERIOD + ") " +
                        "REFERENCES " + CCContract.RepeatingPeriodEntry.TABLE_NAME + " (" + BaseColumns._ID + "), " +
                        "FOREIGN KEY (" + CCContract.RepeatingTransactionEntry.COLUMN_ACCOUNT + ") " +
                        "REFERENCES " + CCContract.AccountEntry.TABLE_NAME + " (" + BaseColumns._ID + "), " +
                        "FOREIGN KEY (" + CCContract.RepeatingTransactionEntry.COLUMN_CATEGORY + ") " +
                        "REFERENCES " + CCContract.CategoryEntry.TABLE_NAME + " (" + BaseColumns._ID + "));"
        )
    }
    //endregion

    //region riggers
    /**
     * Creates a trigger that updates the Account balance any time a withdrawal is inserted.
     */
    private fun addUpdateBalanceForWithdrawalTrigger(db: SQLiteDatabase) {
        db.execSQL(
                "CREATE TRIGGER update_balance_for_withdrawal " +
                        "AFTER INSERT ON " + CCContract.TransactionEntry.TABLE_NAME + " " +
                        "WHEN new." + CCContract.TransactionEntry.COLUMN_WITHDRAWAL + " " +
                        "BEGIN " +
                        "UPDATE " + CCContract.AccountEntry.TABLE_NAME + " " +
                        "SET " + CCContract.AccountEntry.COLUMN_BALANCE + " = " + CCContract.AccountEntry.COLUMN_BALANCE + " - new." + CCContract.TransactionEntry.COLUMN_AMOUNT + " " +
                        "WHERE " + BaseColumns._ID + " = new." + CCContract.TransactionEntry.COLUMN_ACCOUNT + "; END;"
        )
    }

    /**
     * Creates a trigger that updates the Account balance any time a deposit is inserted.
     */
    private fun addUpdateBalanceForDepositTrigger(db: SQLiteDatabase) {
        db.execSQL(
                "CREATE TRIGGER update_balance_for_deposit " +
                        "AFTER INSERT ON " + CCContract.TransactionEntry.TABLE_NAME + " " +
                        "WHEN NOT new." + CCContract.TransactionEntry.COLUMN_WITHDRAWAL + " " +
                        "BEGIN " +
                        "UPDATE " + CCContract.AccountEntry.TABLE_NAME + " " +
                        "SET " + CCContract.AccountEntry.COLUMN_BALANCE + " = " + CCContract.AccountEntry.COLUMN_BALANCE + " + new." + CCContract.TransactionEntry.COLUMN_AMOUNT + " " +
                        "WHERE " + BaseColumns._ID + " = new." + CCContract.TransactionEntry.COLUMN_ACCOUNT + "; END;"
        )
    }

    /**
     * Creates a trigger that deletes all transactions from an Account before deleting the Account.
     */
    private fun addTransactionCascadeDeleteTrigger(db: SQLiteDatabase) {
        db.execSQL(
                "CREATE TRIGGER delete_transactions_for_account " +
                        "BEFORE DELETE ON " + CCContract.AccountEntry.TABLE_NAME + " " +
                        "FOR EACH ROW BEGIN " +
                        "DELETE FROM " + CCContract.TransactionEntry.TABLE_NAME + " " +
                        "WHERE " + CCContract.TransactionEntry.COLUMN_ACCOUNT + " = old." + BaseColumns._ID + "; END;"
        )
    }

    /**
     * Creates a trigger that updates the Account balance whenever a Withdrawal is deleted.
     */
    private fun addUpdateBalanceForWithdrawalDeleteTrigger(db: SQLiteDatabase) {
        db.execSQL(
                "CREATE TRIGGER update_balance_for_withdrawal_delete " +
                        "AFTER DELETE ON " + CCContract.TransactionEntry.TABLE_NAME + " " +
                        "WHEN old." + CCContract.TransactionEntry.COLUMN_WITHDRAWAL + " " +
                        "BEGIN " +
                        "UPDATE " + CCContract.AccountEntry.TABLE_NAME + " " +
                        "SET " + CCContract.AccountEntry.COLUMN_BALANCE + " = " + CCContract.AccountEntry.COLUMN_BALANCE + " + old." + CCContract.TransactionEntry.COLUMN_AMOUNT + " " +
                        "WHERE " + BaseColumns._ID + " = old." + CCContract.TransactionEntry.COLUMN_ACCOUNT + "; END;"
        )
    }

    /**
     * Creates a trigger that updates the Account balance whenever a Deposit is deleted.
     */
    private fun addUpdateBalanceForDepositDeleteTrigger(db: SQLiteDatabase) {
        db.execSQL(
                "CREATE TRIGGER update_balance_for_deposit_delete " +
                        "AFTER DELETE ON " + CCContract.TransactionEntry.TABLE_NAME + " " +
                        "WHEN NOT old." + CCContract.TransactionEntry.COLUMN_WITHDRAWAL + " " +
                        "BEGIN " +
                        "UPDATE " + CCContract.AccountEntry.TABLE_NAME + " " +
                        "SET " + CCContract.AccountEntry.COLUMN_BALANCE + " = " + CCContract.AccountEntry.COLUMN_BALANCE + " - old." + CCContract.TransactionEntry.COLUMN_AMOUNT + " " +
                        "WHERE " + BaseColumns._ID + " = old." + CCContract.TransactionEntry.COLUMN_ACCOUNT + "; END;"
        )
    }

    /**
     * Updates the account balance any time a transaction is updated from a deposit to a withdrawal.

     * The new balance is balance - old amount for deposit - new amount for withdrawal
     */
    private fun addUpdateBalanceForDepositToWithdrawalChangeTrigger(db: SQLiteDatabase) {
        db.execSQL(
                "CREATE TRIGGER update_balance_for_deposit_to_withdrawal_change " +
                        "AFTER UPDATE ON " + CCContract.TransactionEntry.TABLE_NAME + " " +
                        "WHEN NOT old." + CCContract.TransactionEntry.COLUMN_WITHDRAWAL + " AND new." + CCContract.TransactionEntry.COLUMN_WITHDRAWAL + " " +
                        "BEGIN " +
                        "UPDATE " + CCContract.AccountEntry.TABLE_NAME + " " +
                        "SET " + CCContract.AccountEntry.COLUMN_BALANCE + " = " + CCContract.AccountEntry.COLUMN_BALANCE + " - old." + CCContract.TransactionEntry.COLUMN_AMOUNT + " - new." + CCContract.TransactionEntry.COLUMN_AMOUNT + " " +
                        "WHERE " + BaseColumns._ID + " = new." + CCContract.TransactionEntry.COLUMN_ACCOUNT + "; END;"
        )
    }

    /**
     * Updates the account balance any time a transaction is updated from a withdrawal to a deposit.

     * The new balance is balance + old amount for withdrawal + new amount for deposit
     */
    private fun addUpdateBalanceForWithdrawalToDepositChangeTrigger(db: SQLiteDatabase) {
        db.execSQL(
                "CREATE TRIGGER update_balance_for_withdrawal_to_deposit_change " +
                        "AFTER UPDATE ON " + CCContract.TransactionEntry.TABLE_NAME + " " +
                        "WHEN old." + CCContract.TransactionEntry.COLUMN_WITHDRAWAL + " AND NOT new." + CCContract.TransactionEntry.COLUMN_WITHDRAWAL + " " +
                        "BEGIN " +
                        "UPDATE " + CCContract.AccountEntry.TABLE_NAME + " " +
                        "SET " + CCContract.AccountEntry.COLUMN_BALANCE + " = " + CCContract.AccountEntry.COLUMN_BALANCE + " + old." + CCContract.TransactionEntry.COLUMN_AMOUNT + " + new." + CCContract.TransactionEntry.COLUMN_AMOUNT + " " +
                        "WHERE " + BaseColumns._ID + " = new." + CCContract.TransactionEntry.COLUMN_ACCOUNT + "; END;"
        )
    }

    /**
     * Updates the account balance any time a transaction deposit balance changes.

     * For deposits, the new balance is balance - old amount + new amount
     */
    private fun addUpdateBalanceForDepositChangeTrigger(db: SQLiteDatabase) {
        db.execSQL(
                "CREATE TRIGGER update_balance_for_deposit_amount_change " +
                        "AFTER UPDATE ON " + CCContract.TransactionEntry.TABLE_NAME + " " +
                        "WHEN NOT old." + CCContract.TransactionEntry.COLUMN_WITHDRAWAL + " AND NOT new." + CCContract.TransactionEntry.COLUMN_WITHDRAWAL + " " +
                        "BEGIN " +
                        "UPDATE " + CCContract.AccountEntry.TABLE_NAME + " " +
                        "SET " + CCContract.AccountEntry.COLUMN_BALANCE + " = " + CCContract.AccountEntry.COLUMN_BALANCE + " - old." + CCContract.TransactionEntry.COLUMN_AMOUNT + " + new." + CCContract.TransactionEntry.COLUMN_AMOUNT + " " +
                        "WHERE " + BaseColumns._ID + " = old." + CCContract.TransactionEntry.COLUMN_ACCOUNT + "; END"
        )
    }

    /**
     * Updates the account balance any time a transaction withdrawal balance changes.

     * For withdrawal, the new balance is balance + old amount - new amount
     */
    private fun addUpdateBalanceForWithdrawalChangeTrigger(db: SQLiteDatabase) {
        db.execSQL(
                "CREATE TRIGGER update_balance_for_withdrawal_amount_change " +
                        "AFTER UPDATE ON " + CCContract.TransactionEntry.TABLE_NAME + " " +
                        "WHEN old." + CCContract.TransactionEntry.COLUMN_WITHDRAWAL + " AND new." + CCContract.TransactionEntry.COLUMN_WITHDRAWAL + " " +
                        "BEGIN " +
                        "UPDATE " + CCContract.AccountEntry.TABLE_NAME + " " +
                        "SET " + CCContract.AccountEntry.COLUMN_BALANCE + " = " + CCContract.AccountEntry.COLUMN_BALANCE + " + old." + CCContract.TransactionEntry.COLUMN_AMOUNT + " - new." + CCContract.TransactionEntry.COLUMN_AMOUNT + " " +
                        "WHERE " + BaseColumns._ID + " = old." + CCContract.TransactionEntry.COLUMN_ACCOUNT + "; END"
        )
    }

    companion object {
        private val DATABASE_NAME = "cashcaretaker.db"
        private val DATABASE_VERSION = 4
    }
    //endregion
}
