package com.androidessence.cashcaretaker.refresh

import android.content.ContentValues
import android.os.Parcel
import android.os.Parcelable
import android.provider.BaseColumns
import com.androidessence.cashcaretaker.creator

/**
 * Base model for all of our classes.
 *
 * Created by adam.mcneilly on 1/25/17.
 */
open class BaseModel(): Parcelable {

    var id: Long = 0

    constructor(source: Parcel): this() {
        id = source.readLong()
    }

    override fun writeToParcel(dest: Parcel?, flags: Int) {
        dest?.writeLong(id)
    }

    override fun describeContents(): Int {
        return 0
    }

    /**
     * Returns the ContentValues for this base model to be saved.
     *
     * Did not make an abstract method, because then we wouldn't be able to use the CREATOR extension.
     */
    open fun getContentValues(): ContentValues {
        val values = ContentValues()

        if (id > 0) {
            values.put(BaseColumns._ID, id)
        }

        return values
    }

    companion object {
        @JvmField val CREATOR = creator(::BaseModel)
    }
}