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
    private static final double TEST_ACCOUNT_BALANCE = 100.40;

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
     * Retrieves ContentValues for a test account entry.
     */
    private ContentValues getAccountContentValues(){
        ContentValues contentValues = new ContentValues();

        contentValues.put(CCContract.AccountEntry.COLUMN_NAME, TEST_ACCOUNT_NAME);
        contentValues.put(CCContract.AccountEntry.COLUMN_BALANCE, TEST_ACCOUNT_BALANCE);

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
