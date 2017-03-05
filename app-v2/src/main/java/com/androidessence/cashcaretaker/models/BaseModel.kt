package com.androidessence.cashcaretaker.models

import android.content.ContentValues
import android.os.Parcel
import android.os.Parcelable
import com.androidessence.utility.creator

/**
 * Base model for all of our classes.
 *
 * Created by adam.mcneilly on 1/25/17.
 */
open class BaseModel(): Parcelable {

    constructor(source: Parcel): this()

    override fun writeToParcel(dest: Parcel?, flags: Int) { }

    override fun describeContents(): Int {
        return 0
    }

    companion object {
        @JvmField val CREATOR = creator(::BaseModel)
    }
}