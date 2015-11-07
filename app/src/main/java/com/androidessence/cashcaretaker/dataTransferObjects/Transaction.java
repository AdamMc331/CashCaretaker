package com.androidessence.cashcaretaker.dataTransferObjects;

import android.content.ContentValues;
import android.database.Cursor;

import com.androidessence.cashcaretaker.Utility;
import com.androidessence.cashcaretaker.data.CCContract;

import org.joda.time.LocalDate;

/**
 * Class representing a Transaction for an account.
 *
 * Created by adammcneilly on 11/3/15.
 */
public class Transaction {
    private long identifier;
    private long account;
    private String description;
    private double amount;
    private String notes;
    private LocalDate date;
    private long category;
    private boolean withdrawal;

    public Transaction(Cursor cursor){
        setIdentifier(cursor.getLong(cursor.getColumnIndex(CCContract.TransactionEntry._ID)));
        setAccount(cursor.getLong(cursor.getColumnIndex(CCContract.TransactionEntry.COLUMN_ACCOUNT)));
        setDescription(cursor.getString(cursor.getColumnIndex(CCContract.TransactionEntry.COLUMN_DESCRIPTION)));
        setAmount(cursor.getDouble(cursor.getColumnIndex(CCContract.TransactionEntry.COLUMN_AMOUNT)));
        setNotes(cursor.getString(cursor.getColumnIndex(CCContract.TransactionEntry.COLUMN_NOTES)));
        String dateString = cursor.getString(cursor.getColumnIndex(CCContract.TransactionEntry.COLUMN_DATE));
        setDate(Utility.getDateFromDb(dateString));
        setCategory(cursor.getLong(cursor.getColumnIndex(CCContract.TransactionEntry.COLUMN_CATEGORY)));
        int withdrawalInt = cursor.getInt(cursor.getColumnIndex(CCContract.TransactionEntry.COLUMN_WITHDRAWAL));
        setWithdrawal(withdrawalInt == 1);
    }

    public Transaction(long account, String description, double amount, String notes, LocalDate date, long category, boolean withdrawal) {
        setAccount(account);
        setDescription(description);
        setAmount(amount);
        setNotes(notes);
        setDate(date);
        setCategory(category);
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

    private double getAmount() {
        return amount;
    }

    private void setAmount(double amount) {
        this.amount = amount;
    }

    private String getNotes() {
        return notes;
    }

    private void setNotes(String notes) {
        this.notes = notes;
    }

    private LocalDate getDate() {
        return date;
    }

    private void setDate(LocalDate date) {
        this.date = date;
    }

    private long getCategory() {
        return category;
    }

    private void setCategory(long category) {
        this.category = category;
    }

    private boolean isWithdrawal() {
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
        values.put(CCContract.TransactionEntry.COLUMN_CATEGORY, getCategory());
        values.put(CCContract.TransactionEntry.COLUMN_WITHDRAWAL, isWithdrawal() ? 1 : 0);

        return values;
    }
}
