package com.androidessence.cashcaretaker;

import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.test.AndroidTestCase;

import com.androidessence.cashcaretaker.data.CCContract;

import java.util.Map;
import java.util.Set;

/**
 * Test cases the ensure the quality of the Content Provider for Cash Caretaker.
 *
 * Created by adammcneilly on 11/1/15.
 */
public class TestProvider extends AndroidTestCase {
    private static final String TEST_ACCOUNT_NAME = "Checking";
    private static final String TEST_UPDATE_ACCOUNT_NAME = "Checking1";
    private static final double TEST_ACCOUNT_BALANCE = 100.40;

    private static final String TEST_TRANSACTION_DESCRIPTION = "Speedway";
    private static final String TEST_UPDATE_TRANSACTION_DESCRIPTION = "Speedway1";
    private static final double TEST_TRANSACTION_AMOUNT = 33.56;
    private static final String TEST_TRANSACTION_DATE = "2015-11-08";
    private static final String TEST_TRANSACTION_NOTES = "Some Notes";

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        testDeleteAllRecords();
    }

    @Override
    protected void tearDown() throws Exception {
        testDeleteAllRecords();
        super.tearDown();
    }

    /**
     * Deletes all records from the database and ensures they were deleted.
     */
    public void testDeleteAllRecords(){
        // Delete transactions
        mContext.getContentResolver().delete(
                CCContract.TransactionEntry.CONTENT_URI,
                null,
                null
        );

        Cursor cursor = mContext.getContentResolver().query(
                CCContract.TransactionEntry.CONTENT_URI,
                null,
                null,
                null,
                null
        );
        assertNotNull(cursor);
        assertEquals(0, cursor.getCount());
        cursor.close();

        // Delete accounts
        mContext.getContentResolver().delete(
                CCContract.AccountEntry.CONTENT_URI,
                null,
                null
        );

        // Verify deletion
        cursor = mContext.getContentResolver().query(
                CCContract.AccountEntry.CONTENT_URI,
                null,
                null,
                null,
                null
        );
        assertNotNull(cursor);
        assertEquals(0, cursor.getCount());
        cursor.close();
    }

    /**
     * Verifies the response of the `getType()` method required by content provider for each of the supported URIs in this application.
     */
    public void testGetType(){
        //-- ACCOUNT --//
        String type = mContext.getContentResolver().getType(CCContract.AccountEntry.CONTENT_URI);
        assertEquals(CCContract.AccountEntry.CONTENT_TYPE, type);

        //-- ACCOUNT_ID --//
        type = mContext.getContentResolver().getType(CCContract.AccountEntry.buildAccountUri(0));
        assertEquals(CCContract.AccountEntry.CONTENT_ITEM_TYPE, type);

        //-- CATEGORY --//
        type = mContext.getContentResolver().getType(CCContract.CategoryEntry.CONTENT_URI);
        assertEquals(CCContract.CategoryEntry.CONTENT_TYPE, type);

        //-- TRANSACTION --//
        type = mContext.getContentResolver().getType(CCContract.TransactionEntry.CONTENT_URI);
        assertEquals(CCContract.TransactionEntry.CONTENT_TYPE, type);

        //-- TRANSACTION_ID --//
        type = mContext.getContentResolver().getType(CCContract.TransactionEntry.buildTransactionUri(0));
        assertEquals(CCContract.TransactionEntry.CONTENT_ITEM_TYPE, type);

        //-- TRANSACTION_FOR_ACCOUNT --//
        type = mContext.getContentResolver().getType(CCContract.TransactionEntry.buildTransactionsForAccountUri(0));
        assertEquals(CCContract.TransactionEntry.CONTENT_TYPE, type);
    }

    /**
     * Tests that an account can be inserted and read back properly.
     */
    public void testInsertReadAccount(){
        // Get content values and insert Account entry
        ContentValues accountContentValues = getAccountContentValues();
        Uri accountInsertUri = mContext.getContentResolver().insert(CCContract.AccountEntry.CONTENT_URI, accountContentValues);
        long accountRowID = ContentUris.parseId(accountInsertUri);

        // Query for accounts in general and validate
        Cursor accountCursor = mContext.getContentResolver().query(
                CCContract.AccountEntry.CONTENT_URI,
                null,
                null,
                null,
                null
        );
        assertNotNull(accountCursor);
        // Verify we got 1 row then validate cursor
        assertEquals(1, accountCursor.getCount());
        validateCursor(accountCursor, accountContentValues);
        accountCursor.close();

        // Query for individual account
        accountCursor = mContext.getContentResolver().query(
                CCContract.AccountEntry.buildAccountUri(accountRowID),
                null,
                null,
                null,
                null
        );
        assertNotNull(accountCursor);
        assertEquals(1, accountCursor.getCount());
        validateCursor(accountCursor, accountContentValues);
        accountCursor.close();
    }

    /**
     * Tests to ensure that an account entry can be updated.
     */
    public void testUpdateAccount() {
        // Get content values and insert account entry.
        ContentValues accountContentValues = getAccountContentValues();
        Uri accountInsertUri = mContext.getContentResolver().insert(CCContract.AccountEntry.CONTENT_URI, accountContentValues);
        long accountRowID = ContentUris.parseId(accountInsertUri);

        // Update content values
        accountContentValues.put(CCContract.AccountEntry.COLUMN_NAME, TEST_UPDATE_ACCOUNT_NAME);

        // Update
        int rows = mContext.getContentResolver().update(
                CCContract.AccountEntry.CONTENT_URI,
                accountContentValues,
                CCContract.AccountEntry._ID + " = ?",
                new String[]{String.valueOf(accountRowID)}
        );

        // Assert we updated one row
        assertEquals(1, rows);

        // Validate
        Cursor accountCursor = mContext.getContentResolver().query(
                CCContract.AccountEntry.buildAccountUri(accountRowID),
                null,
                null,
                null,
                null
        );
        assertNotNull(accountCursor);
        assertEquals(1, accountCursor.getCount());
        validateCursor(accountCursor, accountContentValues);
        accountCursor.close();
    }

    /**
     * Categories are hard coded inserted in the beginning, and as of version 1.0.1 we only support
     * reading those categories, just verify that rows come back.
     */
    public void testReadCategories() {
        Cursor categoryCursor = mContext.getContentResolver().query(
                CCContract.CategoryEntry.CONTENT_URI,
                null,
                null,
                null,
                null
        );
        assertNotNull(categoryCursor);
        assertTrue(categoryCursor.getCount() > 0);
        categoryCursor.close();
    }

    /**
     * Tests that a transaction can be inserted into the database. This tests three queries:
     * TRANSACTION, TRANSACTION_ID, and TRANSACTION_FOR_ACCOUNT
     */
    public void testInsertReadTransaction() {
        // Get content values and insert Account entry
        ContentValues accountContentValues = getAccountContentValues();
        Uri accountInsertUri = mContext.getContentResolver().insert(CCContract.AccountEntry.CONTENT_URI, accountContentValues);
        long accountRowID = ContentUris.parseId(accountInsertUri);

        // Get content values and insert transaction
        ContentValues transactionContentValues = getTransactionContentValues(accountRowID, 1, false, true);
        Uri transactionInsertUri = mContext.getContentResolver().insert(CCContract.TransactionEntry.CONTENT_URI, transactionContentValues);
        long transactionRowID = ContentUris.parseId(transactionInsertUri);

        // Query for all transactions
        Cursor transactionCursor = mContext.getContentResolver().query(
                CCContract.TransactionEntry.CONTENT_URI,
                null,
                null,
                null,
                null
        );
        assertNotNull(transactionCursor);
        assertEquals(1, transactionCursor.getCount());
        validateCursor(transactionCursor, transactionContentValues);
        transactionCursor.close();

        // Query for specific transaction
        transactionCursor = mContext.getContentResolver().query(
                CCContract.TransactionEntry.buildTransactionUri(transactionRowID),
                null,
                null,
                null,
                null
        );
        assertNotNull(transactionCursor);
        assertEquals(1, transactionCursor.getCount());
        validateCursor(transactionCursor, transactionContentValues);
        transactionCursor.close();

        // Query for account transactions
        transactionCursor = mContext.getContentResolver().query(
                CCContract.TransactionEntry.buildTransactionsForAccountUri(accountRowID),
                null,
                null,
                null,
                null
        );
        assertNotNull(transactionCursor);
        assertEquals(1, transactionCursor.getCount());
        validateCursor(transactionCursor, transactionContentValues);
        transactionCursor.close();
    }

    /**
     * Retrieves ContentValues for a test account entry.
     */
    private ContentValues getAccountContentValues(){
        ContentValues contentValues = new ContentValues();

        contentValues.put(CCContract.AccountEntry.COLUMN_NAME, TEST_ACCOUNT_NAME);
        contentValues.put(CCContract.AccountEntry.COLUMN_BALANCE, TEST_ACCOUNT_BALANCE);

        return contentValues;
    }

    /**
     * Retrieves ContentValues for a test transaction entry.
     * @param account The account to insert a transaction for.
     */
    private ContentValues getTransactionContentValues(long account, long category, boolean useNotes, boolean isWithdrawal) {
        ContentValues contentValues = new ContentValues();

        contentValues.put(CCContract.TransactionEntry.COLUMN_ACCOUNT, account);
        contentValues.put(CCContract.TransactionEntry.COLUMN_CATEGORY, category);
        contentValues.put(CCContract.TransactionEntry.COLUMN_DESCRIPTION, TEST_TRANSACTION_DESCRIPTION);
        contentValues.put(CCContract.TransactionEntry.COLUMN_AMOUNT, TEST_TRANSACTION_AMOUNT);
        contentValues.put(CCContract.TransactionEntry.COLUMN_DATE, TEST_TRANSACTION_DATE);
        contentValues.put(CCContract.TransactionEntry.COLUMN_WITHDRAWAL, isWithdrawal ? 1 : 0);

        if(useNotes) {
            contentValues.put(CCContract.TransactionEntry.COLUMN_NOTES, TEST_TRANSACTION_NOTES);
        }

        return contentValues;
    }

    /**
     * Parses through the information read from a database table and ensures that it matches
     * the group of expected values.
     * @param valueCursor The cursor for the database values read.
     * @param expectedValues The expected values to be read from the database.
     */
    private void validateCursor(Cursor valueCursor, ContentValues expectedValues){
        assertTrue(valueCursor.moveToFirst());

        Set<Map.Entry<String, Object>> valueSet = expectedValues.valueSet();

        for(Map.Entry<String, Object> entry : valueSet){
            String columnName = entry.getKey();
            int idx = valueCursor.getColumnIndex(columnName);
            assertFalse(idx == -1);
            switch(valueCursor.getType(idx)){
                case Cursor.FIELD_TYPE_FLOAT:
                    assertEquals(entry.getValue(), valueCursor.getDouble(idx));
                    break;
                case Cursor.FIELD_TYPE_INTEGER:
                    assertEquals(Integer.parseInt(entry.getValue().toString()), valueCursor.getInt(idx));
                    break;
                case Cursor.FIELD_TYPE_STRING:
                    assertEquals(entry.getValue(), valueCursor.getString(idx));
                    break;
                default:
                    assertEquals(entry.getValue().toString(), valueCursor.getString(idx));
                    break;
            }
        }
        valueCursor.close();
    }
}
