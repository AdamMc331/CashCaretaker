package com.androidessence.cashcaretaker.fragments;

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

import com.androidessence.cashcaretaker.DividerItemDecoration;
import com.androidessence.cashcaretaker.R;
import com.androidessence.cashcaretaker.adapters.TransactionAdapter;
import com.androidessence.cashcaretaker.data.CCContract;

/**
 * Created by adammcneilly on 11/1/15.
 */
public class TransactionsFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>{
    private RecyclerView mTransactionRecyclerView;
    private FloatingActionButton mAddTransactionFAB;
    private TransactionAdapter mTransactionAdapter;

    private long mAccount;
    private static final String ARG_ACCOUNT = "accountArg";
    private static final int TRANSACTION_LOADER = 0;

    public static TransactionsFragment NewInstance(long account){
        Bundle args = new Bundle();
        args.putLong(ARG_ACCOUNT, account);

        TransactionsFragment fragment = new TransactionsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAccount = getArguments().getLong(ARG_ACCOUNT, 0);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_transactions, container, false);

        getUIElements(view);
        setupRecyclerView();
        setClickListeners();

        return view;
    }

    private void setupRecyclerView(){
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mTransactionRecyclerView.setLayoutManager(linearLayoutManager);

        mTransactionRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST));

        mTransactionAdapter = new TransactionAdapter(getActivity());
        mTransactionRecyclerView.setAdapter(mTransactionAdapter);
    }

    private void getUIElements(View view){
        mTransactionRecyclerView = (RecyclerView) view.findViewById(R.id.transaction_recycler_view);
        mAddTransactionFAB = (FloatingActionButton) view.findViewById(R.id.add_transaction_fab);
    }

    private void setClickListeners(){
        mAddTransactionFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((OnAddTransactionFABClickedListener)getActivity()).addTransactionFABClicked();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        getLoaderManager().restartLoader(TRANSACTION_LOADER, null, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        switch(id){
            case TRANSACTION_LOADER:
                return new CursorLoader(
                        getActivity(),
                        CCContract.TransactionEntry.buildTransactionsForAccountUri(mAccount),
                        TransactionAdapter.TRANSACTION_COLUMNS,
                        null,
                        null,
                        CCContract.TransactionEntry.COLUMN_DATE + " DESC"
                );
            default:
                throw new UnsupportedOperationException("Unknown loader id: " + id);
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        switch(loader.getId()){
            case TRANSACTION_LOADER:
                mTransactionAdapter.swapCursor(data);
                break;
            default:
                throw new UnsupportedOperationException("Unknown loader id: " + loader.getId());
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        switch(loader.getId()){
            case TRANSACTION_LOADER:
                mTransactionAdapter.swapCursor(null);
                break;
            default:
                throw new UnsupportedOperationException("Unknown loader id: " + loader.getId());
        }
    }

    public interface OnAddTransactionFABClickedListener {
        void addTransactionFABClicked();
    }
}
