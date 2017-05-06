package com.androidessence.cashcaretaker.data

import android.content.ContentProvider
import android.content.ContentUris
import android.content.ContentValues
import android.content.UriMatcher
import android.database.Cursor
import android.database.sqlite.SQLiteQueryBuilder
import android.net.Uri
import android.provider.BaseColumns

/**
 * Content provider used for accessing the data model of Cash Caretaker.

 * Created by adammcneilly on 10/30/15.
 */
class CCProvider : ContentProvider() {

    private var openHelper: CCDatabaseHelper? = null
    private val uriMatcher = buildUriMatcher()

    override fun onCreate(): Boolean {
        openHelper = CCDatabaseHelper(context!!)
        return true
    }

    override fun query(uri: Uri, projection: Array<String>?, selection: String?, selectionArgs: Array<String>?, sortOrder: String?): Cursor? {
        val db = openHelper!!.readableDatabase
        val _id: Long
        val retCursor: Cursor

        when (uriMatcher.match(uri)) {
            ACCOUNT -> retCursor = db.query(
                    CCContract.AccountEntry.TABLE_NAME,
                    projection,
                    selection,
                    selectionArgs, null, null,
                    sortOrder
            )
            ACCOUNT_ID -> {
                _id = ContentUris.parseId(uri)
                retCursor = db.query(
                        CCContract.AccountEntry.TABLE_NAME,
                        projection,
                        BaseColumns._ID + " = ?",
                        arrayOf(_id.toString()), null, null,
                        sortOrder
                )
            }
            CATEGORY -> retCursor = db.query(
                    CCContract.CategoryEntry.TABLE_NAME,
                    projection,
                    selection,
                    selectionArgs, null, null,
                    sortOrder
            )
            TRANSACTION -> retCursor = db.query(
                    CCContract.TransactionEntry.TABLE_NAME,
                    projection,
                    selection,
                    selectionArgs, null, null,
                    sortOrder
            )
            TRANSACTION_ID -> {
                _id = ContentUris.parseId(uri)
                retCursor = db.query(
                        CCContract.TransactionEntry.TABLE_NAME,
                        projection,
                        BaseColumns._ID + " = ?",
                        arrayOf(_id.toString()), null, null,
                        sortOrder
                )
            }
            TRANSACTION_FOR_ACCOUNT -> {
                _id = ContentUris.parseId(uri)
                retCursor = transactionWithCategoryBuilder.query(
                        db,
                        projection,
                        CCContract.TransactionEntry.COLUMN_ACCOUNT + " = ?",
                        arrayOf(_id.toString()), null, null,
                        sortOrder
                )
            }
            TRANSACTION_FOR_ACCOUNT_DESCRIPTION -> {
                // Decided not to query by account, but all transaction descriptions.
                // _id = ContentUris.parseId(uri);
                val description = CCContract.TransactionEntry.getDescriptionFromUri(uri)
                retCursor = db.query(
                        CCContract.TransactionEntry.TABLE_NAME,
                        projection,
                        CCContract.TransactionEntry.COLUMN_DESCRIPTION + " LIKE ?",
                        arrayOf("%$description%"),
                        CCContract.TransactionEntry.COLUMN_DESCRIPTION, null,
                        sortOrder
                )// Get unique descriptions by grouping by them
            }
            REPEATING_PERIOD -> retCursor = db.query(
                    CCContract.RepeatingPeriodEntry.TABLE_NAME,
                    projection,
                    selection,
                    selectionArgs, null, null,
                    sortOrder
            )
            REPEATING_TRANSACTION -> retCursor = db.query(
                    CCContract.RepeatingTransactionEntry.TABLE_NAME,
                    projection,
                    selection,
                    selectionArgs, null, null,
                    sortOrder
            )
            REPEATING_TRANSACTION_ID -> {
                _id = ContentUris.parseId(uri)
                retCursor = db.query(
                        CCContract.RepeatingTransactionEntry.TABLE_NAME,
                        projection,
                        BaseColumns._ID + " = ?",
                        arrayOf(_id.toString()), null, null,
                        sortOrder
                )
            }
            REPEATING_TRANSACTION_DETAILS -> retCursor = repeatingTransactionDetailsBuilder.query(
                    db,
                    projection,
                    selection,
                    selectionArgs, null, null,
                    sortOrder
            )
            else -> throw UnsupportedOperationException("Unknown uri: " + uri)
        }

        // Notify any followers
        assert(context != null)
        retCursor.setNotificationUri(context!!.contentResolver, uri)
        return retCursor
    }

