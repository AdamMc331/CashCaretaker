package com.androidessence.cashcaretaker

import android.content.ContentUris
import android.content.ContentValues
import android.database.Cursor
import android.provider.BaseColumns
import android.test.AndroidTestCase
import com.androidessence.cashcaretaker.data.CCContract
import junit.framework.Assert

/**
 * Test cases the ensure the quality of the Content Provider for Cash Caretaker.

 * Created by adammcneilly on 11/1/15.
 */
class TestProvider : AndroidTestCase() {

    @Throws(Exception::class)
    override fun setUp() {
        super.setUp()
        testDeleteAllRecords()
    }

    @Throws(Exception::class)
    override fun tearDown() {
        testDeleteAllRecords()
        super.tearDown()
    }

    /**
     * Deletes all records from the database and ensures they were deleted.
     */
    fun testDeleteAllRecords() {
        // Delete transactions
        mContext.contentResolver.delete(
                CCContract.TransactionEntry.CONTENT_URI,
                null, null
        )

        var cursor: Cursor = mContext.contentResolver.query(
                CCContract.TransactionEntry.CONTENT_URI, null, null, null, null
        )
        Assert.assertNotNull(cursor)
        Assert.assertEquals(0, cursor.count)
        cursor.close()

        // Delete repeating transactions
        mContext.contentResolver.delete(
                CCContract.RepeatingTransactionEntry.CONTENT_URI, null, null
        )

        cursor = mContext.contentResolver.query(
                CCContract.RepeatingTransactionEntry.CONTENT_URI, null, null, null, null
        )
        Assert.assertNotNull(cursor)
        Assert.assertEquals(0, cursor.count)
        cursor.close()

        // Delete accounts
        mContext.contentResolver.delete(
                CCContract.AccountEntry.CONTENT_URI, null, null
        )

        // Verify deletion
        cursor = mContext.contentResolver.query(
                CCContract.AccountEntry.CONTENT_URI, null, null, null, null
        )
        Assert.assertNotNull(cursor)
        Assert.assertEquals(0, cursor.count)
        cursor.close()
    }

    /**
     * Verifies the response of the `getType()` method required by content provider for each of the supported URIs in this application.
     */
    fun testGetType() {
        //-- ACCOUNT --//
        var type: String = mContext.contentResolver.getType(CCContract.AccountEntry.CONTENT_URI)
        Assert.assertEquals(CCContract.AccountEntry.CONTENT_TYPE, type)

        //-- ACCOUNT_ID --//
        type = mContext.contentResolver.getType(CCContract.AccountEntry.buildAccountUri(0))
        Assert.assertEquals(CCContract.AccountEntry.CONTENT_ITEM_TYPE, type)

        //-- CATEGORY --//
        type = mContext.contentResolver.getType(CCContract.CategoryEntry.CONTENT_URI)
        Assert.assertEquals(CCContract.CategoryEntry.CONTENT_TYPE, type)

        //-- TRANSACTION --//
        type = mContext.contentResolver.getType(CCContract.TransactionEntry.CONTENT_URI)
        Assert.assertEquals(CCContract.TransactionEntry.CONTENT_TYPE, type)

        //-- TRANSACTION_ID --//
        type = mContext.contentResolver.getType(CCContract.TransactionEntry.buildTransactionUri(0))
        Assert.assertEquals(CCContract.TransactionEntry.CONTENT_ITEM_TYPE, type)

        //-- TRANSACTION_FOR_ACCOUNT --//
        type = mContext.contentResolver.getType(CCContract.TransactionEntry.buildTransactionsForAccountUri(0))
        Assert.assertEquals(CCContract.TransactionEntry.CONTENT_TYPE, type)

        //-- REPEATING_PERIOD --//
        type = mContext.contentResolver.getType(CCContract.RepeatingPeriodEntry.CONTENT_URI)
        Assert.assertEquals(CCContract.RepeatingPeriodEntry.CONTENT_TYPE, type)

        //-- REPEATING_TRANSACTION --//
        type = mContext.contentResolver.getType(CCContract.RepeatingTransactionEntry.CONTENT_URI)
        Assert.assertEquals(CCContract.RepeatingTransactionEntry.CONTENT_TYPE, type)

        //-- REPEATING_TRANSACTION_ID --//
        type = mContext.contentResolver.getType(CCContract.RepeatingTransactionEntry.buildRepeatingTransactionUri(0))
        Assert.assertEquals(CCContract.RepeatingTransactionEntry.CONTENT_ITEM_TYPE, type)
    }

