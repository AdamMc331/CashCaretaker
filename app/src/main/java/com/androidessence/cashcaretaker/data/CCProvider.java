package com.androidessence.cashcaretaker.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
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

    private CCDatabaseHelper mOpenHelper;
    private UriMatcher sUriMatcher = buildUriMatcher();

    private static UriMatcher buildUriMatcher() {
        String content = CCContract.CONTENT_AUTHORITY;

        UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        matcher.addURI(content, CCContract.PATH_ACCOUNT, ACCOUNT);
        matcher.addURI(content, CCContract.PATH_ACCOUNT + "/#", ACCOUNT_ID);
        matcher.addURI(content, CCContract.PATH_CATEGORY, CATEGORY);
        matcher.addURI(content, CCContract.PATH_TRANSACTION, TRANSACTION);
        matcher.addURI(content, CCContract.PATH_TRANSACTION + "/#", TRANSACTION_ID);
        matcher.addURI(content, CCContract.PATH_TRANSACTION + "/" + CCContract.PATH_ACCOUNT + "/#", TRANSACTION_FOR_ACCOUNT);

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

    @Override
    public boolean onCreate() {
        mOpenHelper = new CCDatabaseHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
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
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        // Notify any followers
        retCursor.setNotificationUri(getContext().getContentResolver(), uri);
        return retCursor;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        switch (sUriMatcher.match(uri)) {
            case ACCOUNT:
                return CCContract.AccountEntry.CONTENT_TYPE;
            case ACCOUNT_ID:
                return CCContract.AccountEntry.CONTENT_ITEM_TYPE;
            case CATEGORY:
                return CCContract.CategoryEntry.CONTENT_TYPE;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
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
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        // Notify change
        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
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
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        if (rows != 0 || selection == null) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rows;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        int rows;
        SQLiteDatabase db = mOpenHelper.getReadableDatabase();

        switch (sUriMatcher.match(uri)) {
            case ACCOUNT:
                rows = db.update(CCContract.AccountEntry.TABLE_NAME, values, selection, selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        if (rows != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rows;
    }
}
