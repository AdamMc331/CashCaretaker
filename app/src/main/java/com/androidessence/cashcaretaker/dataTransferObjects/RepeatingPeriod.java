package com.androidessence.cashcaretaker.dataTransferObjects;

import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import com.androidessence.cashcaretaker.data.CCContract;

/**
 * Created by adammcneilly on 11/16/15.
 */
public class RepeatingPeriod implements Parcelable {
    private long identifier;
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
        setIdentifier(parcel.readLong());
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
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(getIdentifier());
        dest.writeString(getName());
    }
}