    /**
     * Tests that an account can be inserted and read back properly.
     */
    fun testInsertReadAccount() {
        // Get content values and insert Account entry
        val accountContentValues = accountContentValues
        val accountInsertUri = mContext.contentResolver.insert(CCContract.AccountEntry.CONTENT_URI, accountContentValues)
        val accountRowID = ContentUris.parseId(accountInsertUri)

        // Query for accounts in general and validate
        var accountCursor: Cursor = mContext.contentResolver.query(
                CCContract.AccountEntry.CONTENT_URI, null, null, null, null
        )
        Assert.assertNotNull(accountCursor)
        // Verify we got 1 row then validate cursor
        Assert.assertEquals(1, accountCursor.count)
        validateCursor(accountCursor, accountContentValues)
        accountCursor.close()

        // Query for individual account
        accountCursor = mContext.contentResolver.query(
                CCContract.AccountEntry.buildAccountUri(accountRowID), null, null, null, null
        )
        Assert.assertNotNull(accountCursor)
        Assert.assertEquals(1, accountCursor.count)
        validateCursor(accountCursor, accountContentValues)
        accountCursor.close()
    }

    /**
     * Tests to ensure that an account entry can be updated.
     */
    fun testUpdateAccount() {
        // Get content values and insert account entry.
        val accountContentValues = accountContentValues
        val accountInsertUri = mContext.contentResolver.insert(CCContract.AccountEntry.CONTENT_URI, accountContentValues)
        val accountRowID = ContentUris.parseId(accountInsertUri)

        // Update content values
        accountContentValues.put(CCContract.AccountEntry.COLUMN_NAME, TEST_UPDATE_ACCOUNT_NAME)

        // Update
        val rows = mContext.contentResolver.update(
                CCContract.AccountEntry.CONTENT_URI,
                accountContentValues,
                BaseColumns._ID + " = ?",
                arrayOf(accountRowID.toString())
        )

        // Assert we updated one row
        Assert.assertEquals(1, rows)

        // Validate
        val accountCursor = mContext.contentResolver.query(
                CCContract.AccountEntry.buildAccountUri(accountRowID), null, null, null, null
        )
        Assert.assertNotNull(accountCursor)
        Assert.assertEquals(1, accountCursor!!.count)
        validateCursor(accountCursor, accountContentValues)
        accountCursor.close()
    }

    /**
     * Categories are hard coded inserted in the beginning, and as of version 1.0.1 we only support
     * reading those categories, just verify that rows come back.
     */
    fun testReadCategories() {
        val categoryCursor = mContext.contentResolver.query(
                CCContract.CategoryEntry.CONTENT_URI, null, null, null, null
        )
        Assert.assertNotNull(categoryCursor)
        Assert.assertTrue(categoryCursor!!.count > 0)
        categoryCursor.close()
    }

    /**
     * Tests that a transaction can be inserted into the database. This tests three queries:
     * TRANSACTION, TRANSACTION_ID, and TRANSACTION_FOR_ACCOUNT
     */
    fun testInsertReadTransaction() {
        // Get content values and insert Account entry
        val accountContentValues = accountContentValues
        val accountInsertUri = mContext.contentResolver.insert(CCContract.AccountEntry.CONTENT_URI, accountContentValues)
        val accountRowID = ContentUris.parseId(accountInsertUri)

        // Get content values and insert transaction
        val transactionContentValues = getTransactionContentValues(accountRowID, 1, false, true)
        val transactionInsertUri = mContext.contentResolver.insert(CCContract.TransactionEntry.CONTENT_URI, transactionContentValues)
        val transactionRowID = ContentUris.parseId(transactionInsertUri)

        // Query for all transactions
        var transactionCursor: Cursor = mContext.contentResolver.query(
                CCContract.TransactionEntry.CONTENT_URI, null, null, null, null
        )
        Assert.assertNotNull(transactionCursor)
        Assert.assertEquals(1, transactionCursor.count)
        validateCursor(transactionCursor, transactionContentValues)
        transactionCursor.close()

        // Query for specific transaction
        transactionCursor = mContext.contentResolver.query(
                CCContract.TransactionEntry.buildTransactionUri(transactionRowID), null, null, null, null
        )
        Assert.assertNotNull(transactionCursor)
        Assert.assertEquals(1, transactionCursor.count)
        validateCursor(transactionCursor, transactionContentValues)
        transactionCursor.close()

        // Query for account transactions
        transactionCursor = mContext.contentResolver.query(
                CCContract.TransactionEntry.buildTransactionsForAccountUri(accountRowID), null, null, null, null
        )
        Assert.assertNotNull(transactionCursor)
        Assert.assertEquals(1, transactionCursor.count)
        validateCursor(transactionCursor, transactionContentValues)
        transactionCursor.close()
    }

