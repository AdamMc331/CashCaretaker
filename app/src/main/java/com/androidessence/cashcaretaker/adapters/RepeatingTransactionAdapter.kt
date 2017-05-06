package com.androidessence.cashcaretaker.adapters

import android.content.Context
import android.database.Cursor
import android.provider.BaseColumns
import android.support.v4.content.ContextCompat
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.support.v7.view.ActionMode
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.androidessence.cashcaretaker.R
import com.androidessence.cashcaretaker.data.CCContract
import com.androidessence.cashcaretaker.dataTransferObjects.RepeatingTransaction
import com.androidessence.recyclerviewcursoradapter.RecyclerViewCursorAdapter
import com.androidessence.recyclerviewcursoradapter.RecyclerViewCursorViewHolder
import com.androidessence.utility.Utility
import com.androidessence.utility.asCurrency

/**
 * Adapter for displaying repeating transactions.

 * Created by adam.mcneilly on 9/5/16.
 */
class RepeatingTransactionAdapter(context: Context) : RecyclerViewCursorAdapter<RepeatingTransactionAdapter.RepeatingTransactionViewHolder>(context) {

    // Colors used inside the ViewHolder.
    private val red: Int = ContextCompat.getColor(mContext, R.color.mds_red_500)
    private val green: Int = ContextCompat.getColor(mContext, R.color.mds_green_500)
    private val primaryTextColor: Int = ContextCompat.getColor(mContext, android.R.color.primary_text_light)

    private var actionMode: ActionMode? = null

    private val actionModeCallback = object : ActionMode.Callback {
        // Called when the action mode is created; startActionMode() was called
        override fun onCreateActionMode(mode: ActionMode, menu: Menu): Boolean {
            // Inflate a menu resource providing context menu items
            mode.menuInflater.inflate(R.menu.repeating_transaction_context_menu, menu)
            return true
        }

        // Called each time the action mode is shown. Always called after onCreateActionMode, but
        // may be called multiple times if the mode is invalidated.
        override fun onPrepareActionMode(mode: ActionMode, menu: Menu): Boolean {
            return false // Return false if nothing is done
        }

        // Called when the user selects a contextual menu item
        override fun onActionItemClicked(mode: ActionMode, item: MenuItem): Boolean {
            when (item.itemId) {
                R.id.action_delete_transaction -> {
                    // The transaction that was selected is passed as the tag
                    // for the action mode.
                    showDeleteAlertDialog(actionMode?.tag as? RepeatingTransaction)
                    mode.finish() // Action picked, so close the CAB
                    return true
                }
                else -> return false
            }
        }

        // Called when the user exits the action mode
        override fun onDestroyActionMode(mode: ActionMode) {
            actionMode = null
        }
    }

    private fun showDeleteAlertDialog(transaction: RepeatingTransaction?) {
        AlertDialog.Builder(mContext)
                .setTitle("Delete Repeating Transaction")
                .setMessage("Are you sure you want to delete " + transaction?.description + "?")
                .setPositiveButton("Yes") { dialog, _ ->
                    //TODO: Handle update
                    // Remove
                    mContext.contentResolver.delete(
                            CCContract.RepeatingTransactionEntry.CONTENT_URI,
                            BaseColumns._ID + " = ?",
                            arrayOf(transaction?.identifier.toString())
                    )
                    dialog.dismiss()
                }
                .setNegativeButton("No") { dialog, which -> dialog.dismiss() }
                .create().show()
    }

    init {
        setupCursorAdapter(null, 0, R.layout.list_item_repeating_transaction, false)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RepeatingTransactionViewHolder {
        return RepeatingTransactionViewHolder(mCursorAdapter.newView(mContext, mCursorAdapter.cursor, parent))
    }

    override fun onBindViewHolder(holder: RepeatingTransactionViewHolder, position: Int) {
        setViewHolder(holder)

        mCursorAdapter.cursor.moveToPosition(position)
        mCursorAdapter.bindView(null, mContext, mCursorAdapter.cursor)
    }

    inner class RepeatingTransactionViewHolder(view: View) : RecyclerViewCursorViewHolder(view), View.OnClickListener, View.OnLongClickListener {
        val mDescriptionTextView: TextView = view.findViewById(R.id.transaction_description) as TextView
        val mAmountTextView: TextView = view.findViewById(R.id.transaction_amount) as TextView
        val mNextDateTextView: TextView = view.findViewById(R.id.transaction_date) as TextView
        val mCategoryTextView: TextView = view.findViewById(R.id.transaction_category) as TextView
        val mAccountTextView: TextView = view.findViewById(R.id.transaction_account) as TextView
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
            val dateString = cursor.getString(NEXT_DATE_INDEX)
            mNextDateTextView.text = String.format("Next date: %s", Utility.getUIDateStringFromDB(dateString))

            //TODO: Globals somewhere
            val defaultCategory = "None"
            val category = cursor.getString(CATEGORY_INDEX)
            mCategoryTextView.text =
                    if (category == defaultCategory)
                        ""
                    else
                        category

            mAccountTextView.text = String.format("Account: %s", cursor.getString(ACCOUNT_INDEX))

            // Set notes.
            //TODO: Use resource
            mNotesTextView.text = String.format("Notes: %s", cursor.getString(NOTES_INDEX))
        }

        override fun onClick(v: View) {
            // Toggle notes
            mNotesTextView.visibility =
                    if (mNextDateTextView.visibility == View.VISIBLE)
                        View.GONE
                    else
                        View.VISIBLE
        }

        override fun onLongClick(view: View): Boolean {
            // Get current item and start action mode
            mCursorAdapter.cursor.moveToPosition(adapterPosition)
            startActionMode(RepeatingTransaction(mCursorAdapter.cursor))
            return true
        }

        private fun startActionMode(transaction: RepeatingTransaction) {
            // Don't fire if action mode is already being used
            if (actionMode == null) {
                // Start the CAB using the ActionMode.Callback already defined
                actionMode = (mContext as AppCompatActivity).startSupportActionMode(actionModeCallback)
                // Get name to set as title for action bar
                // Need to subtract one to account for Header position
                actionMode?.title = transaction.description
                // Get account ID to pass as tag.
                actionMode?.tag = transaction
            }
        }
    }

    companion object {

        val REPEATING_TRANSACTION_COLUMNS = arrayOf(
                CCContract.RepeatingTransactionEntry.TABLE_NAME + "." + BaseColumns._ID,
                CCContract.RepeatingTransactionEntry.COLUMN_DESCRIPTION,
                CCContract.RepeatingTransactionEntry.COLUMN_AMOUNT,
                CCContract.RepeatingTransactionEntry.COLUMN_WITHDRAWAL,
                CCContract.RepeatingTransactionEntry.COLUMN_NEXT_DATE,
                CCContract.RepeatingTransactionEntry.COLUMN_NOTES,
                CCContract.AccountEntry.COLUMN_NAME,
                CCContract.CategoryEntry.COLUMN_DESCRIPTION,
                CCContract.RepeatingTransactionEntry.COLUMN_ACCOUNT,
                CCContract.RepeatingTransactionEntry.COLUMN_REPEATING_PERIOD,
                CCContract.RepeatingTransactionEntry.COLUMN_CATEGORY,
                CCContract.RepeatingPeriodEntry.COLUMN_NAME)

        val DESCRIPTION_INDEX = 1
        val AMOUNT_INDEX = 2
        val WITHDRAWAL_INDEX = 3
        val NEXT_DATE_INDEX = 4
        val NOTES_INDEX = 5
        val ACCOUNT_INDEX = 6
        val CATEGORY_INDEX = 7
    }
}
