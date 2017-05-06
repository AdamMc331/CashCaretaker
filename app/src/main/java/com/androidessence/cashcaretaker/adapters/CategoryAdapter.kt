package com.androidessence.cashcaretaker.adapters

import android.content.Context
import android.database.Cursor
import android.support.v4.widget.CursorAdapter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

import com.androidessence.cashcaretaker.R
import com.androidessence.cashcaretaker.data.CCContract

/**
 * Adapter that displays a list of categories.

 * Created by adam.mcneilly on 9/5/16.
 */
class CategoryAdapter(context: Context) : CursorAdapter(context, null, 0) {

    override fun newView(context: Context, cursor: Cursor, parent: ViewGroup): View {
        // Create view
        val view = LayoutInflater.from(context).inflate(R.layout.list_item_textview, parent, false)

        // Set ViewHolder
        view.tag = CategoryViewHolder(view)

        return view
    }

    override fun bindView(view: View, context: Context, cursor: Cursor) {
        // Bind values
        (view.tag as CategoryViewHolder).bindCursor(cursor)
    }

    inner class CategoryViewHolder(view: View) {
        val categoryNameTextView: TextView = view.findViewById(R.id.list_item_text_view) as TextView

        // Set values.
        fun bindCursor(cursor: Cursor) {
            categoryNameTextView.text = cursor.getString(DESCRIPTION_INDEX)
        }
    }

    companion object {

        val CATEGORY_COLUMNS = arrayOf(CCContract.CategoryEntry.TABLE_NAME + "." + CCContract.CategoryEntry._ID, CCContract.CategoryEntry.COLUMN_DESCRIPTION, CCContract.CategoryEntry.COLUMN_IS_DEFAULT)

        private val DESCRIPTION_INDEX = 1
    }
}
