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
 * Adapter for displaying repeating periods.
 *
 * Created by adam.mcneilly on 9/5/16.
 */
public class RepeatingPeriodAdapter extends CursorAdapter {
    public RepeatingPeriodAdapter(Context context){
        super(context, null, 0);
    }

    public static final String[] REPEATING_PERIOD_COLUMNS = new String[] {
            CCContract.RepeatingPeriodEntry.TABLE_NAME + "." + CCContract.RepeatingPeriodEntry._ID,
            CCContract.RepeatingPeriodEntry.COLUMN_NAME
    };

    public static final int NAME_INDEX = 1;

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        // Create view
        View view = LayoutInflater.from(context).inflate(R.layout.list_item_textview, parent, false);

        // Set ViewHolder
        view.setTag(new RepeatingPeriodViewHolder(view));

        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        // Bind values
        ((RepeatingPeriodViewHolder)view.getTag()).bindCursor(cursor);
    }

    public class RepeatingPeriodViewHolder {
        public final TextView repeatingPeriodNameTextView;

        public RepeatingPeriodViewHolder(View view){
            repeatingPeriodNameTextView = (TextView) view.findViewById(R.id.list_item_text_view);
        }

        // Set values.
        public void bindCursor(Cursor cursor) {
            repeatingPeriodNameTextView.setText(cursor.getString(NAME_INDEX));
        }
    }
}
