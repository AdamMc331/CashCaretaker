package com.androidessence.cashcaretaker.views

import android.content.Context
import android.graphics.Canvas
import android.support.v7.app.AlertDialog
import android.support.v7.widget.AppCompatEditText
import android.util.AttributeSet

@Suppress("MemberVisibilityCanPrivate")
class SpinnerInputEditText<T> : AppCompatEditText {

    var items: List<T> = ArrayList()

    private val displayItems: Array<String>
        get() = items.map { it.toString() }.toTypedArray()

    var selectedItem: T? = null
        private set

    private var mHint: CharSequence

    constructor(context: Context) : super(context) {
        mHint = hint
        configureOnClickListener()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        mHint = hint
        configureOnClickListener()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        mHint = hint
        configureOnClickListener()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        isFocusable = false
        isClickable = true
    }

    private fun configureOnClickListener() {
        setOnClickListener { view ->
            val builder = AlertDialog.Builder(view.context)
            builder.setTitle(mHint)
            builder.setItems(displayItems) { _, selectedIndex ->
                setText(displayItems[selectedIndex])

                selectedItem = items[selectedIndex]
            }
            builder.setPositiveButton("Close", null)
            builder.create().show()
        }
    }
}