package com.androidessence.cashcaretaker

import android.databinding.BindingAdapter
import android.support.annotation.ColorRes
import android.support.v4.content.ContextCompat
import android.view.View
import android.widget.TextView

/**
 * DataBinding adapter that applies a color resource to the text color of a TextView.
 */
@BindingAdapter("textColorResource")
fun setTextColorResource(view: TextView?, @ColorRes colorRes: Int) {
    val context = view?.context

    if (context != null) {
        val color = ContextCompat.getColor(context, colorRes)
        view.setTextColor(color)
    }
}

/**
 * DataBinding adapter that applies a color resource to the background of a View.
 */
@BindingAdapter("backgroundColorResource")
fun setBackgroundColor(view: View?, @ColorRes colorRes: Int) {
    val context = view?.context

    if (context != null) {
        val color = ContextCompat.getColor(context, colorRes)
        view.setBackgroundColor(color)
    }
}