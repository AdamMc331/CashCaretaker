package com.androidessence.cashcaretaker;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.androidessence.utility.DoubleUtilsKt;
import com.androidessence.utility.Utility;

/**
 * Adapter that displays accounts from the database on an Android wear device.
 *
 * Created by adammcneilly on 12/28/15.
 */
public class AccountCursorAdapter extends CursorAdapter {
    public static final String[] ACCOUNT_COLUMNS = new String[] {
            CCContract.AccountEntry.TABLE_NAME + "." + CCContract.AccountEntry._ID,
            CCContract.AccountEntry.COLUMN_NAME,
            CCContract.AccountEntry.COLUMN_BALANCE
    };

    private static final int NAME_INDEX = 1;
    private static final int BALANCE_INDEX = 2;

    public AccountCursorAdapter(Context context) {
        super(context, null, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_item_account, parent, false);
        view.setTag(new AccountViewHolder(view));
        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        AccountViewHolder accountViewHolder = (AccountViewHolder) view.getTag();
        accountViewHolder.bindCursor(cursor);
    }

    private class AccountViewHolder {
        private TextView mAccountName;
        private TextView mAccountBalance;

        public AccountViewHolder(View view) {
            mAccountName = (TextView) view.findViewById(R.id.account_name);
            mAccountBalance = (TextView) view.findViewById(R.id.account_balance);
        }

        public void bindCursor(Cursor cursor) {
            mAccountName.setText(cursor.getString(NAME_INDEX));
            mAccountBalance.setText(DoubleUtilsKt.asCurrency(cursor.getDouble(BALANCE_INDEX)));
        }
    }
}
