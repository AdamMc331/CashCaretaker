package com.androidessence.cashcaretaker.adapters;

import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by adammcneilly on 11/1/15.
 */
public abstract class RecyclerViewCursorViewHolder extends RecyclerView.ViewHolder {

    public RecyclerViewCursorViewHolder(View view){
        super(view);
    }

    abstract void bindCursor(Cursor cursor);
}