    override fun getType(uri: Uri): String? {
        when (uriMatcher.match(uri)) {
            ACCOUNT -> return CCContract.AccountEntry.CONTENT_TYPE
            ACCOUNT_ID -> return CCContract.AccountEntry.CONTENT_ITEM_TYPE
            CATEGORY -> return CCContract.CategoryEntry.CONTENT_TYPE
            TRANSACTION, TRANSACTION_FOR_ACCOUNT -> return CCContract.TransactionEntry.CONTENT_TYPE
            TRANSACTION_ID -> return CCContract.TransactionEntry.CONTENT_ITEM_TYPE
            REPEATING_PERIOD -> return CCContract.RepeatingPeriodEntry.CONTENT_TYPE
            REPEATING_TRANSACTION, REPEATING_TRANSACTION_DETAILS -> return CCContract.RepeatingTransactionEntry.CONTENT_TYPE
            REPEATING_TRANSACTION_ID -> return CCContract.RepeatingTransactionEntry.CONTENT_ITEM_TYPE
            else -> throw UnsupportedOperationException("Unknown uri: " + uri)
        }
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        val _id: Long
        val returnUri: Uri
        val db = openHelper!!.readableDatabase

        when (uriMatcher.match(uri)) {
            ACCOUNT -> {
                _id = db.insertOrThrow(CCContract.AccountEntry.TABLE_NAME, null, values)
                if (_id > 0) {
                    returnUri = CCContract.AccountEntry.buildAccountUri(_id)
                } else {
                    throw UnsupportedOperationException("Unable to insert row into: " + uri)
                }
            }
            CATEGORY -> {
                _id = db.insert(CCContract.CategoryEntry.TABLE_NAME, null, values)
                if (_id > 0) {
                    returnUri = CCContract.CategoryEntry.buildCategoryUri(_id)
                } else {
                    throw UnsupportedOperationException("Unable to insert row into: " + uri)
                }
            }
            TRANSACTION -> {
                _id = db.insert(CCContract.TransactionEntry.TABLE_NAME, null, values)
                if (_id > 0) {
                    returnUri = CCContract.TransactionEntry.buildTransactionUri(_id)
                } else {
                    throw UnsupportedOperationException("Unable to insert row into: " + uri)
                }
            }
            REPEATING_TRANSACTION -> {
                _id = db.insert(CCContract.RepeatingTransactionEntry.TABLE_NAME, null, values)
                if (_id > 0) {
                    returnUri = CCContract.RepeatingTransactionEntry.buildRepeatingTransactionUri(_id)
                } else {
                    throw UnsupportedOperationException("Unable to insert row into: " + uri)
                }
            }
            else -> throw UnsupportedOperationException("Unknown uri: " + uri)
        }

        // Notify change
        assert(context != null)
        context!!.contentResolver.notifyChange(uri, null)
        return returnUri
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<String>?): Int {
        val rows: Int
        val db = openHelper!!.readableDatabase

        when (uriMatcher.match(uri)) {
            ACCOUNT -> rows = db.delete(CCContract.AccountEntry.TABLE_NAME, selection, selectionArgs)
            CATEGORY -> rows = db.delete(CCContract.CategoryEntry.TABLE_NAME, selection, selectionArgs)
            TRANSACTION -> rows = db.delete(CCContract.TransactionEntry.TABLE_NAME, selection, selectionArgs)
            REPEATING_TRANSACTION -> rows = db.delete(CCContract.RepeatingTransactionEntry.TABLE_NAME, selection, selectionArgs)
            else -> throw UnsupportedOperationException("Unknown uri: " + uri)
        }

        if (rows != 0 || selection == null) {
            assert(context != null)
            context!!.contentResolver.notifyChange(uri, null)
        }
        return rows
    }

    override fun update(uri: Uri, values: ContentValues?, selection: String?, selectionArgs: Array<String>?): Int {
        val rows: Int
        val db = openHelper!!.readableDatabase

        when (uriMatcher.match(uri)) {
            ACCOUNT -> rows = db.update(CCContract.AccountEntry.TABLE_NAME, values, selection, selectionArgs)
            TRANSACTION -> rows = db.update(CCContract.TransactionEntry.TABLE_NAME, values, selection, selectionArgs)
            REPEATING_TRANSACTION -> rows = db.update(CCContract.RepeatingTransactionEntry.TABLE_NAME, values, selection, selectionArgs)
            else -> throw UnsupportedOperationException("Unknown uri: " + uri)
        }

        if (rows != 0) {
            assert(context != null)
            context!!.contentResolver.notifyChange(uri, null)
        }
        return rows
    }

