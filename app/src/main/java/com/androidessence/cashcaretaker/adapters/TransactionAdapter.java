package com.androidessence.cashcaretaker.adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ActionMode;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.androidessence.cashcaretaker.R;
import com.androidessence.cashcaretaker.Utility;
import com.androidessence.cashcaretaker.data.CCContract;
import com.androidessence.cashcaretaker.dataTransferObjects.Transaction;

/**
 * An apter for displaying a list of Transaction objects.
 *
 * Created by adammcneilly on 11/1/15.
 */
public class TransactionAdapter extends RecyclerViewCursorAdapter<TransactionAdapter.TransactionViewHolder>{
    /**
     * A list of data columns required for creating and displaying a Transaction object.
     */
    public static final String[] TRANSACTION_COLUMNS = new String[] {
            CCContract.TransactionEntry.TABLE_NAME + "." + CCContract.TransactionEntry._ID,
            CCContract.TransactionEntry.COLUMN_DESCRIPTION,
            CCContract.TransactionEntry.COLUMN_AMOUNT,
            CCContract.TransactionEntry.COLUMN_WITHDRAWAL,
            CCContract.TransactionEntry.COLUMN_NOTES,
            CCContract.TransactionEntry.COLUMN_DATE,
            CCContract.CategoryEntry.COLUMN_DESCRIPTION,
            CCContract.TransactionEntry.COLUMN_CATEGORY,
            CCContract.TransactionEntry.COLUMN_ACCOUNT
    };

    // Indexes for each of the columns of display data.
    private static final int DESCRIPTION_INDEX = 1;
    private static final int AMOUNT_INDEX = 2;
    private static final int WITHDRAWAL_INDEX = 3;
    private static final int NOTES_INDEX = 4;
    private static final int DATE_INDEX = 5;
    private static final int CATEGORY_INDEX = 6;

    // Colors used inside the ViewHolder.
    private final int mRed;
    private final int mGreen;
    private final int mPrimaryText;

    /**
     * The ActionMode to be displayed for deleting a Transaction.
     */
    private ActionMode mActionMode;

