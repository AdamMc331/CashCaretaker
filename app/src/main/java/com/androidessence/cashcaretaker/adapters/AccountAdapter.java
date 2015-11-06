package com.androidessence.cashcaretaker.adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.widget.CursorAdapter;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.androidessence.cashcaretaker.activities.TransactionsActivity;
import com.androidessence.cashcaretaker.data.CCContract;
import com.androidessence.cashcaretaker.R;
import com.androidessence.cashcaretaker.Utility;
import com.androidessence.cashcaretaker.dataTransferObjects.Account;

/**
 * RecyclerView.Adapter used to display the user's list of accounts.
 *
 * Created by adammcneilly on 11/1/15.
 */
public class AccountAdapter extends RecyclerViewCursorAdapter<AccountAdapter.AccountViewHolder> {

    /**
     * The necessary data fields to display for each account.
     */
    public static final String[] ACCOUNT_COLUMNS = new String[] {
            CCContract.AccountEntry.TABLE_NAME + "." + CCContract.AccountEntry._ID,
            CCContract.AccountEntry.COLUMN_NAME,
            CCContract.AccountEntry.COLUMN_BALANCE
    };

    // Indexes for each of the columns of display data.
    public static final int NAME_INDEX = 1;
    public static final int BALANCE_INDEX = 2;

    /**
     * The ActionMode used to delete an Account.
     */
    private ActionMode mActionMode;

    /**
     * An Action mode callback used for the context menu.
     */
    private final ActionMode.Callback mAccountActionModeCallback = new ActionMode.Callback() {
        // Called when the action mode is created; startActionMode() was called
        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            // Inflate a menu resource providing context menu items
            MenuInflater inflater = mode.getMenuInflater();
            inflater.inflate(R.menu.account_context_menu, menu);
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
                    showAccountDeleteAlertDialog((Account) mActionMode.getTag());
                    mode.finish(); // Action picked, so close the CAB
                    return true;
                default:
                    return false;
            }
        }

        // Called when the user exits the action mode
        @Override
        public void onDestroyActionMode(ActionMode mode) {
            mActionMode = null;
        }
    };

    /**
     * Alerts the user that they are about to delete an account and ensures that they
     * are okay with it.
     */
    private void showAccountDeleteAlertDialog(final Account account){
        final AlertDialog alertDialog = new AlertDialog.Builder(mContext).create();
        alertDialog.setTitle("Delete Account");
        alertDialog.setMessage("Are you sure you want to delete " + account.getName() + "?");
        alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "Yes",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //TODO: Handle the update with the new activity.
                        // Remove
                        mContext.getContentResolver().delete(
                                CCContract.AccountEntry.CONTENT_URI,
                                CCContract.AccountEntry._ID + " = ?",
                                new String[]{String.valueOf(account.getIdentifier())}
                        );
                        alertDialog.dismiss();
                    }
                });
        alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                alertDialog.dismiss();
            }
        });
        alertDialog.show();
    }

    public AccountAdapter(Context context){
        super(context);

        this.mCursorAdapter = new CursorAdapter(mContext, null, 0) {
            @Override
            public View newView(Context context, Cursor cursor, ViewGroup parent) {
                return LayoutInflater.from(context).inflate(R.layout.list_item_account, parent, false);
            }

            @Override
            public void bindView(View view, Context context, Cursor cursor) {
                AccountViewHolder viewHolder = (AccountViewHolder) view.getTag();

                // Bind item
                viewHolder.bindCursor(cursor);
            }
        };
    }

    @Override
    public AccountViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new AccountViewHolder(mCursorAdapter.newView(mContext, mCursorAdapter.getCursor(), parent));
    }

    @Override
    public void onBindViewHolder(AccountViewHolder holder, int position) {
        // Set tag
        mTempView.setTag(holder);

        // Move Cursor to this item
        mCursorAdapter.getCursor().moveToPosition(position);

        // Bind view
        mCursorAdapter.bindView(mTempView, mContext, mCursorAdapter.getCursor());
    }

    /**
     * Starts the TransactionsActivity for an Account.
     * @param account The account item from the list to view Transaction information for.
     */
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
        void bindCursor(Cursor cursor) {
            // Set name
            nameTextView.setText(cursor.getString(NAME_INDEX));

            // Set balance
            double balance = cursor.getDouble(BALANCE_INDEX);
            balanceTextView.setText(Utility.getCurrencyString(balance));
        }

        void startActionMode(Account account){
            // Don't fire if the action mode is already active.
            if (mActionMode == null) {
                // Start the CAB using the ActionMode.Callback already defined
                mActionMode = ((AppCompatActivity) mContext).startSupportActionMode(mAccountActionModeCallback);
                // Get name to set as title for action bar
                mActionMode.setTitle(account.getName());
                // Get account ID to pass as tag.
                mActionMode.setTag(account);
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
}