    companion object {
        private val ACCOUNT = 0
        private val ACCOUNT_ID = 1
        private val CATEGORY = 10
        private val TRANSACTION = 20
        private val TRANSACTION_ID = 21
        private val TRANSACTION_FOR_ACCOUNT = 22
        private val TRANSACTION_FOR_ACCOUNT_DESCRIPTION = 23
        private val REPEATING_PERIOD = 30
        private val REPEATING_TRANSACTION = 40
        private val REPEATING_TRANSACTION_ID = 41
        private val REPEATING_TRANSACTION_DETAILS = 42

        private fun buildUriMatcher(): UriMatcher {
            val content = CCContract.CONTENT_AUTHORITY

            val matcher = UriMatcher(UriMatcher.NO_MATCH)
            matcher.addURI(content, CCContract.PATH_ACCOUNT, ACCOUNT)
            matcher.addURI(content, CCContract.PATH_ACCOUNT + "/#", ACCOUNT_ID)
            matcher.addURI(content, CCContract.PATH_CATEGORY, CATEGORY)
            matcher.addURI(content, CCContract.PATH_TRANSACTION, TRANSACTION)
            matcher.addURI(content, CCContract.PATH_TRANSACTION + "/#", TRANSACTION_ID)
            matcher.addURI(content, CCContract.PATH_TRANSACTION + "/" + CCContract.PATH_ACCOUNT + "/#", TRANSACTION_FOR_ACCOUNT)
            matcher.addURI(content, CCContract.PATH_TRANSACTION + "/*/" + CCContract.PATH_ACCOUNT + "/#", TRANSACTION_FOR_ACCOUNT_DESCRIPTION)
            matcher.addURI(content, CCContract.PATH_REPEATING_PERIOD, REPEATING_PERIOD)
            matcher.addURI(content, CCContract.PATH_REPEATING_TRANSACTION, REPEATING_TRANSACTION)
            matcher.addURI(content, CCContract.PATH_REPEATING_TRANSACTION + "/#", REPEATING_TRANSACTION_ID)
            matcher.addURI(content, CCContract.PATH_REPEATING_TRANSACTION + "/" + CCContract.PATH_DETAILS, REPEATING_TRANSACTION_DETAILS)

            return matcher
        }

        private val transactionWithCategoryBuilder: SQLiteQueryBuilder

        init {
            transactionWithCategoryBuilder = SQLiteQueryBuilder()
            transactionWithCategoryBuilder.tables = CCContract.TransactionEntry.TABLE_NAME + " " +
                    "LEFT JOIN " + CCContract.CategoryEntry.TABLE_NAME + " ON " +
                    CCContract.CategoryEntry.TABLE_NAME + "." + BaseColumns._ID + " = " + CCContract.TransactionEntry.COLUMN_CATEGORY
        }

        private val repeatingTransactionDetailsBuilder: SQLiteQueryBuilder

        init {
            repeatingTransactionDetailsBuilder = SQLiteQueryBuilder()
            repeatingTransactionDetailsBuilder.tables = CCContract.RepeatingTransactionEntry.TABLE_NAME + " " +
                    "LEFT JOIN " + CCContract.CategoryEntry.TABLE_NAME + " ON " +
                    CCContract.CategoryEntry.TABLE_NAME + "." + BaseColumns._ID + " = " + CCContract.RepeatingTransactionEntry.COLUMN_CATEGORY + " " +
                    "LEFT JOIN " + CCContract.AccountEntry.TABLE_NAME + " ON " +
                    CCContract.AccountEntry.TABLE_NAME + "." + BaseColumns._ID + " = " + CCContract.RepeatingTransactionEntry.COLUMN_ACCOUNT + " " +
                    "LEFT JOIN " + CCContract.RepeatingPeriodEntry.TABLE_NAME + " ON " +
                    CCContract.RepeatingPeriodEntry.TABLE_NAME + "." + BaseColumns._ID + " = " + CCContract.RepeatingTransactionEntry.COLUMN_REPEATING_PERIOD
        }
    }
}
