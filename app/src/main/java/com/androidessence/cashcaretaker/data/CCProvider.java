package com.androidessence.cashcaretaker.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Content provider used for accessing the data model of Cash Caretaker.
 *
 * Created by adammcneilly on 10/30/15.
 */
public class CCProvider extends ContentProvider {
    private static final int ACCOUNT = 0;
    private static final int ACCOUNT_ID = 1;
    private static final int CATEGORY = 10;
    private static final int TRANSACTION = 20;
    private static final int TRANSACTION_ID = 21;
    private static final int TRANSACTION_FOR_ACCOUNT = 22;
    private static final int TRANSACTION_FOR_ACCOUNT_DESCRIPTION = 23;
    private static final int REPEATING_PERIOD = 30;
    private static final int REPEATING_TRANSACTION = 40;
    private static final int REPEATING_TRANSACTION_ID = 41;
    private static final int REPEATING_TRANSACTION_DETAILS = 42;

    private CCDatabaseHelper mOpenHelper;
    private final UriMatcher sUriMatcher = buildUriMatcher();

    private static UriMatcher buildUriMatcher() {
        String content = CCContract.CONTENT_AUTHORITY;

        UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        matcher.addURI(content, CCContract.PATH_ACCOUNT, ACCOUNT);
        matcher.addURI(content, CCContract.PATH_ACCOUNT + "/#", ACCOUNT_ID);
        matcher.addURI(content, CCContract.PATH_CATEGORY, CATEGORY);
        matcher.addURI(content, CCContract.PATH_TRANSACTION, TRANSACTION);
        matcher.addURI(content, CCContract.PATH_TRANSACTION + "/#", TRANSACTION_ID);
        matcher.addURI(content, CCContract.PATH_TRANSACTION + "/" + CCContract.PATH_ACCOUNT + "/#", TRANSACTION_FOR_ACCOUNT);
        matcher.addURI(content, CCContract.PATH_TRANSACTION + "/*/" + CCContract.PATH_ACCOUNT + "/#", TRANSACTION_FOR_ACCOUNT_DESCRIPTION);
        matcher.addURI(content, CCContract.PATH_REPEATING_PERIOD, REPEATING_PERIOD);
        matcher.addURI(content, CCContract.PATH_REPEATING_TRANSACTION, REPEATING_TRANSACTION);
        matcher.addURI(content, CCContract.PATH_REPEATING_TRANSACTION + "/#", REPEATING_TRANSACTION_ID);
        matcher.addURI(content, CCContract.PATH_REPEATING_TRANSACTION + "/" + CCContract.PATH_DETAILS, REPEATING_TRANSACTION_DETAILS);

        return matcher;
    }

    private static final SQLiteQueryBuilder sTransactionWithCategoryBuilder;

    static {
        sTransactionWithCategoryBuilder = new SQLiteQueryBuilder();
        sTransactionWithCategoryBuilder.setTables(
                CCContract.TransactionEntry.TABLE_NAME + " " +
                        "LEFT JOIN " + CCContract.CategoryEntry.TABLE_NAME + " ON " +
                        CCContract.CategoryEntry.TABLE_NAME + "." + CCContract.CategoryEntry._ID + " = " + CCContract.TransactionEntry.COLUMN_CATEGORY
        );
    }

