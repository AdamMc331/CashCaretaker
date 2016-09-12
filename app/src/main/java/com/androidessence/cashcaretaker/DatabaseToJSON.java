package com.androidessence.cashcaretaker;

import android.content.Context;
import android.database.Cursor;

import com.androidessence.cashcaretaker.data.CCContract;
import com.androidessence.cashcaretaker.dataTransferObjects.Account;

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
    private Context context;

    public DatabaseToJSON(Context context) {
        this.context = context;
    }

    public JSONArray getAccountJSON() throws JSONException {
        JSONArray root = new JSONArray();

        List<Account> accounts = getAccounts();

        for(Account account: accounts) {
            JSONObject object = new JSONObject();

            object.put(CCContract.AccountEntry._ID, account.getIdentifier());
            object.put(CCContract.AccountEntry.COLUMN_NAME, account.getName());
            object.put(CCContract.AccountEntry.COLUMN_BALANCE, account.getBalance());

            root.put(object);
        }

        return root;
    }

    private List<Account> getAccounts() {
        List<Account> accounts = new ArrayList<>();

        Cursor cursor = context.getContentResolver().query(
                CCContract.AccountEntry.CONTENT_URI,
                null,
                null,
                null,
                null
        );

        assert cursor != null;
        while(cursor.moveToNext()) {
            accounts.add(new Account(cursor));
        }

        cursor.close();

        return accounts;
    }
}
