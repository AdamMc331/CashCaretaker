package com.androidessence.cashcaretaker.adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.androidessence.cashcaretaker.R;
import com.androidessence.cashcaretaker.activities.TransactionsActivity;
import com.androidessence.cashcaretaker.core.CoreActivity;
import com.androidessence.cashcaretaker.data.CCContract;
import com.androidessence.cashcaretaker.dataTransferObjects.Account;
import com.androidessence.recyclerviewcursoradapter.RecyclerViewCursorAdapter;
import com.androidessence.recyclerviewcursoradapter.RecyclerViewCursorViewHolder;
import com.androidessence.utility.Utility;

/**
 * Adapter that displays all accounts.
 *
 * Created by adam.mcneilly on 9/5/16.
 */
public class AccountAdapterR extends RecyclerViewCursorAdapter<AccountAdapterR.AccountViewHolder> {

    public static final String[] ACCOUNT_COLUMNS = new String[] {
            CCContract.AccountEntry.TABLE_NAME + "." + CCContract.AccountEntry._ID,
            CCContract.AccountEntry.COLUMN_NAME,
            CCContract.AccountEntry.COLUMN_BALANCE
    };

    private static final int NAME_INDEX = 1;
    private static final int BALANCE_INDEX = 2;

    private final int red;
    private final int primaryTextColor;

    private ActionMode actionMode;

    private final ActionMode.Callback actionModeCallback = new ActionMode.Callback() {
        // Called when the action mode is created; startActionMode() was called
        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            // Inflate a menu resource providing context menu items
            mode.getMenuInflater().inflate(R.menu.account_context_menu, menu);
            return true;
        }

        // Called each time the action mode is shown. Always called after onCreateActionMode, but
        // may be called multiple times if the mode is invalidated.
        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false; // Return false if nothing is done
        }

        // Called when the user selects a contextual menu item
        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            switch (item.getItemId()) {
                case R.id.action_delete_account:
                    // The account that was selected is passed as the tag
                    // for the action mode.
                    showAccountDeleteAlertDialog((Account) actionMode.getTag());
                    mode.finish(); // Action picked, so close the CAB
                    return true;
                default:
                    return false;
            }
        }

        // Called when the user exits the action mode
        @Override
        public void onDestroyActionMode(ActionMode mode) {
            actionMode = null;
        }
    };

    private void showAccountDeleteAlertDialog(final Account account){
        new AlertDialog.Builder(mContext)
                .setTitle("Delete Account")
                .setMessage("Are you sure you want to delete " + account.getName() + "?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //TODO: Handle the update with the new activity
                        mContext.getContentResolver().delete(
                                CCContract.AccountEntry.CONTENT_URI,
                                CCContract.AccountEntry._ID + " = ?",
                                new String[] { String.valueOf(account.getIdentifier()) }
                        );
                        ((OnAccountDeletedListener)mContext).onAccountDeleted(account.getIdentifier());
                        dialog.dismiss();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .create().show();
    }

    public AccountAdapterR(Context context){
        super(context);

        red = ContextCompat.getColor(mContext, R.color.mds_red_500);
        primaryTextColor = ContextCompat.getColor(mContext, android.R.color.primary_text_light);

        setupCursorAdapter(null, 0, R.layout.list_item_account, false);
    }

    @Override
    public AccountViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new AccountViewHolder(mCursorAdapter.newView(mContext, mCursorAdapter.getCursor(), parent));
    }

    @Override
    public void onBindViewHolder(AccountViewHolder holder, int position) {
        // Set holder
        setViewHolder(holder);

        // Move Cursor to this item
        mCursorAdapter.getCursor().moveToPosition(position);

        // Bind view
        mCursorAdapter.bindView(null, mContext, mCursorAdapter.getCursor());
    }

    private void startTransactionActivity(Account account){
        // Create intent
        Intent transactionsActivity = new Intent(mContext, TransactionsActivity.class);

        // Build and set arguments.
        Bundle args = new Bundle();
        args.putParcelable(TransactionsActivity.ARG_ACCOUNT, account);
        transactionsActivity.putExtras(args);

        // Start activity
        mContext.startActivity(transactionsActivity);
    }

    public class AccountViewHolder extends RecyclerViewCursorViewHolder implements View.OnClickListener, View.OnLongClickListener{
        public final TextView nameTextView;
        public final TextView balanceTextView;

        public AccountViewHolder(View view){
            super(view);

            nameTextView = (TextView) view.findViewById(R.id.account_name);
            balanceTextView = (TextView) view.findViewById(R.id.account_balance);

            view.setOnClickListener(this);
            view.setOnLongClickListener(this);
        }

        @Override
        public void bindCursor(Cursor cursor) {
            // Set name
            nameTextView.setText(cursor.getString(NAME_INDEX));

            // Set balance
            double balance = cursor.getDouble(BALANCE_INDEX);
            balanceTextView.setText(Utility.getCurrencyString(balance));

            // If balance is negative set red
            balanceTextView.setTextColor((balance < 0.00)
                    ? red
                    : primaryTextColor
            );
        }

        void startActionMode(Account account){
            // Don't fire if the action mode is already active.
            if (actionMode == null) {
                // Start the CAB using the ActionMode.Callback already defined
                actionMode = ((CoreActivity) mContext).startSupportActionMode(actionModeCallback);
                // Get name to set as title for action bar
                actionMode.setTitle(account.getName());
                // Get account ID to pass as tag.
                actionMode.setTag(account);
            }
        }

        @Override
        public void onClick(View v) {
            // Get cursor for item clicked.
            mCursorAdapter.getCursor().moveToPosition(getAdapterPosition());
            startTransactionActivity(new Account(mCursorAdapter.getCursor()));
        }

        @Override
        public boolean onLongClick(View v) {
            // Get cursor for item clicked.
            mCursorAdapter.getCursor().moveToPosition(getAdapterPosition());
            startActionMode(new Account(mCursorAdapter.getCursor()));
            return true;
        }
    }

    public interface OnAccountDeletedListener {
        void onAccountDeleted(long id);
    }
}
