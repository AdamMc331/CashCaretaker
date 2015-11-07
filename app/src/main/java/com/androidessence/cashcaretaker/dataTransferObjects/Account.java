package com.androidessence.cashcaretaker.dataTransferObjects;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import com.androidessence.cashcaretaker.data.CCContract;

/**
 * Class representing an Account object.
 *
 * Created by adammcneilly on 11/1/15.
 */
public class Account implements Parcelable{
    private long identifier;
    private String name;
    private double balance;

    public Account(String name, double balance) {
        this.name = name;
        this.balance = balance;
    }

    public Account(Cursor cursor){
        setIdentifier(cursor.getLong(cursor.getColumnIndex(CCContract.AccountEntry._ID)));
        setName(cursor.getString(cursor.getColumnIndex(CCContract.AccountEntry.COLUMN_NAME)));
        setBalance(cursor.getDouble(cursor.getColumnIndex(CCContract.AccountEntry.COLUMN_BALANCE)));
    }

    private Account(Parcel parcel){
        setIdentifier(parcel.readLong());
        setName(parcel.readString());
        setBalance(parcel.readDouble());
    }

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

    public long getIdentifier() {
        return identifier;
    }

    private void setIdentifier(long identifier) {
        this.identifier = identifier;
    }

    public String getName() {
        return name;
    }

    private void setName(String name) {
        this.name = name;
    }

    private double getBalance() {
        return balance;
    }

    private void setBalance(double balance) {
        this.balance = balance;
    }

    public ContentValues getContentValues(){
        ContentValues contentValues = new ContentValues();

        if(identifier > 0){
            contentValues.put(CCContract.AccountEntry._ID, getIdentifier());
        }

        contentValues.put(CCContract.AccountEntry.COLUMN_NAME, getName());
        contentValues.put(CCContract.AccountEntry.COLUMN_BALANCE, getBalance());

        return contentValues;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(getIdentifier());
        dest.writeString(getName());
        dest.writeDouble(getBalance());
    }
}
