package com.androidessence.cashcaretaker.fragments;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.androidessence.cashcaretaker.DividerItemDecoration;
import com.androidessence.cashcaretaker.R;
import com.androidessence.cashcaretaker.activities.AddAccountActivity;
import com.androidessence.cashcaretaker.adapters.AccountAdapter;
import com.androidessence.cashcaretaker.data.CCContract;

/**
 * Fragment used to display a list of Accounts.
 *
 * Created by adammcneilly on 11/1/15.
 */
public class AccountsFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>{
    // UI Elements
    private RecyclerView mAccountRecyclerView;
    private AccountAdapter mAccountAdapter;
    private FloatingActionButton mFloatingActionButton;
    private TextView mAddFirstAccount;

    // Loader identifier for the CursorLoader.
    private static final int ACCOUNT_LOADER = 0;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_accounts, container, false);

        getUIElements(view);
        setupRecyclerView();
        setupFloatingActionButton();

        return view;
    }

    /**
     * Retrieves all necessary elements for the Fragment.
     */
    private void getUIElements(View view){
        mAccountRecyclerView = (RecyclerView) view.findViewById(R.id.account_recycler_view);
        mFloatingActionButton = (FloatingActionButton) view.findViewById(R.id.add_account_fab);
        mAddFirstAccount = (TextView) view.findViewById(R.id.add_first_account_text_view);
    }

    /**
     * Prepares the RecyclerView of the fragment by setting the LayoutManager, ItemDecorator, and Adapter.
     */
    private void setupRecyclerView(){
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mAccountRecyclerView.setLayoutManager(linearLayoutManager);

        mAccountRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST));

        mAccountAdapter = new AccountAdapter(getActivity());
        mAccountRecyclerView.setAdapter(mAccountAdapter);
    }

    /**
     * Prepares the FloatingActionButton of the fragment by setting its click listener.
     */
    private void setupFloatingActionButton(){
        mFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startAddAccountActivity();
            }
        });
    }

    /**
     * Starts the Activity for adding a new account.
     */
    private void startAddAccountActivity(){
        Intent addAccount = new Intent(getActivity(), AddAccountActivity.class);
        startActivity(addAccount);
    }

    /**
     * Restart the CursorLoader when the fragment resumes.
     */
    @Override
    public void onResume() {
        super.onResume();
        getLoaderManager().restartLoader(ACCOUNT_LOADER, null, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        switch(id){
            case ACCOUNT_LOADER:
                return new CursorLoader(
                        getActivity(),
                        CCContract.AccountEntry.CONTENT_URI,
                        AccountAdapter.ACCOUNT_COLUMNS,
                        null,
                        null,
                        CCContract.AccountEntry.COLUMN_NAME
                );
            default:
                throw new UnsupportedOperationException("Unknown loader id: " + id);
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        switch(loader.getId()){
            case ACCOUNT_LOADER:
                mAccountAdapter.swapCursor(data);
                // If we have no data, hide the recyclerview and show the text view
                mAccountRecyclerView.setVisibility(data.getCount() == 0 ? View.GONE : View.VISIBLE);
                mAddFirstAccount.setVisibility(data.getCount() == 0 ? View.VISIBLE : View.GONE);
                break;
            default:
                throw new UnsupportedOperationException("Unknown loader id: " + loader.getId());
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        switch(loader.getId()){
            case ACCOUNT_LOADER:
                mAccountAdapter.swapCursor(null);
                break;
            default:
                throw new UnsupportedOperationException("Unknown loader id: " + loader.getId());
        }
    }
}
