package com.androidessence.cashcaretaker.dataTransferObjects;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Parcel;

import com.androidessence.cashcaretaker.core.CoreDTO;
import com.androidessence.cashcaretaker.data.CCContract;

/**
 * Represents a repeating period for a transaction.
 *
 * Created by adam.mcneilly on 9/7/16.
 */
public class RepeatingPeriod extends CoreDTO {
    private String name;

    public static final Creator<RepeatingPeriod> CREATOR = new Creator<RepeatingPeriod>() {
        @Override
        public RepeatingPeriod createFromParcel(Parcel source) {
            return new RepeatingPeriod(source);
        }

        @Override
        public RepeatingPeriod[] newArray(int size) {
            return new RepeatingPeriod[size];
        }
    };

    public RepeatingPeriod(Cursor cursor) {
        setIdentifier(cursor.getLong(cursor.getColumnIndex(CCContract.RepeatingPeriodEntry._ID)));
        setName(cursor.getString(cursor.getColumnIndex(CCContract.RepeatingPeriodEntry.COLUMN_NAME)));
    }

    public RepeatingPeriod(Parcel parcel) {
        super(parcel);
        setName(parcel.readString());
    }

    public RepeatingPeriod() {
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
