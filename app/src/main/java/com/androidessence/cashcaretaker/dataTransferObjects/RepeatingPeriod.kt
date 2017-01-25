package com.androidessence.cashcaretaker.dataTransferObjects

import android.content.ContentValues
import android.database.Cursor
import android.os.Parcel

import com.androidessence.cashcaretaker.core.CoreDTO
import com.androidessence.cashcaretaker.creator
import com.androidessence.cashcaretaker.data.CCContract

/**
 * Represents a repeating period for a transaction.

 * Created by adam.mcneilly on 9/7/16.
 */
class RepeatingPeriod : CoreDTO {
    var name: String? = null
        private set

    constructor(cursor: Cursor) {
        identifier = cursor.getLong(cursor.getColumnIndex(CCContract.RepeatingPeriodEntry._ID))
        name = cursor.getString(cursor.getColumnIndex(CCContract.RepeatingPeriodEntry.COLUMN_NAME))
    }

    constructor(parcel: Parcel) : super(parcel) {
        name = parcel.readString()
    }

    constructor() {
        identifier = 0
        name = ""
    }

    override fun getContentValues(): ContentValues {
        val values = ContentValues()

        if (identifier > 0) {
            values.put(CCContract.RepeatingPeriodEntry._ID, identifier)
        }

        values.put(CCContract.RepeatingPeriodEntry.COLUMN_NAME, name)

        return values
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        super.writeToParcel(dest, flags)
        dest.writeString(name)
    }

    companion object {
        @JvmField val CREATOR = creator(::RepeatingPeriod)
    }
}