    /**
     * Tests that the Account balance is updated after a withdrawal is inserted.
     */
    fun testInsertWithdrawalTrigger() {
        // Get content values and insert Account entry
        val accountContentValues = accountContentValues
        val accountInsertUri = mContext.contentResolver.insert(CCContract.AccountEntry.CONTENT_URI, accountContentValues)
        val accountRowID = ContentUris.parseId(accountInsertUri)

        // Get content values and insert transaction
        val transactionContentValues = getTransactionContentValues(accountRowID, 1, false, true)
        val transactionInsertUri = mContext.contentResolver.insert(CCContract.TransactionEntry.CONTENT_URI, transactionContentValues)

        // Query for account
        val accountCursor = mContext.contentResolver.query(
                CCContract.AccountEntry.buildAccountUri(accountRowID), null, null, null, null
        )
        Assert.assertNotNull(accountCursor)

        // The account balance should be the test balance - test withdrawal amount
        val expectedAmount = TEST_ACCOUNT_BALANCE - TEST_TRANSACTION_AMOUNT
        Assert.assertTrue(accountCursor!!.moveToFirst())
        Assert.assertEquals(expectedAmount, accountCursor.getDouble(accountCursor.getColumnIndex(CCContract.AccountEntry.COLUMN_BALANCE)))
    }

    /**
     * Tests that the Account balance is updated after a deposit is inserted.
     */
    fun testInsertDepositTrigger() {
        // Get content values and insert Account entry
        val accountContentValues = accountContentValues
        val accountInsertUri = mContext.contentResolver.insert(CCContract.AccountEntry.CONTENT_URI, accountContentValues)
        val accountRowID = ContentUris.parseId(accountInsertUri)

        // Get content values and insert transaction
        val transactionContentValues = getTransactionContentValues(accountRowID, 1, false, false)
        val transactionInsertUri = mContext.contentResolver.insert(CCContract.TransactionEntry.CONTENT_URI, transactionContentValues)

        // Query for account
        val accountCursor = mContext.contentResolver.query(
                CCContract.AccountEntry.buildAccountUri(accountRowID), null, null, null, null
        )
        Assert.assertNotNull(accountCursor)

        // The account balance should be the test balance + test withdrawal amount
        val expectedAmount = TEST_ACCOUNT_BALANCE + TEST_TRANSACTION_AMOUNT
        Assert.assertTrue(accountCursor!!.moveToFirst())
        Assert.assertEquals(expectedAmount, accountCursor.getDouble(accountCursor.getColumnIndex(CCContract.AccountEntry.COLUMN_BALANCE)))
    }

    /**
     * Tests that the account balance is updated after a withdrawal is deleted.
     */
    fun testDeleteWithdrawalTrigger() {
        // Get content values and insert Account entry
        val accountContentValues = accountContentValues
        val accountInsertUri = mContext.contentResolver.insert(CCContract.AccountEntry.CONTENT_URI, accountContentValues)
        val accountRowID = ContentUris.parseId(accountInsertUri)

        // Get content values and insert transaction
        val transactionContentValues = getTransactionContentValues(accountRowID, 1, false, true)
        val transactionInsertUri = mContext.contentResolver.insert(CCContract.TransactionEntry.CONTENT_URI, transactionContentValues)
        val transactionRowID = ContentUris.parseId(transactionInsertUri)

        // Delete transaction
        val rows = mContext.contentResolver.delete(
                CCContract.TransactionEntry.CONTENT_URI,
                BaseColumns._ID + " = ?",
                arrayOf(transactionRowID.toString())
        )

        // Ensure one row was deleted
        Assert.assertEquals(1, rows)

        // Query for account
        val accountCursor = mContext.contentResolver.query(
                CCContract.AccountEntry.buildAccountUri(accountRowID), null, null, null, null
        )
        Assert.assertNotNull(accountCursor)

        // The account balance should be the same as the beginning
        val expectedAmount = TEST_ACCOUNT_BALANCE
        Assert.assertTrue(accountCursor!!.moveToFirst())
        Assert.assertEquals(expectedAmount, accountCursor.getDouble(accountCursor.getColumnIndex(CCContract.AccountEntry.COLUMN_BALANCE)))
    }

