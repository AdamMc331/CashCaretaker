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
 * Adapter for displaying repeating periods.

 * Created by adam.mcneilly on 9/5/16.
 */
class RepeatingPeriodAdapter(context: Context) : CursorAdapter(context, null, 0) {

    override fun newView(context: Context, cursor: Cursor, parent: ViewGroup): View {
        // Create view
        val view = LayoutInflater.from(context).inflate(R.layout.list_item_textview, parent, false)

        // Set ViewHolder
        view.tag = RepeatingPeriodViewHolder(view)

        return view
    }

    override fun bindView(view: View, context: Context, cursor: Cursor) {
        // Bind values
        (view.tag as RepeatingPeriodViewHolder).bindCursor(cursor)
    }

    inner class RepeatingPeriodViewHolder(view: View) {
        val repeatingPeriodNameTextView: TextView = view.findViewById(R.id.list_item_text_view) as TextView

        // Set values.
        fun bindCursor(cursor: Cursor) {
            repeatingPeriodNameTextView.text = cursor.getString(NAME_INDEX)
        }
    }

    companion object {
        val REPEATING_PERIOD_COLUMNS = arrayOf(CCContract.RepeatingPeriodEntry.TABLE_NAME + "." + CCContract.RepeatingPeriodEntry._ID, CCContract.RepeatingPeriodEntry.COLUMN_NAME)

        val NAME_INDEX = 1
    }
}