    private static final SQLiteQueryBuilder sRepeatingTransactionDetailsBuilder;
    static {
        sRepeatingTransactionDetailsBuilder = new SQLiteQueryBuilder();
        sRepeatingTransactionDetailsBuilder.setTables(
                CCContract.RepeatingTransactionEntry.TABLE_NAME + " " +
                        "LEFT JOIN " + CCContract.CategoryEntry.TABLE_NAME + " ON " +
                        CCContract.CategoryEntry.TABLE_NAME + "." + CCContract.CategoryEntry._ID + " = " + CCContract.RepeatingTransactionEntry.COLUMN_CATEGORY + " " +
                        "LEFT JOIN " + CCContract.AccountEntry.TABLE_NAME + " ON " +
                        CCContract.AccountEntry.TABLE_NAME + "." + CCContract.AccountEntry._ID + " = " + CCContract.RepeatingTransactionEntry.COLUMN_ACCOUNT + " " +
                        "LEFT JOIN " + CCContract.RepeatingPeriodEntry.TABLE_NAME + " ON " +
                        CCContract.RepeatingPeriodEntry.TABLE_NAME + "." + CCContract.RepeatingPeriodEntry._ID + " = " + CCContract.RepeatingTransactionEntry.COLUMN_REPEATING_PERIOD
        );
    }

