package com.androidessence.cashcaretaker.adapters

import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AlertDialog
import android.support.v7.view.ActionMode
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.androidessence.cashcaretaker.R
import com.androidessence.cashcaretaker.activities.TransactionsActivity
import com.androidessence.cashcaretaker.core.CoreActivity
import com.androidessence.cashcaretaker.data.CCContract
import com.androidessence.cashcaretaker.dataTransferObjects.Account
import com.androidessence.recyclerviewcursoradapter.RecyclerViewCursorAdapter
import com.androidessence.recyclerviewcursoradapter.RecyclerViewCursorViewHolder
import com.androidessence.utility.asCurrency
import com.androidessence.utility.default

/**
 * Adapter that displays all accounts.

 * Created by adam.mcneilly on 9/5/16.
 */
class AccountAdapter(context: Context) : RecyclerViewCursorAdapter<AccountAdapter.AccountViewHolder>(context) {

    private val red: Int = ContextCompat.getColor(mContext, R.color.mds_red_500)
    private val primaryTextColor: Int = ContextCompat.getColor(mContext, android.R.color.primary_text_light)

    private var actionMode: ActionMode? = null

    private val actionModeCallback = object : ActionMode.Callback {
        // Called when the action mode is created; startActionMode() was called
        override fun onCreateActionMode(mode: ActionMode, menu: Menu): Boolean {
            // Inflate a menu resource providing context menu items
            mode.menuInflater.inflate(R.menu.account_context_menu, menu)
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
                R.id.action_delete_account -> {
                    // The account that was selected is passed as the tag
                    // for the action mode.
                    showAccountDeleteAlertDialog(actionMode?.tag as? Account)
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

    private fun showAccountDeleteAlertDialog(account: Account?) {
        AlertDialog.Builder(mContext)
                .setTitle("Delete Account")
                .setMessage("Are you sure you want to delete " + account?.name + "?")
                .setPositiveButton("Yes") { dialog, which ->
                    //TODO: Handle the update with the new activity
                    mContext.contentResolver.delete(
                            CCContract.AccountEntry.CONTENT_URI,
                            CCContract.AccountEntry._ID + " = ?",
                            arrayOf(account?.identifier.toString())
                    )
                    (mContext as OnAccountDeletedListener).onAccountDeleted(account?.identifier.default(0))
                    dialog.dismiss()
                }
                .setNegativeButton("No") { dialog, which -> dialog.dismiss() }
                .create().show()
    }

    init {
        setupCursorAdapter(null, 0, R.layout.list_item_account, false)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AccountViewHolder {
        return AccountViewHolder(mCursorAdapter.newView(mContext, mCursorAdapter.cursor, parent))
    }

    override fun onBindViewHolder(holder: AccountViewHolder, position: Int) {
        // Set holder
        setViewHolder(holder)

        // Move Cursor to this item
        mCursorAdapter.cursor.moveToPosition(position)

        // Bind view
        mCursorAdapter.bindView(null, mContext, mCursorAdapter.cursor)
    }

    private fun startTransactionActivity(account: Account) {
        // Create intent
        val transactionsActivity = Intent(mContext, TransactionsActivity::class.java)

        // Build and set arguments.
        val args = Bundle()
        args.putParcelable(TransactionsActivity.ARG_ACCOUNT, account)
        transactionsActivity.putExtras(args)

        // Start activity
        mContext.startActivity(transactionsActivity)
    }

    inner class AccountViewHolder(view: View) : RecyclerViewCursorViewHolder(view), View.OnClickListener, View.OnLongClickListener {
        val nameTextView: TextView = view.findViewById(R.id.account_name) as TextView
        val balanceTextView: TextView = view.findViewById(R.id.account_balance) as TextView

        init {
            view.setOnClickListener(this)
            view.setOnLongClickListener(this)
        }

        override fun bindCursor(cursor: Cursor) {
            // Set name
            nameTextView.text = cursor.getString(NAME_INDEX)

            // Set balance
            val balance = cursor.getDouble(BALANCE_INDEX)
            balanceTextView.text = balance.asCurrency()

            // If balance is negative set red
            balanceTextView.setTextColor(
                    if (balance < 0.00) red else primaryTextColor)
        }

        internal fun startActionMode(account: Account) {
            // Don't fire if the action mode is already active.
            if (actionMode == null) {
                // Start the CAB using the ActionMode.Callback already defined
                actionMode = (mContext as CoreActivity).startSupportActionMode(actionModeCallback)
                // Get name to set as title for action bar
                actionMode?.title = account.name
                // Get account ID to pass as tag.
                actionMode?.tag = account
            }
        }

        override fun onClick(v: View) {
            // Get cursor for item clicked.
            mCursorAdapter.cursor.moveToPosition(adapterPosition)
            startTransactionActivity(Account(mCursorAdapter.cursor))
        }

        override fun onLongClick(v: View): Boolean {
            // Get cursor for item clicked.
            mCursorAdapter.cursor.moveToPosition(adapterPosition)
            startActionMode(Account(mCursorAdapter.cursor))
            return true
        }
    }

    interface OnAccountDeletedListener {
        fun onAccountDeleted(id: Long)
    }

    companion object {
        val ACCOUNT_COLUMNS = arrayOf(CCContract.AccountEntry.TABLE_NAME + "." + CCContract.AccountEntry._ID, CCContract.AccountEntry.COLUMN_NAME, CCContract.AccountEntry.COLUMN_BALANCE)

        private val NAME_INDEX = 1
        private val BALANCE_INDEX = 2
    }
}
