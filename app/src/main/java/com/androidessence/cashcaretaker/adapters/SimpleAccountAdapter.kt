package com.androidessence.cashcaretaker.adapters

import android.content.Context
import android.database.Cursor
import android.provider.BaseColumns
import android.support.v4.widget.CursorAdapter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

import com.androidessence.cashcaretaker.R
import com.androidessence.cashcaretaker.data.CCContract

/**
 * Adapter that displays basic account information.

 * Created by adam.mcneilly on 9/5/16.
 */
class SimpleAccountAdapter(context: Context) : CursorAdapter(context, null, 0) {

    override fun newView(context: Context, cursor: Cursor, parent: ViewGroup): View {
        val view = LayoutInflater.from(context).inflate(R.layout.list_item_textview, parent, false)
        view.tag = SimpleAccountViewHolder(view)
        return view
    }

    override fun bindView(view: View, context: Context, cursor: Cursor) {
        (view.tag as SimpleAccountViewHolder).bindCursor(cursor)
    }

    inner class SimpleAccountViewHolder(view: View) {
        val accountNameTextView: TextView = view.findViewById(R.id.list_item_text_view) as TextView

        fun bindCursor(cursor: Cursor) {
            accountNameTextView.text = cursor.getString(NAME_INDEX)
        }
    }

    companion object {
        val ACCOUNT_COLUMNS = arrayOf(CCContract.AccountEntry.TABLE_NAME + "." + BaseColumns._ID, CCContract.AccountEntry.COLUMN_NAME, CCContract.AccountEntry.COLUMN_BALANCE)

        val NAME_INDEX = 1
    }
}
