package com.androidessence.cashcaretaker.core

import android.content.ContentValues
import android.os.Parcel
import android.os.Parcelable
import android.provider.BaseColumns

/**
 * Core class for all of our models.

 * Created by adam.mcneilly on 9/5/16.
 */
abstract class CoreDTO : Parcelable {
    var identifier: Long = 0
        protected set

    protected constructor()

    protected constructor(parcel: Parcel) {
        this.identifier = parcel.readLong()
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeLong(identifier)
    }

    open fun getContentValues(): ContentValues {
        val values = ContentValues()

        if (identifier > 0) {
            values.put(BaseColumns._ID, identifier)
        }

        return values
    }
}
