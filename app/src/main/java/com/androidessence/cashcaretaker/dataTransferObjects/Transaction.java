package com.androidessence.cashcaretaker.dataTransferObjects;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import com.androidessence.cashcaretaker.data.CCContract;
import com.androidessence.utility.Utility;

import org.joda.time.LocalDate;

/**
 * Class representing a Transaction for an account.
 *
 * Created by adammcneilly on 11/3/15.
 */
public class Transaction implements Parcelable{
    private long identifier;
    private long account;
    private String description;
    private double amount;
    private String notes;
    private LocalDate date;
    private long categoryID;
    private boolean withdrawal;

    public static final Creator<Transaction> CREATOR = new Creator<Transaction>() {
        @Override
        public Transaction createFromParcel(Parcel source) {
            return new Transaction(source);
        }

        @Override
        public Transaction[] newArray(int size) {
            return new Transaction[size];
        }
    };

    public Transaction(Parcel source) {
        setIdentifier(source.readLong());
        setAccount(source.readLong());
        setDescription(source.readString());
        setAmount(source.readDouble());
        setNotes(source.readString());
        setDate((LocalDate) source.readSerializable());
        setCategoryID(source.readLong());
        setWithdrawal(source.readInt() == 1);
    }

    public Transaction(Cursor cursor){
        setIdentifier(cursor.getLong(cursor.getColumnIndex(CCContract.TransactionEntry._ID)));
        setAccount(cursor.getLong(cursor.getColumnIndex(CCContract.TransactionEntry.COLUMN_ACCOUNT)));
        setDescription(cursor.getString(cursor.getColumnIndex(CCContract.TransactionEntry.COLUMN_DESCRIPTION)));
        setAmount(cursor.getDouble(cursor.getColumnIndex(CCContract.TransactionEntry.COLUMN_AMOUNT)));
        setNotes(cursor.getString(cursor.getColumnIndex(CCContract.TransactionEntry.COLUMN_NOTES)));
        String dateString = cursor.getString(cursor.getColumnIndex(CCContract.TransactionEntry.COLUMN_DATE));
        setDate(Utility.getDateFromDb(dateString));
        setCategoryID(cursor.getLong(cursor.getColumnIndex(CCContract.TransactionEntry.COLUMN_CATEGORY)));
        int withdrawalInt = cursor.getInt(cursor.getColumnIndex(CCContract.TransactionEntry.COLUMN_WITHDRAWAL));
        setWithdrawal(withdrawalInt == 1);
    }

    public Transaction(long account, String description, double amount, String notes, LocalDate date, long categoryID, boolean withdrawal) {
        setAccount(account);
        setDescription(description);
        setAmount(amount);
        setNotes(notes);
        setDate(date);
        setCategoryID(categoryID);
        setWithdrawal(withdrawal);
    }

    public long getIdentifier() {
        return identifier;
    }

    private void setIdentifier(long identifier) {
        this.identifier = identifier;
    }

    private long getAccount() {
        return account;
    }

    private void setAccount(long account) {
        this.account = account;
    }

    public String getDescription() {
        return description;
    }

    private void setDescription(String description) {
        this.description = description;
    }

    public double getAmount() {
        return amount;
    }

    private void setAmount(double amount) {
        this.amount = amount;
    }

    public String getNotes() {
        return notes;
    }

    private void setNotes(String notes) {
        this.notes = notes;
    }

    public LocalDate getDate() {
        return date;
    }

    private void setDate(LocalDate date) {
        this.date = date;
    }

    public long getCategoryID() {
        return categoryID;
    }

    private void setCategoryID(long category) {
        this.categoryID = category;
    }

    public boolean isWithdrawal() {
        return withdrawal;
    }

    private void setWithdrawal(boolean withdrawal) {
        this.withdrawal = withdrawal;
    }

    public ContentValues getContentValues(){
        ContentValues values = new ContentValues();

        if(identifier > 0){
            values.put(CCContract.TransactionEntry._ID, getIdentifier());
        }

        values.put(CCContract.TransactionEntry.COLUMN_ACCOUNT, getAccount());
        values.put(CCContract.TransactionEntry.COLUMN_DESCRIPTION, getDescription());
        values.put(CCContract.TransactionEntry.COLUMN_AMOUNT, getAmount());
        values.put(CCContract.TransactionEntry.COLUMN_NOTES, getNotes());
        values.put(CCContract.TransactionEntry.COLUMN_DATE, Utility.getDBDateString(getDate()));
        values.put(CCContract.TransactionEntry.COLUMN_CATEGORY, getCategoryID());
        values.put(CCContract.TransactionEntry.COLUMN_WITHDRAWAL, isWithdrawal() ? 1 : 0);

        return values;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(getIdentifier());
        dest.writeLong(getAccount());
        dest.writeString(getDescription());
        dest.writeDouble(getAmount());
        dest.writeString(getNotes());
        dest.writeSerializable(getDate());
        dest.writeLong(getCategoryID());
        dest.writeInt(isWithdrawal() ? 1 : 0);
    }
}
