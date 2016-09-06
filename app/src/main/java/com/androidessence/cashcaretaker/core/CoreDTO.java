package com.androidessence.cashcaretaker.core;

import android.content.ContentValues;
import android.os.Parcelable;

/**
 * Core class for all of our models.
 *
 * Created by adam.mcneilly on 9/5/16.
 */
public abstract class CoreDTO implements Parcelable {

    public abstract ContentValues getContentValues();

    @Override
    public int describeContents() {
        return 0;
    }
}
