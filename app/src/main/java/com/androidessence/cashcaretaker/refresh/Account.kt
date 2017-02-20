package com.androidessence.cashcaretaker.refresh

import android.content.ContentValues
import android.database.Cursor
import android.os.Parcel
import com.androidessence.cashcaretaker.creator
import com.androidessence.cashcaretaker.data.CCContract

/**
 * Represents a bank account.
 *
 * Created by adam.mcneilly on 1/25/17.
 */
open class Account: BaseModel {
    var name = ""
    var balance: Double = 0.toDouble()

    constructor(): super()

    constructor(cursor: Cursor) {
        name = cursor.getString(cursor.getColumnIndex(CCContract.AccountEntry.COLUMN_NAME))
        balance = cursor.getDouble(cursor.getColumnIndex(CCContract.AccountEntry.COLUMN_BALANCE))
    }

    constructor(source: Parcel): super(source) {
        name = source.readString()
        balance = source.readDouble()
    }

    override fun writeToParcel(dest: Parcel?, flags: Int) {
        super.writeToParcel(dest, flags)

        dest?.writeString(name)
        dest?.writeDouble(balance)
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