    @Override
    public boolean onCreate() {
        mOpenHelper = new CCDatabaseHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SQLiteDatabase db = mOpenHelper.getReadableDatabase();
        long _id;
        Cursor retCursor;

        switch (sUriMatcher.match(uri)) {
            case ACCOUNT:
                retCursor = db.query(
                        CCContract.AccountEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            case ACCOUNT_ID:
                _id = ContentUris.parseId(uri);
                retCursor = db.query(
                        CCContract.AccountEntry.TABLE_NAME,
                        projection,
                        CCContract.AccountEntry._ID + " = ?",
                        new String[]{String.valueOf(_id)},
                        null,
                        null,
                        sortOrder
                );
                break;
            case CATEGORY:
                retCursor = db.query(
                        CCContract.CategoryEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            case TRANSACTION:
                retCursor = db.query(
                        CCContract.TransactionEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            case TRANSACTION_ID:
                _id = ContentUris.parseId(uri);
                retCursor = db.query(
                        CCContract.TransactionEntry.TABLE_NAME,
                        projection,
                        CCContract.TransactionEntry._ID + " = ?",
                        new String[]{String.valueOf(_id)},
                        null,
                        null,
                        sortOrder
                );
                break;
            case TRANSACTION_FOR_ACCOUNT:
                _id = ContentUris.parseId(uri);
                retCursor = sTransactionWithCategoryBuilder.query(
                        db,
                        projection,
                        CCContract.TransactionEntry.COLUMN_ACCOUNT + " = ?",
                        new String[]{String.valueOf(_id)},
                        null,
                        null,
                        sortOrder
                );
                break;
            case TRANSACTION_FOR_ACCOUNT_DESCRIPTION:
                // Decided not to query by account, but all transaction descriptions.
                // _id = ContentUris.parseId(uri);
                String description = CCContract.TransactionEntry.getDescriptionFromUri(uri);
                retCursor = db.query(
                        CCContract.TransactionEntry.TABLE_NAME,
                        projection,
                        CCContract.TransactionEntry.COLUMN_DESCRIPTION + " LIKE ?",
                        new String[] {"%" + description + "%"},
                        null,
                        null,
                        sortOrder
                );
                break;
            case REPEATING_PERIOD:
                retCursor = db.query(
                        CCContract.RepeatingPeriodEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            case REPEATING_TRANSACTION:
                retCursor = db.query(
                        CCContract.RepeatingTransactionEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            case REPEATING_TRANSACTION_ID:
                _id = ContentUris.parseId(uri);
                retCursor = db.query(
                        CCContract.RepeatingTransactionEntry.TABLE_NAME,
                        projection,
                        CCContract.RepeatingTransactionEntry._ID + " = ?",
                        new String[]{String.valueOf(_id)},
                        null,
                        null,
                        sortOrder
                );
                break;
            case REPEATING_TRANSACTION_DETAILS:
                retCursor = sRepeatingTransactionDetailsBuilder.query(
                        db,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        // Notify any followers
        assert getContext() != null;
        retCursor.setNotificationUri(getContext().getContentResolver(), uri);
        return retCursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        switch (sUriMatcher.match(uri)) {
            case ACCOUNT:
                return CCContract.AccountEntry.CONTENT_TYPE;
            case ACCOUNT_ID:
                return CCContract.AccountEntry.CONTENT_ITEM_TYPE;
            case CATEGORY:
                return CCContract.CategoryEntry.CONTENT_TYPE;
            case TRANSACTION:
            case TRANSACTION_FOR_ACCOUNT:
                return CCContract.TransactionEntry.CONTENT_TYPE;
            case TRANSACTION_ID:
                return CCContract.TransactionEntry.CONTENT_ITEM_TYPE;
            case REPEATING_PERIOD:
                return CCContract.RepeatingPeriodEntry.CONTENT_TYPE;
            case REPEATING_TRANSACTION:
            case REPEATING_TRANSACTION_DETAILS:
                return CCContract.RepeatingTransactionEntry.CONTENT_TYPE;
            case REPEATING_TRANSACTION_ID:
                return CCContract.RepeatingTransactionEntry.CONTENT_ITEM_TYPE;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, ContentValues values) {
        long _id;
        Uri returnUri;
        SQLiteDatabase db = mOpenHelper.getReadableDatabase();

        switch (sUriMatcher.match(uri)) {
            case ACCOUNT:
                _id = db.insertOrThrow(CCContract.AccountEntry.TABLE_NAME, null, values);
                if (_id > 0) {
                    returnUri = CCContract.AccountEntry.buildAccountUri(_id);
                } else {
                    throw new UnsupportedOperationException("Unable to insert row into: " + uri);
                }
                break;
            case CATEGORY:
                _id = db.insert(CCContract.CategoryEntry.TABLE_NAME, null, values);
                if (_id > 0) {
                    returnUri = CCContract.CategoryEntry.buildCategoryUri(_id);
                } else {
                    throw new UnsupportedOperationException("Unable to insert row into: " + uri);
                }
                break;
            case TRANSACTION:
                _id = db.insert(CCContract.TransactionEntry.TABLE_NAME, null, values);
                if (_id > 0) {
                    returnUri = CCContract.TransactionEntry.buildTransactionUri(_id);
                } else {
                    throw new UnsupportedOperationException("Unable to insert row into: " + uri);
                }
                break;
            case REPEATING_TRANSACTION:
                _id = db.insert(CCContract.RepeatingTransactionEntry.TABLE_NAME, null, values);
                if(_id > 0) {
                    returnUri = CCContract.RepeatingTransactionEntry.buildRepeatingTransactionUri(_id);
                } else {
                    throw new UnsupportedOperationException("Unable to insert row into: " + uri);
                }
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        // Notify change
        assert getContext() != null;
        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;
    }

    @Override
    public int delete(@NonNull Uri uri, String selection, String[] selectionArgs) {
        int rows;
        SQLiteDatabase db = mOpenHelper.getReadableDatabase();

        switch (sUriMatcher.match(uri)) {
            case ACCOUNT:
                rows = db.delete(CCContract.AccountEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case CATEGORY:
                rows = db.delete(CCContract.CategoryEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case TRANSACTION:
                rows = db.delete(CCContract.TransactionEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case REPEATING_TRANSACTION:
                rows = db.delete(CCContract.RepeatingTransactionEntry.TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        if (rows != 0 || selection == null) {
            assert getContext() != null;
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rows;
    }

    @Override
    public int update(@NonNull Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        int rows;
        SQLiteDatabase db = mOpenHelper.getReadableDatabase();

        switch (sUriMatcher.match(uri)) {
            case ACCOUNT:
                rows = db.update(CCContract.AccountEntry.TABLE_NAME, values, selection, selectionArgs);
                break;
            case TRANSACTION:
                rows = db.update(CCContract.TransactionEntry.TABLE_NAME, values, selection, selectionArgs);
                break;
            case REPEATING_TRANSACTION:
                rows = db.update(CCContract.RepeatingTransactionEntry.TABLE_NAME, values, selection, selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        if (rows != 0) {
            assert getContext() != null;
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rows;
    }
}
