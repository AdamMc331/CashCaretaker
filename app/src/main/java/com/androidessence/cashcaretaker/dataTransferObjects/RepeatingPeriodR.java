package com.androidessence.cashcaretaker.dataTransferObjects;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import com.androidessence.cashcaretaker.core.CoreDTO;
import com.androidessence.cashcaretaker.data.CCContract;

/**
 * Represents a repeating period for a transaction.
 *
 * Created by adam.mcneilly on 9/7/16.
 */
public class RepeatingPeriodR extends CoreDTO {
    private String name;

    public static final Creator<RepeatingPeriodR> CREATOR = new Creator<RepeatingPeriodR>() {
        @Override
        public RepeatingPeriodR createFromParcel(Parcel source) {
            return new RepeatingPeriodR(source);
        }

        @Override
        public RepeatingPeriodR[] newArray(int size) {
            return new RepeatingPeriodR[size];
        }
    };

    public RepeatingPeriodR(Cursor cursor) {
        setIdentifier(cursor.getLong(cursor.getColumnIndex(CCContract.RepeatingPeriodEntry._ID)));
        setName(cursor.getString(cursor.getColumnIndex(CCContract.RepeatingPeriodEntry.COLUMN_NAME)));
    }

    public RepeatingPeriodR(Parcel parcel) {
        super(parcel);
        setName(parcel.readString());
    }

    public RepeatingPeriodR() {
        setIdentifier(0);
        setName("");
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

    @Override
    public ContentValues getContentValues() {
        ContentValues values = new ContentValues();

        if(identifier > 0) {
            values.put(CCContract.RepeatingPeriodEntry._ID, identifier);
        }

        values.put(CCContract.RepeatingPeriodEntry.COLUMN_NAME, name);

        return values;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeString(getName());
    }
}
