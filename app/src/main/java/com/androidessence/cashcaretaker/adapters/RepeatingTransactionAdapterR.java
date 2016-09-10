package com.androidessence.cashcaretaker.adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.androidessence.cashcaretaker.R;
import com.androidessence.cashcaretaker.data.CCContract;
import com.androidessence.cashcaretaker.dataTransferObjects.RepeatingTransactionR;
import com.androidessence.recyclerviewcursoradapter.RecyclerViewCursorAdapter;
import com.androidessence.recyclerviewcursoradapter.RecyclerViewCursorViewHolder;
import com.androidessence.utility.Utility;

/**
 * Adapter for displaying repeating transactions.
 *
 * Created by adam.mcneilly on 9/5/16.
 */
public class RepeatingTransactionAdapterR extends RecyclerViewCursorAdapter<RepeatingTransactionAdapterR.RepeatingTransactionViewHolder> {

    public static final String[] REPEATING_TRANSACTION_COLUMNS = new String[] {
            CCContract.RepeatingTransactionEntry.TABLE_NAME + "." + CCContract.RepeatingTransactionEntry._ID,
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
            CCContract.RepeatingPeriodEntry.COLUMN_NAME
    };

    public static final int DESCRIPTION_INDEX = 1;
    public static final int AMOUNT_INDEX = 2;
    public static final int WITHDRAWAL_INDEX = 3;
    public static final int NEXT_DATE_INDEX = 4;
    public static final int NOTES_INDEX = 5;
    public static final int ACCOUNT_INDEX = 6;
    public static final int CATEGORY_INDEX = 7;

    // Colors used inside the ViewHolder.
    private final int red;
    private final int green;
    private final int primaryTextColor;

    private ActionMode actionMode;

    private final ActionMode.Callback actionModeCallback = new ActionMode.Callback() {
        // Called when the action mode is created; startActionMode() was called
        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            // Inflate a menu resource providing context menu items
            mode.getMenuInflater().inflate(R.menu.repeating_transaction_context_menu, menu);
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
                case R.id.action_delete_transaction:
                    // The transaction that was selected is passed as the tag
                    // for the action mode.
                    showDeleteAlertDialog((RepeatingTransactionR) actionMode.getTag());
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

    private void showDeleteAlertDialog(final RepeatingTransactionR transaction){
        new AlertDialog.Builder(mContext)
                .setTitle("Delete Repeating Transaction")
                .setMessage("Are you sure you want to delete " + transaction.getDescription() + "?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //TODO: Handle update
                        // Remove
                        mContext.getContentResolver().delete(
                                CCContract.RepeatingTransactionEntry.CONTENT_URI,
                                CCContract.RepeatingTransactionEntry._ID + " = ?",
                                new String[]{String.valueOf(transaction.getIdentifier())}
                        );
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

    public RepeatingTransactionAdapterR(Context context) {
        super(context);

        red = ContextCompat.getColor(mContext, R.color.mds_red_500);
        green = ContextCompat.getColor(mContext, R.color.mds_green_500);
        primaryTextColor = ContextCompat.getColor(mContext, android.R.color.primary_text_light);

        setupCursorAdapter(null, 0, R.layout.list_item_repeating_transaction, false);
    }

    @Override
    public RepeatingTransactionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new RepeatingTransactionViewHolder(mCursorAdapter.newView(mContext, mCursorAdapter.getCursor(), parent));
    }

    @Override
    public void onBindViewHolder(RepeatingTransactionViewHolder holder, int position) {
        setViewHolder(holder);

        mCursorAdapter.getCursor().moveToPosition(position);
        mCursorAdapter.bindView(null, mContext, mCursorAdapter.getCursor());
    }

    public class RepeatingTransactionViewHolder extends RecyclerViewCursorViewHolder implements View.OnClickListener, View.OnLongClickListener {
        public final TextView mDescriptionTextView;
        public final TextView mAmountTextView;
        public final TextView mNextDateTextView;
        public final TextView mCategoryTextView;
        public final TextView mAccountTextView;
        public final TextView mNotesTextView;
        public final View mIndicatorView;

        public RepeatingTransactionViewHolder(View view) {
            super(view);

            mDescriptionTextView = (TextView) view.findViewById(R.id.transaction_description);
            mAmountTextView = (TextView) view.findViewById(R.id.transaction_amount);
            mNextDateTextView = (TextView) view.findViewById(R.id.transaction_date);
            mCategoryTextView = (TextView) view.findViewById(R.id.transaction_category);
            mAccountTextView = (TextView) view.findViewById(R.id.transaction_account);
            mNotesTextView = (TextView) view.findViewById(R.id.transaction_notes);
            mIndicatorView = view.findViewById(R.id.transaction_indicator);

            view.setOnClickListener(this);
            view.setOnLongClickListener(this);
        }

        @Override
        public void bindCursor(Cursor cursor) {
            // Set description
            mDescriptionTextView.setText(cursor.getString(DESCRIPTION_INDEX));

            // Set amount
            double amount = cursor.getDouble(AMOUNT_INDEX);
            mAmountTextView.setText(Utility.getCurrencyString(amount));

            // Set withdrawal. Depending on withdrawal, we need to color certain views.
            int isWithdrawal = cursor.getInt(WITHDRAWAL_INDEX);
            if(isWithdrawal == 1) {
                mAmountTextView.setText(String.format("-%s", Utility.getCurrencyString(amount)));
                mAmountTextView.setTextColor(red);
                mIndicatorView.setBackgroundColor(red);
            } else{
                mAmountTextView.setText(Utility.getCurrencyString(amount));
                mAmountTextView.setTextColor(primaryTextColor);
                mIndicatorView.setBackgroundColor(green);
            }

            // Set date
            String dateString = cursor.getString(NEXT_DATE_INDEX);
            mNextDateTextView.setText(String.format("Next date: %s", Utility.getUIDateStringFromDB(dateString)));

            //TODO: Globals somewhere
            String defaultCategory = "None";
            String category = cursor.getString(CATEGORY_INDEX);
            mCategoryTextView.setText(category.equals(defaultCategory)
                    ? ""
                    : category);

            mAccountTextView.setText(String.format("Account: %s", cursor.getString(ACCOUNT_INDEX)));

            // Set notes.
            //TODO: Use resource
            mNotesTextView.setText(String.format("Notes: %s", cursor.getString(NOTES_INDEX)));
        }

        @Override
        public void onClick(View v) {
            // Toggle notes
            mNotesTextView.setVisibility((mNextDateTextView.getVisibility() == View.VISIBLE)
                    ? View.GONE
                    : View.VISIBLE
            );
        }

        @Override
        public boolean onLongClick(View view) {
            // Get current item and start action mode
            mCursorAdapter.getCursor().moveToPosition(getAdapterPosition());
            startActionMode(new RepeatingTransactionR(mCursorAdapter.getCursor()));
            return true;
        }

        private void startActionMode(RepeatingTransactionR transaction){
            // Don't fire if action mode is already being used
            if(actionMode == null){
                // Start the CAB using the ActionMode.Callback already defined
                actionMode = ((AppCompatActivity)mContext).startSupportActionMode(actionModeCallback);
                // Get name to set as title for action bar
                // Need to subtract one to account for Header position
                actionMode.setTitle(transaction.getDescription());
                // Get account ID to pass as tag.
                actionMode.setTag(transaction);
            }
        }
    }
}
