package com.androidessence.cashcaretaker

import android.os.Parcel
import com.androidessence.cashcaretaker.creator

/**
 * Represents a bank account.
 *
 * Created by adam.mcneilly on 1/25/17.
 */
open class Account: BaseModel {
    var name = ""
    var balance: Double = 0.toDouble()

    constructor(): super()

    constructor(source: Parcel): super(source) {
        name = source.readString()
        balance = source.readDouble()
    }

    override fun writeToParcel(dest: Parcel?, flags: Int) {
        super.writeToParcel(dest, flags)

        dest?.writeString(name)
        dest?.writeDouble(balance)
    }

    companion object {
        @JvmField val CREATOR = creator(::Account)
    }
}