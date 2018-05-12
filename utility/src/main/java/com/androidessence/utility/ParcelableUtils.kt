package com.androidessence.utility

import android.os.Parcel
import android.os.Parcelable

/**
 * Utility methods for parcelables.
 */

fun Int.asBoolean(): Boolean = (this != 0)

fun Boolean.asInt(): Int = if (this) 1 else 0