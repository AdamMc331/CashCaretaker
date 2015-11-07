package com.androidessence.cashcaretaker.adapters;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * A template for the RecyclerView.Adapter class which uses a CursorAdapter.
 *
 * Created by adammcneilly on 10/31/15.
 */
public abstract class RecyclerViewCursorAdapter<T extends RecyclerViewCursorViewHolder> extends RecyclerView.Adapter<T> {
    /**
     * The Context of the adapter.
     */
    final Context mContext;

    /**
     * The CursorAdapter used to display data with.
     */
    CursorAdapter mCursorAdapter;

    /**
     * A temporary view used to pass the holder from the RecyclerView.Adapter into the CursorAdapter.
     */
    final View mTempView;

    RecyclerViewCursorAdapter(Context context){
        this.mContext = context;
        this.mTempView = new View(mContext);
    }

    /**
     * Swap the Cursor of the CursorAdapter and notify the RecyclerView.Adapter that data has changed.
     * @param cursor A Cursor representation of the data to be displayed.
     */
    public void swapCursor(Cursor cursor){
        this.mCursorAdapter.swapCursor(cursor);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return mCursorAdapter.getCount();
    }
}
