package com.androidessence.cashcaretaker;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by adammcneilly on 12/28/15.
 */
public class CCDatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "cashcaretaker.db";
    private static final int DATABASE_VERSION = 1;

    public CCDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        buildAccountTable(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    private void buildAccountTable(SQLiteDatabase db) {
        db.execSQL(
                "CREATE TABLE " + CCContract.AccountEntry.TABLE_NAME + " (" +
                        CCContract.AccountEntry._ID + " INTEGER PRIMARY KEY, " +
                        CCContract.AccountEntry.COLUMN_NAME + " TEXT UNIQUE NOT NULL, " +
                        CCContract.AccountEntry.COLUMN_BALANCE + " REAL NOT NULL);"
        );
    }
}
