package com.androidessence.cashcaretaker.fragments;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.androidessence.cashcaretaker.R;
import com.androidessence.cashcaretaker.activities.AddRepeatingTransactionActivityR;
import com.androidessence.cashcaretaker.adapters.RepeatingTransactionAdapterR;
import com.androidessence.cashcaretaker.core.CoreRecyclerViewFragment;
import com.androidessence.cashcaretaker.data.CCContract;

/**
 * Displays a list of Repeating Transactions to the user.
 *
 * Created by adam.mcneilly on 9/8/16.
 */
public class RepeatingTransactionsFragmentR extends CoreRecyclerViewFragment implements LoaderManager.LoaderCallbacks<Cursor>{
    // UI Elements
    private RepeatingTransactionAdapterR mRepeatingTransactionAdapter;
    private FloatingActionButton mAddRepeatingTransactionFloatingActionButton;
    private TextView mAddFirstRepeatingTransaction;

    // Loader identifier for the CursorLoader
    private static final int REPEATING_TRANSACTION_LOADER = 0;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        root = (CoordinatorLayout) inflater.inflate(R.layout.fragment_repeating_transactions, container, false);

        getElements(root);
        setupRecyclerView(LinearLayoutManager.VERTICAL);
        setupFloatingActionButton();

        return root;
    }

    @Override
    protected void getElements(View view) {
        recyclerView = (RecyclerView) view.findViewById(R.id.repeating_transaction_recycler_view);
        mAddRepeatingTransactionFloatingActionButton = (FloatingActionButton) view.findViewById(R.id.add_repeating_transaction_fab);
        mAddFirstRepeatingTransaction = (TextView) view.findViewById(R.id.add_first_repeating_transaction_text_view);
    }

    @Override
    protected void setupRecyclerView(int orientation) {
        super.setupRecyclerView(orientation);

        mRepeatingTransactionAdapter = new RepeatingTransactionAdapterR(getActivity());
        recyclerView.setAdapter(mRepeatingTransactionAdapter);
    }

    /**
     * Sets up the FAB by attaching a click listener to start the AddRPTTransActivity
     */
    private void setupFloatingActionButton() {
        mAddRepeatingTransactionFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startAddRepeatingTransactionActivity();
            }
        });
    }

    /**
     * Starts the activity for adding a repeating transaction.
     */
    private void startAddRepeatingTransactionActivity() {
        Intent addRepeatingTransactionIntent = new Intent(getActivity(), AddRepeatingTransactionActivityR.class);
        startActivity(addRepeatingTransactionIntent);
    }

    /**
     * Start the CursorLoader when the fragment resumes.
     */
    @Override
    public void onResume() {
        super.onResume();
        getLoaderManager().restartLoader(REPEATING_TRANSACTION_LOADER, null, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        switch(id) {
            case REPEATING_TRANSACTION_LOADER:
                return new CursorLoader(
                        getActivity(),
                        CCContract.RepeatingTransactionEntry.CONTENT_DETAILS_URI,
                        RepeatingTransactionAdapterR.REPEATING_TRANSACTION_COLUMNS,
                        null,
                        null,
                        CCContract.RepeatingTransactionEntry.COLUMN_NEXT_DATE + " ASC"
                );
            default:
                throw new UnsupportedOperationException("Unknown loader id: " + id);
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        switch(loader.getId()) {
            case REPEATING_TRANSACTION_LOADER:
                mRepeatingTransactionAdapter.swapCursor(data);

                // If data was empty show text view. Otherwise, show recyclerview.
                recyclerView.setVisibility(data.getCount() == 0 ? View.GONE : View.VISIBLE);
                mAddFirstRepeatingTransaction.setVisibility(data.getCount() == 0 ? View.VISIBLE : View.GONE);
                break;
            default:
                throw new UnsupportedOperationException("Unknown loader id: " + loader.getId());
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        switch(loader.getId()) {
            case REPEATING_TRANSACTION_LOADER:
                mRepeatingTransactionAdapter.swapCursor(null);
                break;
            default:
                throw new UnsupportedOperationException("Unknown loader id: " + loader.getId());
        }
    }
}