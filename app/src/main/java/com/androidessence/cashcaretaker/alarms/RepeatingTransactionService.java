package com.androidessence.cashcaretaker.alarms;

import android.app.IntentService;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.util.Log;

import com.androidessence.cashcaretaker.Utility;
import com.androidessence.cashcaretaker.data.CCContract;
import com.androidessence.cashcaretaker.dataTransferObjects.RepeatingTransaction;

import org.joda.time.LocalDate;

/**
 * Service that checks for repeating transactions and runs them if necessary.
 *
 * Created by adammcneilly on 11/15/15.
 */
public class RepeatingTransactionService extends IntentService {
    private Context mContext;

    public RepeatingTransactionService() {
        super("RepeatingTransactionService");
    }

    // Keep track of indexes for repeating periods. These are hard coded.
    //TODO: Find better idea
    private static final int MONTHLY = 1;
    private static final int YEARLY = 2;

    @Override
    protected void onHandleIntent(Intent intent) {
        mContext = getApplicationContext();

        // Check for any alarms prior to today
        getCurrentOrPreviousRepeatingTransactions();
    }

    private void getCurrentOrPreviousRepeatingTransactions() {
        String currentDate = Utility.getDBDateString(LocalDate.now());

        Cursor cursor = mContext.getContentResolver().query(
                CCContract.RepeatingTransactionEntry.CONTENT_URI,
                null,
                CCContract.RepeatingTransactionEntry.COLUMN_NEXT_DATE + " <= ?",
                new String[]{currentDate},
                null
        );

        assert cursor != null;

        while(cursor.moveToNext()) {
            // Get repeating transaction
            RepeatingTransaction repeatingTransaction = new RepeatingTransaction(cursor);

            // Insert transaction
            mContext.getContentResolver().insert(CCContract.TransactionEntry.CONTENT_URI, repeatingTransaction.getTransactionContentValues());

            // Switch based on update
            String nextTransDate = cursor.getString(cursor.getColumnIndex(CCContract.RepeatingTransactionEntry.COLUMN_NEXT_DATE));
            LocalDate nextDate = Utility.getDateFromDb(nextTransDate);
            LocalDate futureDate = null;
            switch(cursor.getInt(cursor.getColumnIndex(CCContract.RepeatingTransactionEntry.COLUMN_REPEATING_PERIOD))) {
                case MONTHLY:
                    // Update monthly
                    futureDate = nextDate.plusMonths(1);
                    break;
                case YEARLY:
                    // Update yearly
                    futureDate = nextDate.plusYears(1);
                    break;
                default:
                    break;
            }

            if(futureDate != null) {
                String description = cursor.getString(cursor.getColumnIndex(CCContract.RepeatingTransactionEntry.COLUMN_DESCRIPTION));
                Log.v("ADAM", description);
                Log.v("ADAM", "Current date: " + nextTransDate);
                Log.v("ADAM", "Future date: " + Utility.getDBDateString(futureDate));

                ContentValues contentValues = new ContentValues();
                contentValues.put(CCContract.RepeatingTransactionEntry.COLUMN_NEXT_DATE, Utility.getDBDateString(futureDate));

                // Update next date
                mContext.getContentResolver().update(
                        CCContract.RepeatingTransactionEntry.CONTENT_URI,
                        contentValues,
                        CCContract.RepeatingTransactionEntry._ID + " = ?",
                        new String[]{String.valueOf(cursor.getLong(cursor.getColumnIndex(CCContract.RepeatingTransactionEntry._ID)))}
                );
            }
        }

        cursor.close();
    }
}