    /**
     * Tests that the account balance is updated after a deposit is deleted.
     */
    fun testDeleteDepositTrigger() {
        // Get content values and insert Account entry
        val accountContentValues = accountContentValues
        val accountInsertUri = mContext.contentResolver.insert(CCContract.AccountEntry.CONTENT_URI, accountContentValues)
        val accountRowID = ContentUris.parseId(accountInsertUri)

        // Get content values and insert transaction
        val transactionContentValues = getTransactionContentValues(accountRowID, 1, false, false)
        val transactionInsertUri = mContext.contentResolver.insert(CCContract.TransactionEntry.CONTENT_URI, transactionContentValues)
        val transactionRowID = ContentUris.parseId(transactionInsertUri)

        // Delete transaction
        val rows = mContext.contentResolver.delete(
                CCContract.TransactionEntry.CONTENT_URI,
                BaseColumns._ID + " = ?",
                arrayOf(transactionRowID.toString())
        )

        // Ensure one row was deleted
        Assert.assertEquals(1, rows)

        // Query for account
        val accountCursor = mContext.contentResolver.query(
                CCContract.AccountEntry.buildAccountUri(accountRowID), null, null, null, null
        )
        Assert.assertNotNull(accountCursor)

        // The account balance should be the same as the beginning
        val expectedAmount = TEST_ACCOUNT_BALANCE
        Assert.assertTrue(accountCursor!!.moveToFirst())
        Assert.assertEquals(expectedAmount, accountCursor.getDouble(accountCursor.getColumnIndex(CCContract.AccountEntry.COLUMN_BALANCE)))
    }

    /**
     * Tests that all transactions for an account are deleted when an account is deleted.
     */
    fun testCascadeDeleteTransactionTrigger() {
        // Get content values and insert Account entry
        val accountContentValues = accountContentValues
        val accountInsertUri = mContext.contentResolver.insert(CCContract.AccountEntry.CONTENT_URI, accountContentValues)
        val accountRowID = ContentUris.parseId(accountInsertUri)

        // Get content values and insert transaction
        val transactionContentValues = getTransactionContentValues(accountRowID, 1, false, true)
        val transactionInsertUri = mContext.contentResolver.insert(CCContract.TransactionEntry.CONTENT_URI, transactionContentValues)
        val transactionRowID = ContentUris.parseId(transactionInsertUri)

        // Delete account
        val rows = mContext.contentResolver.delete(
                CCContract.AccountEntry.CONTENT_URI,
                BaseColumns._ID + " = ?",
                arrayOf(accountRowID.toString())
        )
        Assert.assertEquals(1, rows)

        // Query for transactions, should have none.
        val transactionCursor = mContext.contentResolver.query(
                CCContract.TransactionEntry.CONTENT_URI, null, null, null, null
        )
        Assert.assertNotNull(transactionCursor)
        Assert.assertEquals(0, transactionCursor!!.count)
        transactionCursor.close()

    }

