package com.androidessence.cashcaretaker

import android.content.Context
import android.graphics.Canvas
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.support.v7.widget.LinearLayoutManager.HORIZONTAL
import android.support.v7.widget.LinearLayoutManager.VERTICAL
import android.support.v7.widget.RecyclerView
import android.view.View

/**
 * Decoration between items in a RecyclerView.

 * Created by adam.mcneilly on 9/8/16.
 */
class DividerItemDecoration(context: Context, orientation: Int) : RecyclerView.ItemDecoration() {

    private val divider: Drawable
    private var orientation: Int = 0

    init {
        val a = context.obtainStyledAttributes(ATTRS)
        divider = a.getDrawable(0)
        a.recycle()
        setOrientation(orientation)
    }

    private fun setOrientation(orientation: Int) {
        when (orientation) {
            HORIZONTAL, VERTICAL -> this.orientation = orientation
            else -> throw IllegalArgumentException("Invalid orientation.")
        }
    }

    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State?) {
        when (orientation) {
            VERTICAL -> drawVertical(c, parent)
            HORIZONTAL -> drawHorizontal(c, parent)
            else -> {
            }
        }
    }

    private fun drawVertical(c: Canvas, parent: RecyclerView) {
        val left = parent.paddingLeft
        val right = parent.width - parent.paddingRight

        val childCount = parent.childCount
        for (i in 0..childCount - 1) {
            val child = parent.getChildAt(i)
            val params = child
                    .layoutParams as RecyclerView.LayoutParams
            val top = child.bottom + params.bottomMargin
            val bottom = top + divider.intrinsicHeight
            divider.setBounds(left, top, right, bottom)
            divider.draw(c)
        }
    }

    private fun drawHorizontal(c: Canvas, parent: RecyclerView) {
        val top = parent.paddingTop
        val bottom = parent.height - parent.paddingBottom

        val childCount = parent.childCount
        for (i in 0..childCount - 1) {
            val child = parent.getChildAt(i)
            val params = child
                    .layoutParams as RecyclerView.LayoutParams
            val left = child.right + params.rightMargin
            val right = left + divider.intrinsicHeight
            divider.setBounds(left, top, right, bottom)
            divider.draw(c)
        }
    }

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State?) {
        when (orientation) {
            VERTICAL -> outRect.set(0, 0, 0, divider.intrinsicHeight)
            HORIZONTAL -> outRect.set(0, 0, divider.intrinsicWidth, 0)
            else -> {
            }
        }
    }

    companion object {

        private val ATTRS = intArrayOf(android.R.attr.listDivider)
    }
}
