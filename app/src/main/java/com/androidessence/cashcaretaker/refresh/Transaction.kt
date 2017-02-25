package com.androidessence.cashcaretaker.refresh

import android.content.ContentValues
import android.database.Cursor
import android.os.Parcel
import com.androidessence.cashcaretaker.asBoolean
import com.androidessence.cashcaretaker.asInt
import com.androidessence.cashcaretaker.creator
import com.androidessence.cashcaretaker.data.CCContract
import com.androidessence.utility.Utility
import java.util.*

/**
 * Represents a transaction for a specific account.
 *
 * Created by adam.mcneilly on 2/25/17.
 */
open class Transaction: BaseModel {
    var account: Long = 0
    var description: String = ""
    var amount: Double = 0.toDouble()
    var notes: String = ""
    var date: Date? = null
    var categoryID: Long = 0
    var isWithdrawal: Boolean = false

    constructor(source: Parcel) : super(source) {
        account = source.readLong()
        description = source.readString()
        amount = source.readDouble()
        notes = source.readString()
        date = source.readSerializable() as Date
        categoryID = source.readLong()
        isWithdrawal = source.readInt().asBoolean()
    }

    constructor(cursor: Cursor) {
        id = cursor.getLong(cursor.getColumnIndex(CCContract.TransactionEntry._ID))
        account = cursor.getLong(cursor.getColumnIndex(CCContract.TransactionEntry.COLUMN_ACCOUNT))
        description = cursor.getString(cursor.getColumnIndex(CCContract.TransactionEntry.COLUMN_DESCRIPTION))
        amount = cursor.getDouble(cursor.getColumnIndex(CCContract.TransactionEntry.COLUMN_AMOUNT))
        notes = cursor.getString(cursor.getColumnIndex(CCContract.TransactionEntry.COLUMN_NOTES))
        val dateString = cursor.getString(cursor.getColumnIndex(CCContract.TransactionEntry.COLUMN_DATE))
        date = Utility.getDateFromDb(dateString)
        categoryID = cursor.getLong(cursor.getColumnIndex(CCContract.TransactionEntry.COLUMN_CATEGORY))
        val withdrawalInt = cursor.getInt(cursor.getColumnIndex(CCContract.TransactionEntry.COLUMN_WITHDRAWAL))
        isWithdrawal = withdrawalInt.asBoolean()
    }

    override fun getContentValues(): ContentValues {
        val values = super.getContentValues()

        values.put(CCContract.TransactionEntry.COLUMN_ACCOUNT, account)
        values.put(CCContract.TransactionEntry.COLUMN_DESCRIPTION, description)
        values.put(CCContract.TransactionEntry.COLUMN_AMOUNT, amount)
        values.put(CCContract.TransactionEntry.COLUMN_NOTES, notes)
        values.put(CCContract.TransactionEntry.COLUMN_DATE, Utility.getDBDateString(date))
        values.put(CCContract.TransactionEntry.COLUMN_CATEGORY, categoryID)
        values.put(CCContract.TransactionEntry.COLUMN_WITHDRAWAL, isWithdrawal.asInt())

        return values
    }

    override fun writeToParcel(dest: Parcel?, flags: Int) {
        super.writeToParcel(dest, flags)

        super.writeToParcel(dest, flags)
        dest?.writeLong(account)
        dest?.writeString(description)
        dest?.writeDouble(amount)
        dest?.writeString(notes)
        dest?.writeSerializable(date)
        dest?.writeLong(categoryID)
        dest?.writeInt(isWithdrawal.asInt())
    }

    companion object {
        @JvmField val CREATOR = creator(::Transaction)
    }
}