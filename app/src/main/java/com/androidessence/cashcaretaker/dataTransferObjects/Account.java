package com.androidessence.cashcaretaker.dataTransferObjects;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Parcel;

import com.androidessence.cashcaretaker.core.CoreDTO;
import com.androidessence.cashcaretaker.data.CCContract;

/**
 * Represents a finance account for the user.
 *
 * Created by adam.mcneilly on 9/5/16.
 */
public class Account extends CoreDTO {
    private String name;
    private double balance;

    public static Creator<Account> CREATOR = new Creator<Account>() {
        @Override
        public Account createFromParcel(Parcel source) {
            return new Account(source);
        }

        @Override
        public Account[] newArray(int size) {
            return new Account[size];
        }
    };

    public Account(String name, double balance) {
        this.name = name;
        this.balance = balance;
    }

    public Account(Cursor cursor) {
        this.identifier = cursor.getLong(cursor.getColumnIndex(CCContract.AccountEntry._ID));
        this.name = cursor.getString(cursor.getColumnIndex(CCContract.AccountEntry.COLUMN_NAME));
        this.balance = cursor.getDouble(cursor.getColumnIndex(CCContract.AccountEntry.COLUMN_BALANCE));
    }

    public Account(Parcel parcel) {
        super(parcel);
        this.name = parcel.readString();
        this.balance = parcel.readDouble();
    }

    public String getName() {
        return name;
    }

    public double getBalance() {
        return balance;
    }

    @Override
    public ContentValues getContentValues() {
        ContentValues values = new ContentValues();

        if(identifier > 0) {
            values.put(CCContract.AccountEntry._ID, identifier);
        }

        values.put(CCContract.AccountEntry.COLUMN_NAME, name);
        values.put(CCContract.AccountEntry.COLUMN_BALANCE, balance);

        return values;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeString(name);
        dest.writeDouble(balance);
    }
}
