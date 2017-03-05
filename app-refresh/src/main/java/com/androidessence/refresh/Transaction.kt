package com.androidessence.refresh

import android.os.Parcel
import com.androidessence.refresh.asBoolean
import com.androidessence.refresh.asInt
import com.androidessence.refresh.creator
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

    constructor(): super()

    constructor(source: Parcel) : super(source) {
        account = source.readLong()
        description = source.readString()
        amount = source.readDouble()
        notes = source.readString()
        date = source.readSerializable() as Date
        categoryID = source.readLong()
        isWithdrawal = source.readInt().asBoolean()
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