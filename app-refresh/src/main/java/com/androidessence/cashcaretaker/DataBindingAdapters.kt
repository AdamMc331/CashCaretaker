package com.androidessence.cashcaretaker

import android.databinding.BindingAdapter
import android.support.annotation.ColorRes
import android.support.v4.content.ContextCompat
import android.view.View
import android.widget.TextView

@BindingAdapter("textColorResource")
fun setTextColorResource(view: TextView?, @ColorRes colorRes: Int) {
    val context = view?.context

    if (context != null) {
        val color = ContextCompat.getColor(context, colorRes)
        view.setTextColor(color)
    }
}

@BindingAdapter("backgroundColorResource")
fun setBackgroundColor(view: View?, @ColorRes colorRes: Int) {
    val context = view?.context

    if (context != null) {
        val color = ContextCompat.getColor(context, colorRes)
        view.setBackgroundColor(color)
    }
}