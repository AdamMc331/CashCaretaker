package com.androidessence.cashcaretaker.data;

import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * A contract class that include the Schema for the Cash Caretaker database.
 *
 * Created by adammcneilly on 10/30/15.
 */
public class CCContract {
    // Content authority is a name for the entire content provider
    // similar to a domain and its website. This string is guaranteed to be unique.
    public static final String CONTENT_AUTHORITY = "com.androidessence.cashcaretaker";

    // Use the content authority to provide the base
    // of all URIs
    private static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    // Possible paths for URIs
    public static final String PATH_ACCOUNT = "account";
    public static final String PATH_CATEGORY = "category";
    public static final String PATH_TRANSACTION = "transaction";
    public static final String PATH_REPEATING_PERIOD = "repeatingPeriod";
    public static final String PATH_REPEATING_TRANSACTION = "repeatingTransaction";
    public static final String PATH_DETAILS = "details";

    /**
     * A class representing an Account entry in the database.
     */
    public static final class AccountEntry implements BaseColumns {
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_ACCOUNT).build();

        public static final String CONTENT_TYPE =
                "vnd.android.cursor.dir/" + CONTENT_URI + "/" + PATH_ACCOUNT;
        public static final String CONTENT_ITEM_TYPE =
                "vnd.android.cursor.item/" + CONTENT_URI + "/" + PATH_ACCOUNT;

        public static final String TABLE_NAME = "accountTable";
        public static final String COLUMN_NAME = "accountName";
        public static final String COLUMN_BALANCE = "accountBalance";

        public static Uri buildAccountUri(long id){
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }

    /**
     * A class representing a Category entry in the database.
     */
    public static final class CategoryEntry implements BaseColumns {
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_CATEGORY).build();

        public static final String CONTENT_TYPE =
                "vnd.android.cursor.dir/" + CONTENT_URI + "/" + PATH_CATEGORY;
        public static final String CONTENT_ITEM_TYPE =
                "vnd.android.cursor.item/" + CONTENT_URI + "/" + PATH_CATEGORY;

        public static final String TABLE_NAME = "categoryTable";
        public static final String COLUMN_DESCRIPTION = "categoryDescription";
        public static final String COLUMN_IS_DEFAULT = "categoryIsDefault";

        public static Uri buildCategoryUri(long id){
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }

    /**
     * A class representing a Transaction entry in the database.
     */
    public static final class TransactionEntry implements BaseColumns {
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_TRANSACTION).build();

        public static final String CONTENT_TYPE =
                "vnd.android.cursor.dir/" + CONTENT_URI + "/" + PATH_TRANSACTION;
        public static final String CONTENT_ITEM_TYPE =
                "vnd.android.cursor.item/" + CONTENT_URI + "/" + PATH_TRANSACTION;

        public static final String TABLE_NAME = "transactionTable";
        public static final String COLUMN_DESCRIPTION = "transactionDescription";
        public static final String COLUMN_AMOUNT = "transactionAmount";
        public static final String COLUMN_NOTES = "transactionNotes";
        public static final String COLUMN_DATE = "transactionDate";
        public static final String COLUMN_CATEGORY = "transactionCategory";
        public static final String COLUMN_WITHDRAWAL = "transactionWithdrawal";
        public static final String COLUMN_ACCOUNT = "transactionAccount";

        public static Uri buildTransactionUri(long id){
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

        public static Uri buildTransactionsForAccountUri(long account){
            Uri accountUri = CONTENT_URI.buildUpon().appendPath(PATH_ACCOUNT).build();
            return ContentUris.withAppendedId(accountUri, account);
        }
    }

    /**
     * Class representing an entry for a repeating period (monthly, yearly, etc).
     */
    public static final class RepeatingPeriodEntry implements BaseColumns {
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_REPEATING_PERIOD).build();

        public static final String CONTENT_TYPE =
                "vnd.android.cursor.dir/" + CONTENT_URI + "/" + PATH_REPEATING_PERIOD;
        public static final String CONTENT_ITEM_TYPE =
                "vnd.android.cursor.item/" + CONTENT_URI + "/" + PATH_REPEATING_PERIOD;

        public static final String TABLE_NAME = "repeatingPeriodTable";
        public static final String COLUMN_NAME = "repeatingPeriodName";

        public static Uri buildRepeatingPeriodUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }

    /**
     * Class representing an entry for a repeating transaction.
     */
    public static final class RepeatingTransactionEntry implements BaseColumns {
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_REPEATING_TRANSACTION).build();

        public static final Uri CONTENT_DETAILS_URI =
                CONTENT_URI.buildUpon().appendPath(PATH_DETAILS).build();

        public static final String CONTENT_TYPE =
                "vnd.android.cursor.dir/" + CONTENT_URI + "/" + PATH_REPEATING_TRANSACTION;
        public static final String CONTENT_ITEM_TYPE =
                "vnd.android.cursor.item/" + CONTENT_URI + "/" + PATH_REPEATING_TRANSACTION;

        public static final String TABLE_NAME = "repeatingTransactionTable";
        public static final String COLUMN_REPEATING_PERIOD = "repTransRepeatingPeriod";
        public static final String COLUMN_ACCOUNT = "repTransAccount";
        public static final String COLUMN_DESCRIPTION = "repTransDescription";
        public static final String COLUMN_AMOUNT = "repTransAmount";
        public static final String COLUMN_NOTES = "repTransNotes";
        public static final String COLUMN_CATEGORY = "repTransCategory";
        public static final String COLUMN_NEXT_DATE = "repTransNextDate";
        public static final String COLUMN_WITHDRAWAL = "repTransWithdrawal";

        public static Uri buildRepeatingTransactionUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }
}