    /**
     * Tests that we can update a transaction entry.
     */
    fun testUpdateTransaction() {
        // Get content values and insert Account entry
        val accountContentValues = accountContentValues
        val accountInsertUri = mContext.contentResolver.insert(CCContract.AccountEntry.CONTENT_URI, accountContentValues)
        val accountRowID = ContentUris.parseId(accountInsertUri)

        // Get content values and insert transaction
        val transactionContentValues = getTransactionContentValues(accountRowID, 1, false, true)
        val transactionInsertUri = mContext.contentResolver.insert(CCContract.TransactionEntry.CONTENT_URI, transactionContentValues)
        val transactionRowID = ContentUris.parseId(transactionInsertUri)

        // Update content values
        transactionContentValues.put(CCContract.TransactionEntry.COLUMN_DESCRIPTION, TEST_UPDATE_TRANSACTION_DESCRIPTION)

        // Update
        val rows = mContext.contentResolver.update(
                CCContract.TransactionEntry.CONTENT_URI,
                transactionContentValues,
                BaseColumns._ID + " = ?",
                arrayOf(transactionRowID.toString())
        )

        // Verify we updated a single row
        Assert.assertEquals(1, rows)

        // Query for this transaction and validate
        val transactionCursor = mContext.contentResolver.query(
                CCContract.TransactionEntry.buildTransactionUri(transactionRowID), null, null, null, null
        )
        Assert.assertNotNull(transactionCursor)
        validateCursor(transactionCursor, transactionContentValues)
        transactionCursor!!.close()
    }

    /**
     * Tests to ensure we can insert and read a RepeatingTransaction entry.
     */
    fun testInsertReadRepeatingTransaction() {
        // Get content values and insert Account entry
        val accountContentValues = accountContentValues
        val accountInsertUri = mContext.contentResolver.insert(CCContract.AccountEntry.CONTENT_URI, accountContentValues)
        val accountRowID = ContentUris.parseId(accountInsertUri)

        val repeatingTransactionContentValues = getRepeatingTransactionContentValues(accountRowID, 1, false, true)
        val repeatingTransactionInsertUri = mContext.contentResolver.insert(CCContract.RepeatingTransactionEntry.CONTENT_URI, repeatingTransactionContentValues)
        val repeatingTransactionRowID = ContentUris.parseId(repeatingTransactionInsertUri)

        // Query for all repeating transactions
        var repeatingTransactionCursor: Cursor = mContext.contentResolver.query(
                CCContract.RepeatingTransactionEntry.CONTENT_URI, null, null, null, null
        )
        Assert.assertNotNull(repeatingTransactionCursor)
        validateCursor(repeatingTransactionCursor, repeatingTransactionContentValues)
        repeatingTransactionCursor.close()

        // Query for specific transaction
        repeatingTransactionCursor = mContext.contentResolver.query(
                CCContract.RepeatingTransactionEntry.buildRepeatingTransactionUri(repeatingTransactionRowID), null, null, null, null
        )
        Assert.assertNotNull(repeatingTransactionCursor)
        validateCursor(repeatingTransactionCursor, repeatingTransactionContentValues)
        repeatingTransactionCursor.close()
    }

    /**
     * Tests that we can update a repeating transaction entry.
     */
    fun testUpdateRepeatingTransaction() {
        // Get content values and insert Account entry
        val accountContentValues = accountContentValues
        val accountInsertUri = mContext.contentResolver.insert(CCContract.AccountEntry.CONTENT_URI, accountContentValues)
        val accountRowID = ContentUris.parseId(accountInsertUri)

        val repeatingTransactionContentValues = getRepeatingTransactionContentValues(accountRowID, 1, false, true)
        val repeatingTransactionInsertUri = mContext.contentResolver.insert(CCContract.RepeatingTransactionEntry.CONTENT_URI, repeatingTransactionContentValues)
        val repeatingTransactionRowID = ContentUris.parseId(repeatingTransactionInsertUri)

        // Update content values
        repeatingTransactionContentValues.put(CCContract.RepeatingTransactionEntry.COLUMN_DESCRIPTION, TEST_UPDATE_TRANSACTION_DESCRIPTION)

        // Update
        val updatedRows = mContext.contentResolver.update(
                CCContract.RepeatingTransactionEntry.CONTENT_URI,
                repeatingTransactionContentValues,
                BaseColumns._ID + " = ?",
                arrayOf(repeatingTransactionRowID.toString())
        )

        // Verify we updated a single row
        Assert.assertEquals(1, updatedRows)

        // Query for this row and validate
        val repeatingTransactionCursor = mContext.contentResolver.query(
                CCContract.RepeatingTransactionEntry.buildRepeatingTransactionUri(repeatingTransactionRowID), null, null, null, null
        )
        Assert.assertNotNull(repeatingTransactionCursor)
        validateCursor(repeatingTransactionCursor, repeatingTransactionContentValues)
        repeatingTransactionCursor!!.close()
    }

