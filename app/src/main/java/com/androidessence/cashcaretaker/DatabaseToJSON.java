package com.androidessence.cashcaretaker;

import android.content.Context;
import android.database.Cursor;

import com.androidessence.cashcaretaker.data.CCContract;
import com.androidessence.cashcaretaker.dataTransferObjects.AccountR;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Helper class to convert some database information to JSON objects to be sent from the handheld to
 * the Wear device.
 *
 * Created by adammcneilly on 12/28/15.
 */
public class DatabaseToJSON {
    private Context mContext;

    public DatabaseToJSON(Context context) {
        this.mContext = context;
    }

    public JSONArray getAccountJSON() throws JSONException {
        JSONArray root = new JSONArray();

        List<AccountR> accounts = getAccounts();

        for(AccountR account: accounts) {
            JSONObject object = new JSONObject();

            object.put(CCContract.AccountEntry._ID, account.getIdentifier());
            object.put(CCContract.AccountEntry.COLUMN_NAME, account.getName());
            object.put(CCContract.AccountEntry.COLUMN_BALANCE, account.getBalance());

            root.put(object);
        }

        return root;
    }

    private List<AccountR> getAccounts() {
        List<AccountR> accounts = new ArrayList<>();

        Cursor cursor = mContext.getContentResolver().query(
                CCContract.AccountEntry.CONTENT_URI,
                null,
                null,
                null,
                null
        );

        assert cursor != null;
        while(cursor.moveToNext()) {
            accounts.add(new AccountR(cursor));
        }

        cursor.close();

        return accounts;
    }
}
