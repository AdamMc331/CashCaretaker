package com.androidessence.cashcaretaker;

import android.content.ContentValues;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by adammcneilly on 12/27/15.
 */
public class Account {
    private long identifier;
    private String name;
    private double balance;

    public Account(long identifier, String name, double balance) {
        this.identifier = identifier;
        this.name = name;
        this.balance = balance;
    }

    public Account(JSONObject jsonObject) throws JSONException{
        this.identifier = jsonObject.getLong(CCContract.AccountEntry._ID);
        this.name = jsonObject.getString(CCContract.AccountEntry.COLUMN_NAME);
        this.balance = jsonObject.getDouble(CCContract.AccountEntry.COLUMN_BALANCE);
    }

    public long getIdentifier() {
        return identifier;
    }

    public void setIdentifier(long identifier) {
        this.identifier = identifier;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public ContentValues getContentValues() {
        ContentValues values = new ContentValues();

        values.put(CCContract.AccountEntry._ID, getIdentifier());
        values.put(CCContract.AccountEntry.COLUMN_NAME, getName());
        values.put(CCContract.AccountEntry.COLUMN_BALANCE, getBalance());

        return values;
    }
}