    /**
     * Tests that the account balance changes when a deposit is changed to a withdrawal
     */
    fun testUpdateDepositToWithdrawalTrigger() {
        // Get content values and insert Account entry
        val accountContentValues = accountContentValues
        val accountInsertUri = mContext.contentResolver.insert(CCContract.AccountEntry.CONTENT_URI, accountContentValues)
        val accountRowID = ContentUris.parseId(accountInsertUri)

        // Get content values and insert transaction
        val transactionContentValues = getTransactionContentValues(accountRowID, 1, false, false)
        val transactionInsertUri = mContext.contentResolver.insert(CCContract.TransactionEntry.CONTENT_URI, transactionContentValues)
        val transactionRowId = ContentUris.parseId(transactionInsertUri)

        // Set withdrawal flag to 1
        transactionContentValues.put(CCContract.TransactionEntry.COLUMN_WITHDRAWAL, 1)

        // Update
        val rows = mContext.contentResolver.update(
                CCContract.TransactionEntry.CONTENT_URI,
                transactionContentValues,
                BaseColumns._ID + " = ?",
                arrayOf(transactionRowId.toString())
        )

        // Verify we updated one row
        Assert.assertEquals(1, rows)

        // Query for account balance.
        val accountCursor = mContext.contentResolver.query(
                CCContract.AccountEntry.buildAccountUri(accountRowID), null, null, null, null
        )
        Assert.assertNotNull(accountCursor)

        // The expected balance is now the test balance MINUS the test amount.
        val expectedAmount = TEST_ACCOUNT_BALANCE - TEST_TRANSACTION_AMOUNT
        Assert.assertTrue(accountCursor!!.moveToFirst())
        Assert.assertEquals(expectedAmount, accountCursor.getDouble(accountCursor.getColumnIndex(CCContract.AccountEntry.COLUMN_BALANCE)))
    }

    /**
     * Tests that the account balance changes when a withdrawal is changed to a deposit
     */
    fun testUpdateWithdrawalToDepositTrigger() {
        // Get content values and insert Account entry
        val accountContentValues = accountContentValues
        val accountInsertUri = mContext.contentResolver.insert(CCContract.AccountEntry.CONTENT_URI, accountContentValues)
        val accountRowID = ContentUris.parseId(accountInsertUri)

        // Get content values and insert transaction
        val transactionContentValues = getTransactionContentValues(accountRowID, 1, false, true)
        val transactionInsertUri = mContext.contentResolver.insert(CCContract.TransactionEntry.CONTENT_URI, transactionContentValues)
        val transactionRowId = ContentUris.parseId(transactionInsertUri)

        // Set withdrawal flag to 0
        transactionContentValues.put(CCContract.TransactionEntry.COLUMN_WITHDRAWAL, 0)

        // Update
        val rows = mContext.contentResolver.update(
                CCContract.TransactionEntry.CONTENT_URI,
                transactionContentValues,
                BaseColumns._ID + " = ?",
                arrayOf(transactionRowId.toString())
        )

        // Verify we updated one row
        Assert.assertEquals(1, rows)

        // Query for account balance.
        val accountCursor = mContext.contentResolver.query(
                CCContract.AccountEntry.buildAccountUri(accountRowID), null, null, null, null
        )
        Assert.assertNotNull(accountCursor)

        // The expected balance is now the test balance PLUS the test amount.
        val expectedAmount = TEST_ACCOUNT_BALANCE + TEST_TRANSACTION_AMOUNT
        Assert.assertTrue(accountCursor!!.moveToFirst())
        Assert.assertEquals(expectedAmount, accountCursor.getDouble(accountCursor.getColumnIndex(CCContract.AccountEntry.COLUMN_BALANCE)))
    }

