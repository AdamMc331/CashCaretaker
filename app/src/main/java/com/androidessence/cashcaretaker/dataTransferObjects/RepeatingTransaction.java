package com.androidessence.cashcaretaker.dataTransferObjects;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Parcel;

import com.androidessence.cashcaretaker.core.CoreDTO;
import com.androidessence.cashcaretaker.data.CCContract;
import com.androidessence.utility.Utility;

import java.util.Date;

/**
 * Represents a transaction that repeats monthly or yearly.
 *
 * Created by adam.mcneilly on 9/7/16.
 */
public class RepeatingTransaction extends CoreDTO {
    private long repeatingPeriod;
    private long account;
    private String description;
    private double amount;
    private String notes;
    private Date nextDate;
    private long category;
    private boolean withdrawal;

    public static Creator<RepeatingTransaction> CREATOR = new Creator<RepeatingTransaction>() {
        @Override
        public RepeatingTransaction createFromParcel(Parcel source) {
            return new RepeatingTransaction(source);
        }

        @Override
        public RepeatingTransaction[] newArray(int size) {
            return new RepeatingTransaction[size];
        }
    };

    public RepeatingTransaction(Parcel parcel) {
        super(parcel);
        this.repeatingPeriod = parcel.readLong();
        this.account = parcel.readLong();
        this.description = parcel.readString();
        this.amount = parcel.readDouble();
        this.notes = parcel.readString();
        this.nextDate = (Date) parcel.readSerializable();
        this.category = parcel.readLong();
        this.withdrawal = parcel.readInt() == 1;
    }

    public RepeatingTransaction(Cursor cursor) {
        setIdentifier(cursor.getLong(cursor.getColumnIndex(CCContract.RepeatingTransactionEntry._ID)));
        setRepeatingPeriod(cursor.getLong(cursor.getColumnIndex(CCContract.RepeatingTransactionEntry.COLUMN_REPEATING_PERIOD)));
        setAccount(cursor.getLong(cursor.getColumnIndex(CCContract.RepeatingTransactionEntry.COLUMN_ACCOUNT)));
        setDescription(cursor.getString(cursor.getColumnIndex(CCContract.RepeatingTransactionEntry.COLUMN_DESCRIPTION)));
        setAmount(cursor.getDouble(cursor.getColumnIndex(CCContract.RepeatingTransactionEntry.COLUMN_AMOUNT)));
        setNotes(cursor.getString(cursor.getColumnIndex(CCContract.RepeatingTransactionEntry.COLUMN_NOTES)));
        String nextDateString = cursor.getString(cursor.getColumnIndex(CCContract.RepeatingTransactionEntry.COLUMN_NEXT_DATE));
        setNextDate(Utility.getDateFromDb(nextDateString));
        setCategory(cursor.getLong(cursor.getColumnIndex(CCContract.RepeatingTransactionEntry.COLUMN_CATEGORY)));
        int withdrawalInt = cursor.getInt(cursor.getColumnIndex(CCContract.RepeatingTransactionEntry.COLUMN_WITHDRAWAL));
        setWithdrawal(withdrawalInt == 1);
    }

    public RepeatingTransaction(long repeatingPeriod, long account, String description, double amount, String notes, Date nextDate, long category, boolean withdrawal) {
        setRepeatingPeriod(repeatingPeriod);
        setAccount(account);
        setDescription(description);
        setAmount(amount);
        setNotes(notes);
        setNextDate(nextDate);
        setCategory(category);
        setWithdrawal(withdrawal);
    }

    public long getIdentifier() {
        return identifier;
    }

    public void setIdentifier(long identifier) {
        this.identifier = identifier;
    }

    public long getRepeatingPeriod() {
        return repeatingPeriod;
    }

    public void setRepeatingPeriod(long repeatingPeriod) {
        this.repeatingPeriod = repeatingPeriod;
    }

    public long getAccount() {
        return account;
    }

    public void setAccount(long account) {
        this.account = account;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Date getNextDate() {
        return nextDate;
    }

    public void setNextDate(Date nextDate) {
        this.nextDate = nextDate;
    }

    public long getCategory() {
        return category;
    }

    public void setCategory(long category) {
        this.category = category;
    }

    public boolean isWithdrawal() {
        return withdrawal;
    }

    public void setWithdrawal(boolean withdrawal) {
        this.withdrawal = withdrawal;
    }

    @Override
    public ContentValues getContentValues() {
        ContentValues values = new ContentValues();

        if(identifier > 0) {
            values.put(CCContract.RepeatingTransactionEntry._ID, getIdentifier());
        }

        values.put(CCContract.RepeatingTransactionEntry.COLUMN_REPEATING_PERIOD, getRepeatingPeriod());
        values.put(CCContract.RepeatingTransactionEntry.COLUMN_ACCOUNT, getAccount());
        values.put(CCContract.RepeatingTransactionEntry.COLUMN_DESCRIPTION, getDescription());
        values.put(CCContract.RepeatingTransactionEntry.COLUMN_AMOUNT, getAmount());
        values.put(CCContract.RepeatingTransactionEntry.COLUMN_NOTES, getNotes());
        values.put(CCContract.RepeatingTransactionEntry.COLUMN_NEXT_DATE, Utility.getDBDateString(getNextDate()));
        values.put(CCContract.RepeatingTransactionEntry.COLUMN_CATEGORY, getCategory());
        values.put(CCContract.RepeatingTransactionEntry.COLUMN_WITHDRAWAL, isWithdrawal() ? 1 : 0);

        return values;
    }

    public ContentValues getTransactionContentValues() {
        ContentValues values = new ContentValues();

        values.put(CCContract.TransactionEntry.COLUMN_ACCOUNT, getAccount());
        values.put(CCContract.TransactionEntry.COLUMN_DESCRIPTION, getDescription());
        values.put(CCContract.TransactionEntry.COLUMN_AMOUNT, getAmount());
        values.put(CCContract.TransactionEntry.COLUMN_NOTES, getNotes());
        values.put(CCContract.TransactionEntry.COLUMN_DATE, Utility.getDBDateString(getNextDate()));
        values.put(CCContract.TransactionEntry.COLUMN_CATEGORY, getCategory());
        values.put(CCContract.TransactionEntry.COLUMN_WITHDRAWAL, isWithdrawal() ? 1 : 0);

        return values;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeLong(repeatingPeriod);
        dest.writeLong(account);
        dest.writeString(description);
        dest.writeDouble(amount);
        dest.writeString(notes);
        dest.writeSerializable(nextDate);
        dest.writeLong(category);
        dest.writeInt(withdrawal ? 1 : 0);
    }
}
