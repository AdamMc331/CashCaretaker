package com.androidessence.refresh

import android.content.ContentValues
import android.os.Parcel
import android.os.Parcelable
import android.provider.BaseColumns
import com.androidessence.refresh.creator

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

    companion object {
        @JvmField val CREATOR = creator(::BaseModel)
    }
}