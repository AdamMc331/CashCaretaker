package com.androidessence.cashcaretaker.fragments;

import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.androidessence.cashcaretaker.R;
import com.androidessence.cashcaretaker.adapters.CategoryAdapter;
import com.androidessence.cashcaretaker.data.CCContract;
import com.androidessence.cashcaretaker.dataTransferObjects.Category;

/**
 * Dialog that displays a list of transaction categories for the user to select.
 *
 * Created by adammcneilly on 10/16/15.
 */
public class CategoryDialog extends DialogFragment implements LoaderManager.LoaderCallbacks<Cursor>{
    private CategoryAdapter categoryAdapter;

    private static final int CATEGORY_LOADER = 0;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_category, container, false);

        final ListView listView = (ListView) view.findViewById(R.id.category_list_view);
        categoryAdapter = new CategoryAdapter(getActivity());
        listView.setAdapter(categoryAdapter);

        getDialog().setTitle("Category");

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Cursor item = (Cursor) listView.getItemAtPosition(position);
                ((OnCategorySelectedListener)getTargetFragment()).onCategorySelected(new Category(item));
                dismiss();
            }
        });

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getLoaderManager().initLoader(CATEGORY_LOADER, null, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        switch(id){
            case CATEGORY_LOADER:
                return new CursorLoader(
                        getActivity(),
                        CCContract.CategoryEntry.CONTENT_URI,
                        null,
                        null,
                        null,
                        CCContract.CategoryEntry.COLUMN_DESCRIPTION + " ASC"
                );
            default:
                throw new UnsupportedOperationException("Unknown loader id: " + id);
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        switch(loader.getId()){
            case CATEGORY_LOADER:
                categoryAdapter.swapCursor(data);
                break;
            default:
                throw new UnsupportedOperationException("Unknown loader id: " + loader.getId());
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        switch(loader.getId()){
            case CATEGORY_LOADER:
                categoryAdapter.swapCursor(null);
                break;
            default:
                throw new UnsupportedOperationException("Unknown loader id: " + loader.getId());
        }
    }

    public interface OnCategorySelectedListener{
        void onCategorySelected(Category category);
    }
}
