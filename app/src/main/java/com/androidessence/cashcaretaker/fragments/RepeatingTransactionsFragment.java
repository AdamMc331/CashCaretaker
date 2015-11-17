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
import com.androidessence.cashcaretaker.activities.AddRepeatingTransactionActivity;
import com.androidessence.cashcaretaker.adapters.RepeatingTransactionAdapter;
import com.androidessence.cashcaretaker.data.CCContract;

/**
 * Fragment used to display a list of repeating transactions.
 *
 * Created by adammcneilly on 11/17/15.
 */
public class RepeatingTransactionsFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>{
    // UI Elements
    private RecyclerView mRepeatingTransactionRecyclerView;
    private RepeatingTransactionAdapter mRepeatingTransactionAdapter;
    private FloatingActionButton mAddRepeatingTransactionFloatingActionButton;
    private TextView mAddFirstRepeatingTransaction;

    // Loader identifier for the CursorLoader
    private static final int REPEATING_TRANSACTION_LOADER = 0;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_repeating_transactions, container, false);

        getUIElements(view);
        setupRecyclerView();
        setupFloatingActionButton();

        return view;
    }

    /**
     * Retrieves all the necessary UI elements for this fragment.
     * @param view The parent view of the fragment.
     */
    private void getUIElements(View view) {
        mRepeatingTransactionRecyclerView = (RecyclerView) view.findViewById(R.id.repeating_transaction_recycler_view);
        mAddRepeatingTransactionFloatingActionButton = (FloatingActionButton) view.findViewById(R.id.add_repeating_transaction_fab);
        mAddFirstRepeatingTransaction = (TextView) view.findViewById(R.id.add_first_repeating_transaction_text_view);
    }

    /**
     * Prepares the RecyclerView of the fragment by setting the LayoutManager, ItemDecorator, and Adapter.
     */
    private void setupRecyclerView() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRepeatingTransactionRecyclerView.setLayoutManager(linearLayoutManager);

        mRepeatingTransactionRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST));

        mRepeatingTransactionAdapter = new RepeatingTransactionAdapter(getActivity());
        mRepeatingTransactionRecyclerView.setAdapter(mRepeatingTransactionAdapter);
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
        Intent addRepeatingTransactionIntent = new Intent(getActivity(), AddRepeatingTransactionActivity.class);
        startActivity(addRepeatingTransactionIntent);
    }

    /**
     * Start the CursorLoader when the fragment resumes.
     */
    @Override
    public void onResume() {
        super.onResume();
        getLoaderManager().initLoader(REPEATING_TRANSACTION_LOADER, null, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        switch(id) {
            case REPEATING_TRANSACTION_LOADER:
                return new CursorLoader(
                        getActivity(),
                        CCContract.RepeatingTransactionEntry.CONTENT_DETAILS_URI,
                        RepeatingTransactionAdapter.REPEATING_TRANSACTION_COLUMNS,
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
        switch(loader.getId()) {
            case REPEATING_TRANSACTION_LOADER:
                mRepeatingTransactionAdapter.swapCursor(data);

                // If data was empty show text view. Otherwise, show recyclerview.
                mRepeatingTransactionRecyclerView.setVisibility(data.getCount() == 0 ? View.GONE : View.VISIBLE);
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
