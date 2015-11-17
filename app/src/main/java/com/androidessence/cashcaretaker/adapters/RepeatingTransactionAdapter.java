package com.androidessence.cashcaretaker.adapters;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.androidessence.cashcaretaker.R;
import com.androidessence.cashcaretaker.data.CCContract;

/**
 * Adapter for repeating transaction objects.
 *
 * Created by adammcneilly on 11/16/15.
 */
public class RepeatingTransactionAdapter extends RecyclerViewCursorAdapter<RepeatingTransactionAdapter.RepeatingTransactionViewHolder>{

    public static final String[] REPEATING_TRANSACTION_COLUMNS = new String[] {
            CCContract.RepeatingTransactionEntry.TABLE_NAME + "." + CCContract.RepeatingTransactionEntry._ID,
            CCContract.RepeatingTransactionEntry.COLUMN_DESCRIPTION,
            CCContract.RepeatingTransactionEntry.COLUMN_AMOUNT,
            CCContract.RepeatingTransactionEntry.COLUMN_WITHDRAWAL,
            CCContract.RepeatingTransactionEntry.COLUMN_NEXT_DATE,
            CCContract.RepeatingTransactionEntry.COLUMN_NOTES,
            CCContract.AccountEntry.COLUMN_NAME,
            CCContract.CategoryEntry.COLUMN_DESCRIPTION
    };

    public static final int DESCRIPTION_INDEX = 1;
    public static final int AMOUNT_INDEX = 2;
    public static final int WITHDRAWAL_INDEX = 3;
    public static final int NEXT_DATE_INDEX = 4;
    public static final int NOTES_INDEX = 5;
    public static final int ACCOUNT_INDEX = 6;
    public static final int CATEGORY_INDEX = 7;

    public RepeatingTransactionAdapter(Context context) {
        super(context);

        this.mCursorAdapter = new CursorAdapter(mContext, null, 0) {
            @Override
            public View newView(Context context, Cursor cursor, ViewGroup parent) {
                return LayoutInflater.from(context).inflate(R.layout.list_item_repeating_transaction, parent, false);
            }

            @Override
            public void bindView(View view, Context context, Cursor cursor) {
                // Get view holder
                RepeatingTransactionViewHolder viewHolder = (RepeatingTransactionViewHolder) view.getTag();
                viewHolder.bindCursor(cursor);
            }
        };
    }

    @Override
    public RepeatingTransactionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new RepeatingTransactionViewHolder(mCursorAdapter.newView(mContext, mCursorAdapter.getCursor(), parent));
    }

    @Override
    public void onBindViewHolder(RepeatingTransactionViewHolder holder, int position) {
        mTempView.setTag(holder);

        mCursorAdapter.getCursor().moveToPosition(position);
        mCursorAdapter.bindView(mTempView, mContext, mCursorAdapter.getCursor());
    }

    public void swapCursor(Cursor cursor) {
        mCursorAdapter.swapCursor(cursor);
        notifyDataSetChanged();
    }

    public class RepeatingTransactionViewHolder extends RecyclerViewCursorViewHolder {
        public final TextView descriptionTextView;
        public final TextView amountTextView;
        public final TextView nextDateTextView;
        public final TextView categoryTextView;
        public final TextView accountTextView;
        public final TextView notesTextView;
        public final View indicatorView;

        public RepeatingTransactionViewHolder(View view) {
            super(view);

            descriptionTextView = (TextView) view.findViewById(R.id.transaction_description);
            amountTextView = (TextView) view.findViewById(R.id.transaction_amount);
            nextDateTextView = (TextView) view.findViewById(R.id.transaction_date);
            categoryTextView = (TextView) view.findViewById(R.id.transaction_category);
            accountTextView = (TextView) view.findViewById(R.id.transaction_account);
            notesTextView = (TextView) view.findViewById(R.id.transaction_notes);
            indicatorView = view.findViewById(R.id.transaction_indicator);
        }

        @Override
        void bindCursor(Cursor cursor) {

        }
    }
}
