package com.androidessence.cashcaretaker;

import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Contract class that defines the database schema for the database on an Android Wear device.
 *
 * Created by adammcneilly on 12/28/15.
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

    public static class AccountEntry implements BaseColumns {
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_ACCOUNT).build();

        public static final String CONTENT_TYPE =
                "vnd.android.cursor.dir/" + CONTENT_AUTHORITY + "/" + PATH_ACCOUNT;

        public static final String TABLE_NAME = "accountTable";
        public static final String COLUMN_NAME = "accountName";
        public static final String COLUMN_BALANCE = "accountBalance";

        public static Uri buildAccountUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }
}
