package com.androidessence.cashcaretaker.core

import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment

/**
 * Displays an error to the user inside the fragment.
 *
 * @param[error] The error that occurred.
 * @param[length] The length of time the error should be visible.
 */
fun Fragment.showError(error: Throwable?, length: Int = Snackbar.LENGTH_SHORT) {
    view?.let {
        Snackbar.make(it, error?.message.toString(), length).show()
    }
}