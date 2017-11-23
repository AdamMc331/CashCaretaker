package com.androidessence.cashcaretaker.data

import android.content.ContentUris
import android.net.Uri
import android.provider.BaseColumns

/**
 * A contract class that include the Schema for the Cash Caretaker database.

 * Created by adammcneilly on 10/30/15.
 */
object CCContract {
    // Content authority is a name for the entire content provider
    // similar to a domain and its website. This string is guaranteed to be unique.
    val CONTENT_AUTHORITY = "com.androidessence.cashcaretaker"

    // Use the content authority to provide the base
    // of all URIs
    private val BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY)

    // Possible paths for URIs
    val PATH_ACCOUNT = "account"
    val PATH_CATEGORY = "category"
    val PATH_TRANSACTION = "transaction"
    val PATH_REPEATING_PERIOD = "repeatingPeriod"
    val PATH_REPEATING_TRANSACTION = "repeatingTransaction"
    val PATH_DETAILS = "details"

    /**
     * A class representing an Account entry in the database.
     */
    class AccountEntry : BaseColumns {
        companion object {
            val CONTENT_URI: Uri = BASE_CONTENT_URI.buildUpon().appendPath(PATH_ACCOUNT).build()

            val CONTENT_TYPE =
                    "vnd.android.cursor.dir/$CONTENT_URI/$PATH_ACCOUNT"
            val CONTENT_ITEM_TYPE =
                    "vnd.android.cursor.item/$CONTENT_URI/$PATH_ACCOUNT"

            val TABLE_NAME = "accountTable"
            val COLUMN_NAME = "accountName"
            val COLUMN_BALANCE = "accountBalance"

            fun buildAccountUri(id: Long): Uri = ContentUris.withAppendedId(CONTENT_URI, id)
        }
    }

    /**
     * A class representing a Category entry in the database.
     */
    class CategoryEntry : BaseColumns {
        companion object {
            val CONTENT_URI: Uri = BASE_CONTENT_URI.buildUpon().appendPath(PATH_CATEGORY).build()

            val CONTENT_TYPE =
                    "vnd.android.cursor.dir/$CONTENT_URI/$PATH_CATEGORY"
            val CONTENT_ITEM_TYPE =
                    "vnd.android.cursor.item/$CONTENT_URI/$PATH_CATEGORY"

            val TABLE_NAME = "categoryTable"
            val COLUMN_DESCRIPTION = "categoryDescription"
            val COLUMN_IS_DEFAULT = "categoryIsDefault"

            fun buildCategoryUri(id: Long): Uri = ContentUris.withAppendedId(CONTENT_URI, id)
        }
    }

    /**
     * A class representing a Transaction entry in the database.
     */
    class TransactionEntry : BaseColumns {
        companion object {
            val CONTENT_URI: Uri = BASE_CONTENT_URI.buildUpon().appendPath(PATH_TRANSACTION).build()

            val CONTENT_TYPE =
                    "vnd.android.cursor.dir/$CONTENT_URI/$PATH_TRANSACTION"
            val CONTENT_ITEM_TYPE =
                    "vnd.android.cursor.item/$CONTENT_URI/$PATH_TRANSACTION"

            val TABLE_NAME = "transactionTable"
            val COLUMN_DESCRIPTION = "transactionDescription"
            val COLUMN_AMOUNT = "transactionAmount"
            val COLUMN_NOTES = "transactionNotes"
            val COLUMN_DATE = "transactionDate"
            val COLUMN_CATEGORY = "transactionCategory"
            val COLUMN_WITHDRAWAL = "transactionWithdrawal"
            val COLUMN_ACCOUNT = "transactionAccount"

            fun buildTransactionUri(id: Long): Uri = ContentUris.withAppendedId(CONTENT_URI, id)
            

            fun buildTransactionsForAccountUri(account: Long): Uri {
                val accountUri = CONTENT_URI.buildUpon().appendPath(PATH_ACCOUNT).build()
                return ContentUris.withAppendedId(accountUri, account)
            }

            fun buildTransactionsForAccountWithDescriptionUri(account: Long, description: String): Uri {
                val transactionUri = CONTENT_URI.buildUpon().appendPath(description).appendPath(PATH_ACCOUNT).build()
                return ContentUris.withAppendedId(transactionUri, account)
            }

            fun getDescriptionFromUri(uri: Uri): String = uri.pathSegments[1]
        }
    }

    /**
     * Class representing an entry for a repeating period (monthly, yearly, etc).
     */
    class RepeatingPeriodEntry : BaseColumns {
        companion object {
            val CONTENT_URI: Uri = BASE_CONTENT_URI.buildUpon().appendPath(PATH_REPEATING_PERIOD).build()

            val CONTENT_TYPE =
                    "vnd.android.cursor.dir/$CONTENT_URI/$PATH_REPEATING_PERIOD"
            val CONTENT_ITEM_TYPE =
                    "vnd.android.cursor.item/$CONTENT_URI/$PATH_REPEATING_PERIOD"

            val TABLE_NAME = "repeatingPeriodTable"
            val COLUMN_NAME = "repeatingPeriodName"

            fun buildRepeatingPeriodUri(id: Long): Uri = ContentUris.withAppendedId(CONTENT_URI, id)
        }
    }

    /**
     * Class representing an entry for a repeating transaction.
     */
    class RepeatingTransactionEntry : BaseColumns {
        companion object {
            val CONTENT_URI: Uri = BASE_CONTENT_URI.buildUpon().appendPath(PATH_REPEATING_TRANSACTION).build()

            val CONTENT_DETAILS_URI: Uri = CONTENT_URI.buildUpon().appendPath(PATH_DETAILS).build()

            val CONTENT_TYPE =
                    "vnd.android.cursor.dir/$CONTENT_URI/$PATH_REPEATING_TRANSACTION"
            val CONTENT_ITEM_TYPE =
                    "vnd.android.cursor.item/$CONTENT_URI/$PATH_REPEATING_TRANSACTION"

            val TABLE_NAME = "repeatingTransactionTable"
            val COLUMN_REPEATING_PERIOD = "repTransRepeatingPeriod"
            val COLUMN_ACCOUNT = "repTransAccount"
            val COLUMN_DESCRIPTION = "repTransDescription"
            val COLUMN_AMOUNT = "repTransAmount"
            val COLUMN_NOTES = "repTransNotes"
            val COLUMN_CATEGORY = "repTransCategory"
            val COLUMN_NEXT_DATE = "repTransNextDate"
            val COLUMN_WITHDRAWAL = "repTransWithdrawal"

            fun buildRepeatingTransactionUri(id: Long): Uri = ContentUris.withAppendedId(CONTENT_URI, id)
        }
    }
}
