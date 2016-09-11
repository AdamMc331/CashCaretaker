package com.androidessence.cashcaretaker;

import android.content.ContentValues;
import android.util.Log;

import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.WearableListenerService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Service that listens for a message from the mobile handset.
 *
 * Created by adammcneilly on 12/27/15.
 */
public class ListenerService extends WearableListenerService {
    @Override
    public void onMessageReceived(MessageEvent messageEvent) {

        Log.v("ADAMANT", "Message received.");

        if(messageEvent.getPath().equals(getString(R.string.add_account_path))) {
            String jsonString = new String(messageEvent.getData());

            try{
                JSONArray jsonArray = new JSONArray(jsonString);

                // Create ContentValues array to be inserted
                ContentValues[] contentValues = new ContentValues[jsonArray.length()];

                for(int i = 0; i < jsonArray.length(); i++) {
                    JSONObject object = jsonArray.getJSONObject(i);

                    Account account = new Account(object.getLong(CCContract.AccountEntry._ID), object.getString(CCContract.AccountEntry.COLUMN_NAME), object.getDouble(CCContract.AccountEntry.COLUMN_BALANCE));
                    contentValues[i] = account.getContentValues();
                }

                // Delete accounts
                getContentResolver().delete(CCContract.AccountEntry.CONTENT_URI, null, null);

                // Bulk insert accounts
                getContentResolver().bulkInsert(CCContract.AccountEntry.CONTENT_URI, contentValues);
            } catch(JSONException joe) {
                //TODO:
            }
        } else if(messageEvent.getPath().equals(getString(R.string.delete_account_path))) {
            String identifer = new String(messageEvent.getData());

            getContentResolver().delete(
                    CCContract.AccountEntry.CONTENT_URI,
                    CCContract.AccountEntry._ID + " = ?",
                    new String[] {identifer}
            );
        }
    }
}
