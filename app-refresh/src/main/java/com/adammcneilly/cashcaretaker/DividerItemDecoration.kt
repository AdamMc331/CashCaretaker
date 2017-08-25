package com.adammcneilly.cashcaretaker

import android.content.Context
import android.graphics.Canvas
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View

/**
 * DividerItemDecoration to show between RecyclerView items.
 */
class DividerItemDecoration @JvmOverloads constructor(context: Context, orientation: Int = DividerItemDecoration.VERTICAL_LIST) : RecyclerView.ItemDecoration() {
    /**
     * The orientation of the list.
     */
    private var orientation: Int = 0

    /**
     * The actual divider that is displayed.
     */
    private val divider: Drawable

    init {
        val a = context.obtainStyledAttributes(ATTRS)
        divider = a.getDrawable(DIVIDER_POSITION)
        a.recycle()
        setOrientation(orientation)
    }

    /**
     * Sets the orientation of this item decoration. An exception is thrown if it is not one of
     * the two given orientations.
     */
    private fun setOrientation(orientation: Int) {
        if (orientation != HORIZONTAL_LIST && orientation != VERTICAL_LIST) {
            throw IllegalArgumentException(INVALID_ORIENTATION)
        }

        this.orientation = orientation
    }

    /**
     * Draws the divider depending on the orientation of the RecyclerView.
     */
    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State?) {
        when (orientation) {
            VERTICAL_LIST -> drawVertical(c, parent)
            HORIZONTAL_LIST -> drawHorizontal(c, parent)
        }
    }

    /**
     * Draws a divider for a vertical RecyclerView list.
     */
    private fun drawVertical(c: Canvas, parent: RecyclerView) {
        val left = parent.paddingLeft
        val right = parent.width - parent.paddingRight

        val childCount = parent.childCount
        for (i in 0 until childCount) {
            val child = parent.getChildAt(i)
            val params = child
                    .layoutParams as RecyclerView.LayoutParams
            val top = child.bottom + params.bottomMargin
            val bottom = top + divider.intrinsicHeight
            divider.setBounds(left, top, right, bottom)
            divider.draw(c)
        }
    }

    /**
     * Draws a divider for a horizontal RecyclerView list.
     */
    private fun drawHorizontal(c: Canvas, parent: RecyclerView) {
        val top = parent.paddingTop
        val bottom = parent.height - parent.paddingBottom

        val childCount = parent.childCount
        for (i in 0 until childCount) {
            val child = parent.getChildAt(i)
            val params = child
                    .layoutParams as RecyclerView.LayoutParams
            val left = child.right + params.rightMargin
            val right = left + divider.intrinsicHeight
            divider.setBounds(left, top, right, bottom)
            divider.draw(c)
        }
    }

    /**
     * Determines the offset of the divider based on the orientation of the list.
     */
    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State?) {
        when (orientation) {
            VERTICAL_LIST -> outRect.set(0, 0, 0, divider.intrinsicHeight)
            HORIZONTAL_LIST -> outRect.set(0, 0, divider.intrinsicWidth, 0)
        }
    }

    companion object {
        // Attributes for the divider
        private val ATTRS = intArrayOf(android.R.attr.listDivider)
        private val DIVIDER_POSITION = 0

        // List orientations
        val HORIZONTAL_LIST = LinearLayoutManager.HORIZONTAL
        val VERTICAL_LIST = LinearLayoutManager.VERTICAL

        // Error message
        val INVALID_ORIENTATION = "Please supply a valid CoreDividerItemDecoration orientation."
    }
}