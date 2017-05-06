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
import com.androidessence.cashcaretaker.adapters.SimpleAccountAdapter;
import com.androidessence.cashcaretaker.data.CCContract;
import com.androidessence.cashcaretaker.dataTransferObjects.Account;

/**
 * Dialog that displays a list of account names for the user to select.
 *
 * Created by adammcneilly on 11/17/15.
 */
public class AccountDialog extends DialogFragment implements LoaderManager.LoaderCallbacks<Cursor>{
    private SimpleAccountAdapter simpleAccountAdapter;

    private static final int CATEGORY_LOADER = 0;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_account, container, false);

        final ListView listView = (ListView) view.findViewById(R.id.account_list_view);
        simpleAccountAdapter = new SimpleAccountAdapter(getActivity());
        listView.setAdapter(simpleAccountAdapter);

        getDialog().setTitle("Account");

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Cursor item = (Cursor) listView.getItemAtPosition(position);
                ((OnAccountSelectedListener)getTargetFragment()).onAccountSelected(new Account(item));
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
                        CCContract.AccountEntry.CONTENT_URI,
                        SimpleAccountAdapter.Companion.getACCOUNT_COLUMNS(),
                        null,
                        null,
                        CCContract.AccountEntry.COLUMN_NAME + " ASC"
                );
            default:
                throw new UnsupportedOperationException("Unknown loader id: " + id);
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        switch(loader.getId()){
            case CATEGORY_LOADER:
                simpleAccountAdapter.swapCursor(data);
                break;
            default:
                throw new UnsupportedOperationException("Unknown loader id: " + loader.getId());
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        switch(loader.getId()){
            case CATEGORY_LOADER:
                simpleAccountAdapter.swapCursor(null);
                break;
            default:
                throw new UnsupportedOperationException("Unknown loader id: " + loader.getId());
        }
    }

    public interface OnAccountSelectedListener {
        void onAccountSelected(Account account);
    }
}
