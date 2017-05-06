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
import com.androidessence.cashcaretaker.adapters.RepeatingPeriodAdapter;
import com.androidessence.cashcaretaker.data.CCContract;
import com.androidessence.cashcaretaker.dataTransferObjects.RepeatingPeriod;

/**
 * Dialog that displays a list of repeating periods for the user to select.
 *
 * Created by adammcneilly on 11/17/15.
 */
public class RepeatingPeriodDialog extends DialogFragment implements LoaderManager.LoaderCallbacks<Cursor>{
    private RepeatingPeriodAdapter repeatingPeriodAdapter;

    private static final int REPEATING_PERIOD_LOADER = 0;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_category, container, false);

        final ListView listView = (ListView) view.findViewById(R.id.category_list_view);
        repeatingPeriodAdapter = new RepeatingPeriodAdapter(getActivity());
        listView.setAdapter(repeatingPeriodAdapter);

        getDialog().setTitle("Repeating Period");

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Cursor item = (Cursor) listView.getItemAtPosition(position);
                ((OnRepeatingPeriodSelectedListener)getTargetFragment()).onRepeatingPeriodSelected(new RepeatingPeriod(item));
                dismiss();
            }
        });

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getLoaderManager().initLoader(REPEATING_PERIOD_LOADER, null, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        switch(id){
            case REPEATING_PERIOD_LOADER:
                return new CursorLoader(
                        getActivity(),
                        CCContract.RepeatingPeriodEntry.Companion.getCONTENT_URI(),
                        RepeatingPeriodAdapter.Companion.getREPEATING_PERIOD_COLUMNS(),
                        null,
                        null,
                        null
                );
            default:
                throw new UnsupportedOperationException("Unknown loader id: " + id);
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        switch(loader.getId()){
            case REPEATING_PERIOD_LOADER:
                repeatingPeriodAdapter.swapCursor(data);
                break;
            default:
                throw new UnsupportedOperationException("Unknown loader id: " + loader.getId());
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        switch(loader.getId()){
            case REPEATING_PERIOD_LOADER:
                repeatingPeriodAdapter.swapCursor(null);
                break;
            default:
                throw new UnsupportedOperationException("Unknown loader id: " + loader.getId());
        }
    }

    public interface OnRepeatingPeriodSelectedListener {
        void onRepeatingPeriodSelected(RepeatingPeriod repeatingPeriod);
    }
}
