package com.androidessence.cashcaretaker.ui.utils

import android.view.View

fun View.visibleIf(condition: Boolean) {
    this.visibility = if (condition) {
        View.VISIBLE
    } else {
        View.GONE
    }
}