    /**
     * Tests that the account balance changes when a deposit amount is changed.
     */
    fun testUpdateDepositAmountTrigger() {
        // Get content values and insert Account entry
        val accountContentValues = accountContentValues
        val accountInsertUri = mContext.contentResolver.insert(CCContract.AccountEntry.CONTENT_URI, accountContentValues)
        val accountRowID = ContentUris.parseId(accountInsertUri)

        // Get content values and insert transaction
        // For deposit
        val transactionContentValues = getTransactionContentValues(accountRowID, 1, false, false)
        val transactionInsertUri = mContext.contentResolver.insert(CCContract.TransactionEntry.CONTENT_URI, transactionContentValues)
        val transactionRowId = ContentUris.parseId(transactionInsertUri)

        // Change balance
        transactionContentValues.put(CCContract.TransactionEntry.COLUMN_AMOUNT, TEST_TRANSACTION_UPDATE_AMOUNT)

        // Update
        val rows = mContext.contentResolver.update(
                CCContract.TransactionEntry.CONTENT_URI,
                transactionContentValues,
                BaseColumns._ID + " = ?",
                arrayOf(transactionRowId.toString())
        )

        // Verify we updated one row
        Assert.assertEquals(1, rows)

        // Query for account balance.
        val accountCursor = mContext.contentResolver.query(
                CCContract.AccountEntry.buildAccountUri(accountRowID), null, null, null, null
        )
        Assert.assertNotNull(accountCursor)

        // The expected balance is now the test balance PLUS the updated amount.
        val expectedAmount = TEST_ACCOUNT_BALANCE + TEST_TRANSACTION_UPDATE_AMOUNT
        Assert.assertTrue(accountCursor!!.moveToFirst())
        Assert.assertEquals(expectedAmount, accountCursor.getDouble(accountCursor.getColumnIndex(CCContract.AccountEntry.COLUMN_BALANCE)))
    }

    /**
     * Tests that the account balance changes when a withdrawal amount is changed.
     */
    fun testUpdateWithdrawalAmountTrigger() {
        // Get content values and insert Account entry
        val accountContentValues = accountContentValues
        val accountInsertUri = mContext.contentResolver.insert(CCContract.AccountEntry.CONTENT_URI, accountContentValues)
        val accountRowID = ContentUris.parseId(accountInsertUri)

        // Get content values and insert transaction
        // For withdrawal
        val transactionContentValues = getTransactionContentValues(accountRowID, 1, false, true)
        val transactionInsertUri = mContext.contentResolver.insert(CCContract.TransactionEntry.CONTENT_URI, transactionContentValues)
        val transactionRowId = ContentUris.parseId(transactionInsertUri)

        // Change balance
        transactionContentValues.put(CCContract.TransactionEntry.COLUMN_AMOUNT, TEST_TRANSACTION_UPDATE_AMOUNT)

        // Update
        val rows = mContext.contentResolver.update(
                CCContract.TransactionEntry.CONTENT_URI,
                transactionContentValues,
                BaseColumns._ID + " = ?",
                arrayOf(transactionRowId.toString())
        )

        // Verify we updated one row
        Assert.assertEquals(1, rows)

        // Query for account balance.
        val accountCursor = mContext.contentResolver.query(
                CCContract.AccountEntry.buildAccountUri(accountRowID), null, null, null, null
        )
        Assert.assertNotNull(accountCursor)

        // The expected balance is now the test balance MINUS the updated amount.
        val expectedAmount = TEST_ACCOUNT_BALANCE - TEST_TRANSACTION_UPDATE_AMOUNT
        Assert.assertTrue(accountCursor!!.moveToFirst())
        Assert.assertEquals(expectedAmount, accountCursor.getDouble(accountCursor.getColumnIndex(CCContract.AccountEntry.COLUMN_BALANCE)))
    }

    /**
     * Retrieves ContentValues for a test account entry.
     */
    private val accountContentValues: ContentValues
        get() {
            val contentValues = ContentValues()

            contentValues.put(CCContract.AccountEntry.COLUMN_NAME, TEST_ACCOUNT_NAME)
            contentValues.put(CCContract.AccountEntry.COLUMN_BALANCE, TEST_ACCOUNT_BALANCE)

            return contentValues
        }

