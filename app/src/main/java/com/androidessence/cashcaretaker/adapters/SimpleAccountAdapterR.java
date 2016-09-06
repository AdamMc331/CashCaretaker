package com.androidessence.cashcaretaker.adapters;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.androidessence.cashcaretaker.R;
import com.androidessence.cashcaretaker.data.CCContract;

/**
 * Adapter that displays basic account information.
 *
 * Created by adam.mcneilly on 9/5/16.
 */
public class SimpleAccountAdapterR extends CursorAdapter {

    public SimpleAccountAdapterR(Context context) {
        super(context, null, 0);
    }

    public static final String[] ACCOUNT_COLUMNS = new String[] {
            CCContract.AccountEntry.TABLE_NAME + "." + CCContract.AccountEntry._ID,
            CCContract.AccountEntry.COLUMN_NAME,
            CCContract.AccountEntry.COLUMN_BALANCE
    };

    public static final int NAME_INDEX = 1;

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_item_textview, parent, false);
        view.setTag(new SimpleAccountViewHolder(view));
        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        ((SimpleAccountViewHolder)view.getTag()).bindCursor(cursor);
    }

    public class SimpleAccountViewHolder {
        public final TextView accountNameTextView;

        public SimpleAccountViewHolder(View view) {
            accountNameTextView = (TextView) view.findViewById(R.id.list_item_text_view);
        }

        public void bindCursor(Cursor cursor) {
            accountNameTextView.setText(cursor.getString(NAME_INDEX));
        }
    }
}
