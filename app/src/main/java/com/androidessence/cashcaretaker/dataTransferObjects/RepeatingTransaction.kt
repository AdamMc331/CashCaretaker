package com.androidessence.cashcaretaker.dataTransferObjects

import android.content.ContentValues
import android.database.Cursor
import android.os.Parcel
import com.androidessence.cashcaretaker.asBoolean
import com.androidessence.cashcaretaker.asInt

import com.androidessence.cashcaretaker.core.CoreDTO
import com.androidessence.cashcaretaker.creator
import com.androidessence.cashcaretaker.data.CCContract
import com.androidessence.utility.Utility

import java.util.Date

/**
 * Represents a transaction that repeats monthly or yearly.

 * Created by adam.mcneilly on 9/7/16.
 */
class RepeatingTransaction : CoreDTO {
    var repeatingPeriod: Long = 0
        private set
    var account: Long = 0
        private set
    var description: String? = null
        private set
    var amount: Double = 0.toDouble()
        private set
    var notes: String? = null
        private set
    var nextDate: Date? = null
        private set
    var category: Long = 0
        private set
    var isWithdrawal: Boolean = false
        private set

    constructor(parcel: Parcel) : super(parcel) {
        this.repeatingPeriod = parcel.readLong()
        this.account = parcel.readLong()
        this.description = parcel.readString()
        this.amount = parcel.readDouble()
        this.notes = parcel.readString()
        this.nextDate = parcel.readSerializable() as Date
        this.category = parcel.readLong()
        this.isWithdrawal = parcel.readInt().asBoolean()
    }

    constructor(cursor: Cursor) {
        identifier = cursor.getLong(cursor.getColumnIndex(CCContract.RepeatingTransactionEntry._ID))
        repeatingPeriod = cursor.getLong(cursor.getColumnIndex(CCContract.RepeatingTransactionEntry.COLUMN_REPEATING_PERIOD))
        account = cursor.getLong(cursor.getColumnIndex(CCContract.RepeatingTransactionEntry.COLUMN_ACCOUNT))
        description = cursor.getString(cursor.getColumnIndex(CCContract.RepeatingTransactionEntry.COLUMN_DESCRIPTION))
        amount = cursor.getDouble(cursor.getColumnIndex(CCContract.RepeatingTransactionEntry.COLUMN_AMOUNT))
        notes = cursor.getString(cursor.getColumnIndex(CCContract.RepeatingTransactionEntry.COLUMN_NOTES))
        val nextDateString = cursor.getString(cursor.getColumnIndex(CCContract.RepeatingTransactionEntry.COLUMN_NEXT_DATE))
        nextDate = Utility.getDateFromDb(nextDateString)
        category = cursor.getLong(cursor.getColumnIndex(CCContract.RepeatingTransactionEntry.COLUMN_CATEGORY))
        val withdrawalInt = cursor.getInt(cursor.getColumnIndex(CCContract.RepeatingTransactionEntry.COLUMN_WITHDRAWAL))
        isWithdrawal = withdrawalInt.asBoolean()
    }

    constructor(repeatingPeriod: Long, account: Long, description: String, amount: Double, notes: String, nextDate: Date, category: Long, withdrawal: Boolean) {
        this.repeatingPeriod = repeatingPeriod
        this.account = account
        this.description = description
        this.amount = amount
        this.notes = notes
        this.nextDate = nextDate
        this.category = category
        this.isWithdrawal = withdrawal
    }

    override fun getContentValues(): ContentValues {
        val values = ContentValues()

        if (identifier > 0) {
            values.put(CCContract.RepeatingTransactionEntry._ID, getIdentifier())
        }

        values.put(CCContract.RepeatingTransactionEntry.COLUMN_REPEATING_PERIOD, repeatingPeriod)
        values.put(CCContract.RepeatingTransactionEntry.COLUMN_ACCOUNT, account)
        values.put(CCContract.RepeatingTransactionEntry.COLUMN_DESCRIPTION, description)
        values.put(CCContract.RepeatingTransactionEntry.COLUMN_AMOUNT, amount)
        values.put(CCContract.RepeatingTransactionEntry.COLUMN_NOTES, notes)
        values.put(CCContract.RepeatingTransactionEntry.COLUMN_NEXT_DATE, Utility.getDBDateString(nextDate))
        values.put(CCContract.RepeatingTransactionEntry.COLUMN_CATEGORY, category)
        values.put(CCContract.RepeatingTransactionEntry.COLUMN_WITHDRAWAL, if (isWithdrawal) 1 else 0)

        return values
    }

    val transactionContentValues: ContentValues
        get() {
            val values = ContentValues()

            values.put(CCContract.TransactionEntry.COLUMN_ACCOUNT, account)
            values.put(CCContract.TransactionEntry.COLUMN_DESCRIPTION, description)
            values.put(CCContract.TransactionEntry.COLUMN_AMOUNT, amount)
            values.put(CCContract.TransactionEntry.COLUMN_NOTES, notes)
            values.put(CCContract.TransactionEntry.COLUMN_DATE, Utility.getDBDateString(nextDate))
            values.put(CCContract.TransactionEntry.COLUMN_CATEGORY, category)
            values.put(CCContract.TransactionEntry.COLUMN_WITHDRAWAL, isWithdrawal.asInt())

            return values
        }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        super.writeToParcel(dest, flags)
        dest.writeLong(repeatingPeriod)
        dest.writeLong(account)
        dest.writeString(description)
        dest.writeDouble(amount)
        dest.writeString(notes)
        dest.writeSerializable(nextDate)
        dest.writeLong(category)
        dest.writeInt(isWithdrawal.asInt())
    }

    companion object {
        @JvmField val CREATOR = creator(::RepeatingTransaction)
    }
}
