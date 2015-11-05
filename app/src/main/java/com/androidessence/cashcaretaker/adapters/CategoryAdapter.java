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
 * Created by adammcneilly on 11/2/15.
 */
public class CategoryAdapter extends CursorAdapter {
    public CategoryAdapter(Context context){
        super(context, null, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_item_textview, parent, false);
        view.setTag(new CategoryViewHolder(view));
        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        CategoryViewHolder viewHolder = (CategoryViewHolder) view.getTag();

        viewHolder.categoryNameTextView.setText(cursor.getString(cursor.getColumnIndex(CCContract.CategoryEntry.COLUMN_DESCRIPTION)));
    }

    public class CategoryViewHolder{
        public final TextView categoryNameTextView;

        public CategoryViewHolder(View view){
            categoryNameTextView = (TextView) view.findViewById(R.id.list_item_text_view);
        }
    }
}