    /**
     * An Action mode callback used for the context menu.
     */
    private final ActionMode.Callback mActionModeCallback = new ActionMode.Callback() {
        // Called when the action mode is created; startActionMode() was called
        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            // Inflate a menu resource providing context menu items
            MenuInflater inflater = mode.getMenuInflater();
            inflater.inflate(R.menu.transaction_context_menu, menu);
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
                    showDeleteAlertDialog((Transaction) mActionMode.getTag());
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
     * Alerts the user that they are about to delete a transaction and ensures that they
     * are okay with it.
     *
     * Returns true if the transaction was deleted, false otherwise.
     */
    private void showDeleteAlertDialog(final Transaction transaction){
        final AlertDialog alertDialog = new AlertDialog.Builder(mContext).create();
        alertDialog.setTitle("Delete Transaction");
        alertDialog.setMessage("Are you sure you want to delete " + transaction.getDescription() + "?");
        alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "Yes",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //TODO: Handle update
                        // Remove
                        mContext.getContentResolver().delete(
                                CCContract.TransactionEntry.CONTENT_URI,
                                CCContract.TransactionEntry._ID + " = ?",
                                new String[]{String.valueOf(transaction.getIdentifier())}
                        );
                        Log.v("TRANSACTION_ADAPTER", "Deleting transaction: " + transaction.getIdentifier());
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

    public TransactionAdapter(Context context){
        super(context);

        mRed = mContext.getResources().getColor(R.color.red);
        mGreen = mContext.getResources().getColor(R.color.green);
        mPrimaryText = mContext.getResources().getColor(android.R.color.primary_text_light);

        this.mCursorAdapter = new CursorAdapter(mContext, null, 0) {
            @Override
            public View newView(Context context, Cursor cursor, ViewGroup parent) {
                return LayoutInflater.from(context).inflate(R.layout.list_item_transaction, parent, false);
            }

            @Override
            public void bindView(View view, Context context, Cursor cursor) {
                TransactionViewHolder viewHolder = (TransactionViewHolder) view.getTag();
                viewHolder.bindCursor(cursor);
            }
        };
    }

    @Override
    public TransactionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new TransactionViewHolder(mCursorAdapter.newView(mContext, mCursorAdapter.getCursor(), parent));
    }

    @Override
    public void onBindViewHolder(TransactionViewHolder holder, int position) {
        mTempView.setTag(holder);

        // Move cursor to the current position
        mCursorAdapter.getCursor().moveToPosition(position);
        mCursorAdapter.bindView(mTempView, mContext, mCursorAdapter.getCursor());
    }

    public class TransactionViewHolder extends RecyclerViewCursorViewHolder implements View.OnClickListener, View.OnLongClickListener {
        public final TextView mDescriptionTextView;
        public final TextView mAmountTextView;
        public final TextView mDateTextView;
        public final TextView mCategoryTextView;
        public final TextView mNotesTextView;
        public final View mIndicatorView;

        public TransactionViewHolder(View view){
            super(view);
            mDescriptionTextView = (TextView) view.findViewById(R.id.transaction_description);
            mAmountTextView = (TextView) view.findViewById(R.id.transaction_amount);
            mDateTextView = (TextView) view.findViewById(R.id.transaction_date);
            mCategoryTextView = (TextView) view.findViewById(R.id.transaction_category);
            mNotesTextView = (TextView) view.findViewById(R.id.transaction_notes);
            mIndicatorView = view.findViewById(R.id.transaction_indicator);

            view.setOnClickListener(this);
            view.setOnLongClickListener(this);
        }

        @Override
        void bindCursor(Cursor cursor) {
            // Set description
            mDescriptionTextView.setText(cursor.getString(DESCRIPTION_INDEX));

            // Set amount
            double amount = cursor.getDouble(AMOUNT_INDEX);
            mAmountTextView.setText(Utility.getCurrencyString(amount));

            // Set withdrawal. Depending on withdrawal, we need to color certain views.
            int isWithdrawal = cursor.getInt(WITHDRAWAL_INDEX);
            if(isWithdrawal == 1) {
                mAmountTextView.setText(String.format("-%s", Utility.getCurrencyString(amount)));
                mAmountTextView.setTextColor(mRed);
                mIndicatorView.setBackgroundColor(mRed);
            } else{
                mAmountTextView.setText(Utility.getCurrencyString(amount));
                mAmountTextView.setTextColor(mPrimaryText);
                mIndicatorView.setBackgroundColor(mGreen);
            }

            // Set date
            String dateString = cursor.getString(DATE_INDEX);
            mDateTextView.setText(Utility.getUIDateStringFromDB(dateString));

            //TODO: Globals somewhere
            String defaultCategory = "None";
            String category = cursor.getString(CATEGORY_INDEX);
            if(category.equals(defaultCategory)) {
                mCategoryTextView.setText("");
            } else{
                mCategoryTextView.setText(category);
            }

            // Set notes.
            //TODO: Use resource
            mNotesTextView.setText(String.format("Notes: %s", cursor.getString(NOTES_INDEX)));
        }

        @Override
        public void onClick(View v) {
            // Toggle notes
            if(mNotesTextView.getVisibility() == View.VISIBLE) {
                mNotesTextView.setVisibility(View.GONE);
            } else if(mNotesTextView.getVisibility() == View.GONE) {
                mNotesTextView.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public boolean onLongClick(View view) {
            // Get current item and start action mode
            mCursorAdapter.getCursor().moveToPosition(getAdapterPosition());
            startActionMode(new Transaction(mCursorAdapter.getCursor()));
            return true;
        }

        private void startActionMode(Transaction transaction){
            // Don't fire if action mode is already being used
            if(mActionMode == null){
                // Start the CAB using the ActionMode.Callback already defined
                mActionMode = ((AppCompatActivity)mContext).startSupportActionMode(mActionModeCallback);
                // Get name to set as title for action bar
                // Need to subtract one to account for Header position
                mActionMode.setTitle(transaction.getDescription());
                // Get account ID to pass as tag.
                mActionMode.setTag(transaction);
            }
        }
    }
}