    /**
     * Retrieves ContentValues for a test transaction entry.
     * @param account The account to insert a transaction for.
     */
    private fun getTransactionContentValues(account: Long, category: Long, useNotes: Boolean, isWithdrawal: Boolean): ContentValues {
        val contentValues = ContentValues()

        contentValues.put(CCContract.TransactionEntry.COLUMN_ACCOUNT, account)
        contentValues.put(CCContract.TransactionEntry.COLUMN_CATEGORY, category)
        contentValues.put(CCContract.TransactionEntry.COLUMN_DESCRIPTION, TEST_TRANSACTION_DESCRIPTION)
        contentValues.put(CCContract.TransactionEntry.COLUMN_AMOUNT, TEST_TRANSACTION_AMOUNT)
        contentValues.put(CCContract.TransactionEntry.COLUMN_DATE, TEST_TRANSACTION_DATE)
        contentValues.put(CCContract.TransactionEntry.COLUMN_WITHDRAWAL, if (isWithdrawal) 1 else 0)

        if (useNotes) {
            contentValues.put(CCContract.TransactionEntry.COLUMN_NOTES, TEST_TRANSACTION_NOTES)
        }

        return contentValues
    }

    /**
     * Retrieves test content values for a repeating transaction entry.
     */
    private fun getRepeatingTransactionContentValues(account: Long, category: Long, useNotes: Boolean, isWithdrawal: Boolean): ContentValues {
        val contentValues = ContentValues()

        contentValues.put(CCContract.RepeatingTransactionEntry.COLUMN_ACCOUNT, account)
        contentValues.put(CCContract.RepeatingTransactionEntry.COLUMN_CATEGORY, category)
        contentValues.put(CCContract.RepeatingTransactionEntry.COLUMN_REPEATING_PERIOD, TEST_REPEATING_PERIOD)
        contentValues.put(CCContract.RepeatingTransactionEntry.COLUMN_DESCRIPTION, TEST_TRANSACTION_DESCRIPTION)
        contentValues.put(CCContract.RepeatingTransactionEntry.COLUMN_AMOUNT, TEST_TRANSACTION_AMOUNT)
        contentValues.put(CCContract.RepeatingTransactionEntry.COLUMN_NEXT_DATE, TEST_TRANSACTION_DATE)
        contentValues.put(CCContract.RepeatingTransactionEntry.COLUMN_WITHDRAWAL, if (isWithdrawal) 1 else 0)

        if (useNotes) {
            contentValues.put(CCContract.RepeatingTransactionEntry.COLUMN_NOTES, TEST_TRANSACTION_NOTES)
        }


        return contentValues
    }

    /**
     * Parses through the information read from a database table and ensures that it matches
     * the group of expected values.
     * @param valueCursor The cursor for the database values read.
     * *
     * @param expectedValues The expected values to be read from the database.
     */
    private fun validateCursor(valueCursor: Cursor, expectedValues: ContentValues) {
        Assert.assertTrue(valueCursor.moveToFirst())

        val valueSet = expectedValues.valueSet()

        for ((columnName, value) in valueSet) {
            val idx = valueCursor.getColumnIndex(columnName)
            Assert.assertFalse(idx == -1)
            when (valueCursor.getType(idx)) {
                Cursor.FIELD_TYPE_FLOAT -> Assert.assertEquals(value, valueCursor.getDouble(idx))
                Cursor.FIELD_TYPE_INTEGER -> Assert.assertEquals(Integer.parseInt(value.toString()), valueCursor.getInt(idx))
                Cursor.FIELD_TYPE_STRING -> Assert.assertEquals(value, valueCursor.getString(idx))
                else -> Assert.assertEquals(value.toString(), valueCursor.getString(idx))
            }
        }
        valueCursor.close()
    }

    companion object {
        private val TEST_ACCOUNT_NAME = "Checking"
        private val TEST_UPDATE_ACCOUNT_NAME = "Checking1"
        private val TEST_ACCOUNT_BALANCE = 100.40

        private val TEST_TRANSACTION_DESCRIPTION = "Speedway"
        private val TEST_UPDATE_TRANSACTION_DESCRIPTION = "Speedway1"
        private val TEST_TRANSACTION_AMOUNT = 33.56
        private val TEST_TRANSACTION_DATE = "2015-11-08"
        private val TEST_TRANSACTION_NOTES = "Some Notes"
        private val TEST_TRANSACTION_UPDATE_AMOUNT = 10.00

        // Monthly repeating period
        private val TEST_REPEATING_PERIOD: Long = 1
    }
}
