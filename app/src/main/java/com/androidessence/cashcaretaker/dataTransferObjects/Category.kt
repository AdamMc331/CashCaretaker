package com.androidessence.cashcaretaker.dataTransferObjects

import android.content.ContentValues
import android.database.Cursor
import android.os.Parcel
import android.provider.BaseColumns
import com.androidessence.cashcaretaker.asBoolean
import com.androidessence.cashcaretaker.asInt

import com.androidessence.cashcaretaker.core.CoreDTO
import com.androidessence.cashcaretaker.creator
import com.androidessence.cashcaretaker.data.CCContract

/**
 * Represents a category for a transaction.

 * Created by adam.mcneilly on 9/5/16.
 */
class Category : CoreDTO {
    var description: String? = null
        private set
    var isDefault: Boolean = false
        private set

    constructor(identifier: Long, description: String) {
        this.identifier = identifier
        this.description = description
    }

    constructor(cursor: Cursor) {
        this.identifier = cursor.getLong(cursor.getColumnIndex(BaseColumns._ID))
        this.description = cursor.getString(cursor.getColumnIndex(CCContract.CategoryEntry.COLUMN_DESCRIPTION))
        val defaultInt = cursor.getInt(cursor.getColumnIndex(CCContract.CategoryEntry.COLUMN_IS_DEFAULT))
        this.isDefault = defaultInt.asBoolean()
    }

    private constructor(parcel: Parcel) : super(parcel) {
        this.description = parcel.readString()
        this.isDefault = parcel.readInt().asBoolean()
    }

    constructor() {
        this.identifier = 0
        this.description = ""
        this.isDefault = false
    }

    constructor(description: String) {
        this.identifier = 0
        this.description = description
        this.isDefault = false
    }

    override fun getContentValues(): ContentValues {
        val values = super.getContentValues()

        values.put(CCContract.CategoryEntry.COLUMN_DESCRIPTION, description)
        values.put(CCContract.CategoryEntry.COLUMN_IS_DEFAULT, isDefault.asInt())

        return values
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        super.writeToParcel(dest, flags)
        dest.writeString(description)
        dest.writeInt(isDefault.asInt())
    }

    companion object {
        @JvmField val CREATOR = creator(::Category)
    }
}
