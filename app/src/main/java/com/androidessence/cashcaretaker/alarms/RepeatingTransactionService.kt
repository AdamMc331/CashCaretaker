package com.androidessence.cashcaretaker.alarms

import android.app.IntentService
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.provider.BaseColumns
import android.util.Log
import com.androidessence.cashcaretaker.data.CCContract
import com.androidessence.cashcaretaker.dataTransferObjects.RepeatingTransaction
import com.androidessence.utility.Utility
import java.util.*

/**
 * Service that checks for repeating transactions and runs them if necessary.
 *
 *
 * Created by adammcneilly on 11/15/15.
 */
class RepeatingTransactionService : IntentService("RepeatingTransactionService") {
    private var context: Context? = null

    override fun onHandleIntent(intent: Intent?) {
        context = applicationContext

        // Check for any alarms prior to today
        updateRepeatingTransactions()
    }

    /**
     * Updates any current or previous RepeatingTransaction entries that need to be run.
     */
    private fun updateRepeatingTransactions() {
        val currentDate = Utility.getDBDateString(Date())

        // We need an outer loop so we continue to check until we are caught up.
        // If we process *any* transaction, set `hasTrans` to yes, and continue until we don't.

        var hasTrans: Boolean

        do {
            hasTrans = false

            val cursor = context?.contentResolver?.query(
                    CCContract.RepeatingTransactionEntry.CONTENT_URI,
                    REPEATING_TRANSACTION_COLUMNS,
                    CCContract.RepeatingTransactionEntry.COLUMN_NEXT_DATE + " <= ?",
                    arrayOf(currentDate), null
            )!!

            while (cursor.moveToNext()) {
                hasTrans = true
                // Get repeating transaction
                val repeatingTransaction = RepeatingTransaction(cursor)

                // Insert transaction
                context?.contentResolver?.insert(CCContract.TransactionEntry.CONTENT_URI, repeatingTransaction.transactionContentValues)

                // Switch based on update
                val transDateString = cursor.getString(DATE_INDEX)
                val transDate = Utility.getDateFromDb(transDateString)
                var nextDate: Date? = null
                val calendar = Calendar.getInstance()
                calendar.time = transDate
                when (cursor.getInt(REPEATING_PERIOD_INDEX)) {
                    MONTHLY -> {
                        // Update monthly
                        calendar.add(Calendar.MONTH, 1)
                        nextDate = calendar.time
                    }
                    YEARLY -> {
                        // Update yearly
                        calendar.add(Calendar.YEAR, 1)
                        nextDate = calendar.time
                    }
                    else -> {
                    }
                }

                if (nextDate != null) {
                    val description = cursor.getString(DESCRIPTION_INDEX)
                    Log.v("ADAM", description)
                    Log.v("ADAM", "Current date: " + transDateString)
                    Log.v("ADAM", "Future date: " + Utility.getDBDateString(nextDate))

                    // Update to next date.
                    val contentValues = ContentValues()
                    contentValues.put(CCContract.RepeatingTransactionEntry.COLUMN_NEXT_DATE, Utility.getDBDateString(nextDate))

                    val id = cursor.getLong(ID_INDEX)
                    context?.contentResolver?.update(
                            CCContract.RepeatingTransactionEntry.CONTENT_URI,
                            contentValues,
                            BaseColumns._ID + " = ?",
                            arrayOf(id.toString())
                    )
                }
            }

            cursor.close()
        } while (hasTrans)

        Log.v("ADAM", "Service called.")
    }

    companion object {
        // Keep track of indexes for repeating periods. These are hard coded.
        //TODO: Find better idea
        private val MONTHLY = 1
        private val YEARLY = 2

        // Necessary repeating transaction columns
        private val REPEATING_TRANSACTION_COLUMNS = arrayOf(CCContract.RepeatingTransactionEntry.TABLE_NAME + "." + BaseColumns._ID, CCContract.RepeatingTransactionEntry.COLUMN_DESCRIPTION, CCContract.RepeatingTransactionEntry.COLUMN_AMOUNT, CCContract.RepeatingTransactionEntry.COLUMN_WITHDRAWAL, CCContract.RepeatingTransactionEntry.COLUMN_NEXT_DATE, CCContract.RepeatingTransactionEntry.COLUMN_NOTES, CCContract.RepeatingTransactionEntry.COLUMN_ACCOUNT, CCContract.RepeatingTransactionEntry.COLUMN_REPEATING_PERIOD, CCContract.RepeatingTransactionEntry.COLUMN_CATEGORY)

        private val ID_INDEX = 0
        private val DESCRIPTION_INDEX = 1
        private val DATE_INDEX = 4
        private val REPEATING_PERIOD_INDEX = 7
    }
}
