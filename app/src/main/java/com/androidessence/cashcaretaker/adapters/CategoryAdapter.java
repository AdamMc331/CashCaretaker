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
 * Adapter that displays a list of categories.
 *
 * Created by adam.mcneilly on 9/5/16.
 */
public class CategoryAdapter extends CursorAdapter {

    public static final String[] CATEGORY_COLUMNS = new String[] {
            CCContract.CategoryEntry.TABLE_NAME + "." + CCContract.CategoryEntry._ID,
            CCContract.CategoryEntry.COLUMN_DESCRIPTION,
            CCContract.CategoryEntry.COLUMN_IS_DEFAULT
    };

    private static final int DESCRIPTION_INDEX = 1;
    private static final int DEFAULT_INDEX = 2;

    public CategoryAdapter(Context context){
        super(context, null, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        // Create view
        View view = LayoutInflater.from(context).inflate(R.layout.list_item_textview, parent, false);

        // Set ViewHolder
        view.setTag(new CategoryViewHolder(view));

        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        // Bind values
        ((CategoryViewHolder)view.getTag()).bindCursor(cursor);
    }

    public class CategoryViewHolder{
        public final TextView categoryNameTextView;

        public CategoryViewHolder(View view){
            categoryNameTextView = (TextView) view.findViewById(R.id.list_item_text_view);
        }

        // Set values.
        public void bindCursor(Cursor cursor) {
            categoryNameTextView.setText(cursor.getString(DESCRIPTION_INDEX));
        }
    }
}
