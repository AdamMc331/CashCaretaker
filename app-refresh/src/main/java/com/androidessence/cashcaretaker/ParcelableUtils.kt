package com.androidessence.cashcaretaker

import android.os.Parcel
import android.os.Parcelable

/**
 * Utility methods for parcelables.
 *
 * Created by adam.mcneilly on 1/25/17.
 */
inline fun <reified T : Parcelable> creator(crossinline create: (Parcel) -> T) = object : Parcelable.Creator<T> {
    override fun createFromParcel(source: Parcel) = create(source)
    override fun newArray(size: Int) = arrayOfNulls<T?>(size)
}

fun Int.asBoolean(): Boolean {
    return (this != 0)
}

fun Boolean.asInt(): Int {
    return if (this) 1 else 0
}