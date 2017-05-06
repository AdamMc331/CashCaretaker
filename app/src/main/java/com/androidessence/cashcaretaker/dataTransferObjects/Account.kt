package com.androidessence.cashcaretaker.dataTransferObjects

import android.content.ContentValues
import android.database.Cursor
import android.os.Parcel

import com.androidessence.cashcaretaker.core.CoreDTO
import com.androidessence.cashcaretaker.creator
import com.androidessence.cashcaretaker.data.CCContract

/**
 * Represents a finance account for the user.

 * Created by adam.mcneilly on 9/5/16.
 */
class Account : CoreDTO {

    var name: String? = null
        private set
    var balance: Double = 0.toDouble()
        private set

    constructor(name: String, balance: Double) {
        this.name = name
        this.balance = balance
    }

    constructor(cursor: Cursor) {
        this.identifier = cursor.getLong(cursor.getColumnIndex(CCContract.AccountEntry._ID))
        this.name = cursor.getString(cursor.getColumnIndex(CCContract.AccountEntry.COLUMN_NAME))
        this.balance = cursor.getDouble(cursor.getColumnIndex(CCContract.AccountEntry.COLUMN_BALANCE))
    }

    constructor(parcel: Parcel) : super(parcel) {
        this.name = parcel.readString()
        this.balance = parcel.readDouble()
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        super.writeToParcel(dest, flags)
        dest.writeString(name)
        dest.writeDouble(balance)
    }

    override fun getContentValues(): ContentValues {
        val values = super.getContentValues()

        values.put(CCContract.AccountEntry.COLUMN_NAME, name)
        values.put(CCContract.AccountEntry.COLUMN_BALANCE, balance)

        return values
    }

    companion object {
        @JvmField val CREATOR = creator(::Account)
    }
}
