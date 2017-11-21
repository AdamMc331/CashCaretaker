package com.androidessence.utility

import android.os.Parcel
import android.os.Parcelable

/**
 * Utility methods for parcelables.
 */
inline fun <reified T : Parcelable> creator(crossinline create: (Parcel) -> T) = object : Parcelable.Creator<T> {
    override fun createFromParcel(source: Parcel) = create(source)
    override fun newArray(size: Int) = arrayOfNulls<T?>(size)
}

fun Int.asBoolean(): Boolean = (this != 0)

fun Boolean.asInt(): Int = if (this) 1 else 0