package com.androidessence.cashcaretaker.adapters

import android.content.Context
import android.database.Cursor
import android.provider.BaseColumns
import android.support.v4.content.ContextCompat
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

import com.androidessence.cashcaretaker.R
import com.androidessence.cashcaretaker.data.CCContract
import com.androidessence.cashcaretaker.dataTransferObjects.TransactionDetails
import com.androidessence.recyclerviewcursoradapter.RecyclerViewCursorAdapter
import com.androidessence.recyclerviewcursoradapter.RecyclerViewCursorViewHolder
import com.androidessence.utility.*
import com.androidessence.utility.Utility

/**
 * Adapter for displaying a list of transactions.

 * Created by adam.mcneilly on 9/5/16.
 */
class TransactionAdapter(context: Context) : RecyclerViewCursorAdapter<TransactionAdapter.TransactionViewHolder>(context) {

    // Colors used inside the ViewHolder.
    private val red: Int = ContextCompat.getColor(mContext, R.color.mds_red_500)
    private val green: Int = ContextCompat.getColor(mContext, R.color.mds_green_500)
    private val primaryTextColor: Int = ContextCompat.getColor(mContext, android.R.color.primary_text_light)

    init {
        setupCursorAdapter(null, 0, R.layout.list_item_transaction, false)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransactionViewHolder {
        return TransactionViewHolder(mCursorAdapter.newView(mContext, mCursorAdapter.cursor, parent))
    }

    override fun onBindViewHolder(holder: TransactionViewHolder, position: Int) {
        setViewHolder(holder)

        // Move cursor to the current position
        mCursorAdapter.cursor.moveToPosition(position)
        mCursorAdapter.bindView(null, mContext, mCursorAdapter.cursor)
    }

    inner class TransactionViewHolder(view: View) : RecyclerViewCursorViewHolder(view), View.OnClickListener, View.OnLongClickListener {
        val mDescriptionTextView: TextView = view.findViewById(R.id.transaction_description) as TextView
        val mAmountTextView: TextView = view.findViewById(R.id.transaction_amount) as TextView
        val mDateTextView: TextView = view.findViewById(R.id.transaction_date) as TextView
        val mCategoryTextView: TextView = view.findViewById(R.id.transaction_category) as TextView
        val mNotesTextView: TextView = view.findViewById(R.id.transaction_notes) as TextView
        val mIndicatorView: View = view.findViewById(R.id.transaction_indicator)

        init {
            view.setOnClickListener(this)
            view.setOnLongClickListener(this)
        }

        override fun bindCursor(cursor: Cursor) {
            // Set description
            mDescriptionTextView.text = cursor.getString(DESCRIPTION_INDEX)

            // Set amount
            val amount = cursor.getDouble(AMOUNT_INDEX)
            mAmountTextView.text = amount.asCurrency()

            // Set withdrawal. Depending on withdrawal, we need to color certain views.
            val isWithdrawal = cursor.getInt(WITHDRAWAL_INDEX)
            if (isWithdrawal == 1) {
                mAmountTextView.text = String.format("-%s", amount.asCurrency())
                mAmountTextView.setTextColor(red)
                mIndicatorView.setBackgroundColor(red)
            } else {
                mAmountTextView.text = amount.asCurrency()
                mAmountTextView.setTextColor(primaryTextColor)
                mIndicatorView.setBackgroundColor(green)
            }

            // Set date
            val dateString = cursor.getString(DATE_INDEX)
            mDateTextView.text = Utility.getUIDateStringFromDB(dateString)

            //TODO: Globals somewhere
            val defaultCategory = "None"
            val category = cursor.getString(CATEGORY_INDEX)
            mCategoryTextView.text =
                    if (category == defaultCategory)
                        ""
                    else
                        category

            // Set notes.
            //TODO: Use resource
            mNotesTextView.text = String.format("Notes: %s", cursor.getString(NOTES_INDEX))
        }

        override fun onClick(v: View) {
            // Toggle notes
            mNotesTextView.visibility =
                    if (mNotesTextView.visibility == View.VISIBLE)
                        View.GONE
                    else
                        View.VISIBLE
        }

        override fun onLongClick(view: View): Boolean {
            // Get current item and call back to activity
            mCursorAdapter.cursor.moveToPosition(adapterPosition)
            (mContext as OnTransactionLongClickListener).onTransactionLongClick(TransactionDetails(mCursorAdapter.cursor))
            return true
        }
    }

    interface OnTransactionLongClickListener {
        fun onTransactionLongClick(transaction: TransactionDetails)
    }

    companion object {

        val TRANSACTION_COLUMNS = arrayOf(CCContract.TransactionEntry.TABLE_NAME + "." + BaseColumns._ID, CCContract.TransactionEntry.COLUMN_DESCRIPTION, CCContract.TransactionEntry.COLUMN_AMOUNT, CCContract.TransactionEntry.COLUMN_WITHDRAWAL, CCContract.TransactionEntry.COLUMN_NOTES, CCContract.TransactionEntry.COLUMN_DATE, CCContract.CategoryEntry.COLUMN_DESCRIPTION, CCContract.TransactionEntry.COLUMN_CATEGORY, CCContract.TransactionEntry.COLUMN_ACCOUNT)

        // Indexes for each of the columns of display data.
        private val DESCRIPTION_INDEX = 1
        private val AMOUNT_INDEX = 2
        private val WITHDRAWAL_INDEX = 3
        private val NOTES_INDEX = 4
        private val DATE_INDEX = 5
        private val CATEGORY_INDEX = 6
    }
}
