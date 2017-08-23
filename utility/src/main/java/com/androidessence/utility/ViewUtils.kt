package com.androidessence.utility

import android.view.View

/**
 * Utility methods for views.
 */
fun View.show() {
    this.visibility = View.VISIBLE
}

fun View.hide() {
    this.visibility = View.GONE
}