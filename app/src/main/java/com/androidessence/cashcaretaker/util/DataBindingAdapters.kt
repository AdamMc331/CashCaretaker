package com.androidessence.cashcaretaker.util

import android.content.Context
import android.util.TypedValue
import android.view.View
import android.widget.TextView
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter

/**
 * DataBinding adapter that applies a color resource to the text color of a TextView.
 */
@BindingAdapter("textColorResource")
fun setTextColorResource(view: TextView?, @ColorRes colorRes: Int?) {
    val context = view?.context

    if (context != null) {
        val color = if (colorRes != null) {
            context.colorFromRes(colorRes)
        } else {
            context.primaryTextColor()
        }

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

/**
 * Sets a view to visible or gone based on a condition.
 */
@BindingAdapter("visibilityCondition")
fun setVisibilityCondition(view: View?, condition: Boolean) {
    view?.visibility = if (condition) View.VISIBLE else View.GONE
}

fun Context.colorFromRes(colorRes: Int): Int {
    return ContextCompat.getColor(this, colorRes)
}

fun Context.primaryTextColor(): Int {
    val resolvedAttr = TypedValue()
    this.theme?.resolveAttribute(android.R.attr.textColorPrimary, resolvedAttr, true)
    val primaryTextColorRes = resolvedAttr.run { if (resourceId != 0) resourceId else data }
    return colorFromRes(primaryTextColorRes)
}
