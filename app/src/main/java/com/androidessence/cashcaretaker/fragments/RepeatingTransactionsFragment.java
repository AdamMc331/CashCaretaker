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
import com.androidessence.cashcaretaker.activities.AddRepeatingTransactionActivity;
import com.androidessence.cashcaretaker.adapters.RepeatingTransactionAdapter;
import com.androidessence.cashcaretaker.core.CoreRecyclerViewFragment;
import com.androidessence.cashcaretaker.data.CCContract;

/**
 * Displays a list of Repeating Transactions to the user.
 *
 * Created by adam.mcneilly on 9/8/16.
 */
public class RepeatingTransactionsFragment extends CoreRecyclerViewFragment implements LoaderManager.LoaderCallbacks<Cursor>{
    // UI Elements
    private RepeatingTransactionAdapter repeatingTransactionAdapter;
    private FloatingActionButton addRepeatingTransactionFAB;
    private TextView addFirstRepeatingTransaction;

    // Loader identifier for the CursorLoader
    private static final int REPEATING_TRANSACTION_LOADER = 0;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setRoot((CoordinatorLayout) inflater.inflate(R.layout.fragment_repeating_transactions, container, false));

        getElements(getRoot());
        setupRecyclerView(LinearLayoutManager.VERTICAL, false);
        setupFloatingActionButton();

        return getRoot();
    }

    @Override
    protected void getElements(View view) {
        setRecyclerView((RecyclerView) view.findViewById(R.id.repeating_transaction_recycler_view));
        addRepeatingTransactionFAB = (FloatingActionButton) view.findViewById(R.id.add_repeating_transaction_fab);
        addFirstRepeatingTransaction = (TextView) view.findViewById(R.id.add_first_repeating_transaction_text_view);
    }

    @Override
    protected void setupRecyclerView(int orientation, boolean reverseLayout) {
        super.setupRecyclerView(orientation, reverseLayout);

        repeatingTransactionAdapter = new RepeatingTransactionAdapter(getActivity());
        getRecyclerView().setAdapter(repeatingTransactionAdapter);
    }

    /**
     * Sets up the FAB by attaching a click listener to start the AddRPTTransActivity
     */
    private void setupFloatingActionButton() {
        addRepeatingTransactionFAB.setOnClickListener(new View.OnClickListener() {
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
        getLoaderManager().restartLoader(REPEATING_TRANSACTION_LOADER, null, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        switch(id) {
            case REPEATING_TRANSACTION_LOADER:
                return new CursorLoader(
                        getActivity(),
                        CCContract.RepeatingTransactionEntry.Companion.getCONTENT_DETAILS_URI(),
                        RepeatingTransactionAdapter.Companion.getREPEATING_TRANSACTION_COLUMNS(),
                        null,
                        null,
                        CCContract.RepeatingTransactionEntry.Companion.getCOLUMN_NEXT_DATE() + " ASC"
                );
            default:
                throw new UnsupportedOperationException("Unknown loader id: " + id);
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        switch(loader.getId()) {
            case REPEATING_TRANSACTION_LOADER:
                repeatingTransactionAdapter.swapCursor(data);

                // If data was empty show text view. Otherwise, show recyclerview.
                getRecyclerView().setVisibility(data.getCount() == 0 ? View.GONE : View.VISIBLE);
                addFirstRepeatingTransaction.setVisibility(data.getCount() == 0 ? View.VISIBLE : View.GONE);
                break;
            default:
                throw new UnsupportedOperationException("Unknown loader id: " + loader.getId());
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        switch(loader.getId()) {
            case REPEATING_TRANSACTION_LOADER:
                repeatingTransactionAdapter.swapCursor(null);
                break;
            default:
                throw new UnsupportedOperationException("Unknown loader id: " + loader.getId());
        }
    }
}