package com.androidessence.cashcaretaker;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.Nullable;

/**
 * ContentProvider on the Android wear device to access information.
 *
 * Created by adammcneilly on 12/28/15.
 */
public class CCProvider extends ContentProvider {
    private static final int ACCOUNT = 0;


    private CCDatabaseHelper openHelper;
    private static UriMatcher uriMatcher = buildUriMatcher();

    private static UriMatcher buildUriMatcher() {
        String content = CCContract.CONTENT_AUTHORITY;

        UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        matcher.addURI(content, CCContract.PATH_ACCOUNT, ACCOUNT);

        return matcher;
    }


    @Override
    public boolean onCreate() {
        openHelper = new CCDatabaseHelper(getContext());

        return true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        Cursor retCursor;
        SQLiteDatabase db = openHelper.getReadableDatabase();

        switch(uriMatcher.match(uri)) {
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
    public String getType(Uri uri) {
        switch(uriMatcher.match(uri)) {
            case ACCOUNT:
                return CCContract.AccountEntry.CONTENT_TYPE;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        long _id;
        Uri returnUri;
        SQLiteDatabase db = openHelper.getReadableDatabase();

        switch(uriMatcher.match(uri)) {
            case ACCOUNT:
                _id = db.insertWithOnConflict(CCContract.AccountEntry.TABLE_NAME, null, values, SQLiteDatabase.CONFLICT_REPLACE);
                if(_id > 0) {
                    returnUri = CCContract.AccountEntry.buildAccountUri(_id);
                } else {
                    throw new UnsupportedOperationException("Unable to insert row into: " + uri);
                }
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        // Update
        assert getContext() != null;
        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int rows;
        SQLiteDatabase db = openHelper.getReadableDatabase();

        switch(uriMatcher.match(uri)) {
            case ACCOUNT:
                rows = db.delete(CCContract.AccountEntry.TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        if(rows != 0 || selection == null) {
            assert getContext() != null;
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return rows;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }

    @Override
    public int bulkInsert(Uri uri, ContentValues[] values) {
        SQLiteDatabase db = openHelper.getReadableDatabase();

        switch(uriMatcher.match(uri)) {
            case ACCOUNT:
                db.beginTransaction();
                int returnCount = 0;
                try {
                    for(ContentValues value : values) {
                        long _id = db.insert(CCContract.AccountEntry.TABLE_NAME, null, value);
                        if(_id > 0) {
                            returnCount++;
                        }
                    }

                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }

                assert getContext() != null;
                getContext().getContentResolver().notifyChange(uri, null);
                return returnCount;
            default:
                return super.bulkInsert(uri, values);
        }
    }
}
