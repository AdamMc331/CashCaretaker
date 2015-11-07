package com.androidessence.cashcaretaker;

import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.test.AndroidTestCase;

import com.androidessence.cashcaretaker.data.CCContract;

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

    public void testDeleteAllRecords(){
        // Delete accounts
        mContext.getContentResolver().delete(
                CCContract.AccountEntry.CONTENT_URI,
                null,
                null
        );

        // Verify deletion
        Cursor cursor = mContext.getContentResolver().query(
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

    public void testGetType(){
        //-- ACCOUNT --//
        String type = mContext.getContentResolver().getType(CCContract.AccountEntry.CONTENT_URI);
        assertEquals(CCContract.AccountEntry.CONTENT_TYPE, type);

        //-- ACCOUNT_ID --//
        type = mContext.getContentResolver().getType(CCContract.AccountEntry.buildAccountUri(0));
        assertEquals(CCContract.AccountEntry.CONTENT_ITEM_TYPE, type);
    }

    public void testInsertReadAccount(){
        ContentValues accountContentValues = getAccountContentValues();
        Uri accountInsertUri = mContext.getContentResolver().insert(CCContract.AccountEntry.CONTENT_URI, accountContentValues);
        long accountRowID = ContentUris.parseId(accountInsertUri);
    }

    private ContentValues getAccountContentValues(){
        ContentValues contentValues = new ContentValues();

        contentValues.put(CCContract.AccountEntry.COLUMN_NAME, TEST_ACCOUNT_NAME);
        contentValues.put(CCContract.AccountEntry.COLUMN_BALANCE, TEST_ACCOUNT_BALANCE);

        return contentValues;
    }
}
