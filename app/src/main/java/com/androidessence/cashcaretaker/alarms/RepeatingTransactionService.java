package com.androidessence.cashcaretaker.alarms;

import android.app.IntentService;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.androidessence.cashcaretaker.Utility;
import com.androidessence.cashcaretaker.data.CCContract;

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

    @Override
    protected void onHandleIntent(Intent intent) {
        mContext = getApplicationContext();

        // Check for any alarms prior to today
        getCurrentOrPreviousTransactions();
    }

    private void getCurrentOrPreviousTransactions() {
        String currentDate = Utility.getDBDateString(LocalDate.now());

        Cursor repeatingTransactions = mContext.getContentResolver().query(
                CCContract.RepeatingTransactionEntry.CONTENT_URI,
                null,
                CCContract.RepeatingTransactionEntry.COLUMN_NEXT_DATE + " <= ",
                new String[]{currentDate},
                null
        );

        assert repeatingTransactions != null;

        while(repeatingTransactions.moveToNext()) {
            String description = repeatingTransactions.getString(repeatingTransactions.getColumnIndex(CCContract.RepeatingTransactionEntry.COLUMN_DESCRIPTION));

            Log.v("ADAM", description);
        }

        repeatingTransactions.close();
    }
}
