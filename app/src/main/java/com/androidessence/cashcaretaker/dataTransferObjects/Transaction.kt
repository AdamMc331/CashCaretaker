package com.androidessence.cashcaretaker.dataTransferObjects

import android.content.ContentValues
import android.database.Cursor
import android.os.Parcel
import android.os.Parcelable
import com.androidessence.cashcaretaker.asBoolean
import com.androidessence.cashcaretaker.asInt

import com.androidessence.cashcaretaker.core.CoreDTO
import com.androidessence.cashcaretaker.creator
import com.androidessence.cashcaretaker.data.CCContract
import com.androidessence.utility.Utility

import java.util.Date

/**
 * Represents a Transaction entry.

 * Created by adam.mcneilly on 9/7/16.
 */
open class Transaction : CoreDTO {

    private var account: Long = 0

    var description: String? = null
        private set
    var amount: Double = 0.toDouble()
        private set
    var notes: String? = null
        private set
    var date: Date? = null
        private set
    var categoryID: Long = 0
        private set
    var isWithdrawal: Boolean = false
        private set

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
        identifier = cursor.getLong(cursor.getColumnIndex(CCContract.TransactionEntry._ID))
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

    constructor(account: Long, description: String, amount: Double, notes: String, date: Date, categoryID: Long, withdrawal: Boolean) {
        this.account = account
        this.description = description
        this.amount = amount
        this.notes = notes
        this.date = date
        this.categoryID = categoryID
        this.isWithdrawal = withdrawal
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

    override fun writeToParcel(dest: Parcel, flags: Int) {
        super.writeToParcel(dest, flags)
        dest.writeLong(account)
        dest.writeString(description)
        dest.writeDouble(amount)
        dest.writeString(notes)
        dest.writeSerializable(date)
        dest.writeLong(categoryID)
        dest.writeInt(isWithdrawal.asInt())
    }

    companion object {
        @JvmField val CREATOR = creator(::Transaction)
    }
}