package com.androidessence.cashcaretaker.dataTransferObjects

import android.database.Cursor
import android.os.Parcel
import android.os.Parcelable
import com.androidessence.cashcaretaker.creator

import com.androidessence.cashcaretaker.data.CCContract

/**
 * Details about a transaction beyond the required info.

 * Created by adam.mcneilly on 9/7/16.
 */
class TransactionDetails : Transaction {
    var category: Category? = null

    constructor(cursor: Cursor) : super(cursor) {
        // Get category name
        val categoryName = cursor.getString(cursor.getColumnIndex(CCContract.CategoryEntry.COLUMN_DESCRIPTION))
        category = Category(categoryID, categoryName)
    }

    constructor(source: Parcel) : super(source) {
        category = source.readParcelable<Parcelable>(Category::class.java.classLoader) as Category
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        super.writeToParcel(dest, flags)
        dest.writeParcelable(category, flags)
    }

    companion object {
        @JvmField val CREATOR = creator(::TransactionDetails)
    }
}
