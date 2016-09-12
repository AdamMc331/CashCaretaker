package com.androidessence.cashcaretaker.core;

import android.content.ContentValues;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Core class for all of our models.
 *
 * Created by adam.mcneilly on 9/5/16.
 */
public abstract class CoreDTO implements Parcelable {
    protected long identifier;

    protected CoreDTO() {

    }

    protected CoreDTO(Parcel parcel) {
        this.identifier = parcel.readLong();
    }

    public long getIdentifier() {
        return identifier;
    }

    public abstract ContentValues getContentValues();

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(identifier);
    }
}
