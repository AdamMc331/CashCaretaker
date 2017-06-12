package com.androidessence.cashcaretaker.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.androidessence.cashcaretaker.R;
import com.androidessence.cashcaretaker.activities.AddAccountActivity;
import com.androidessence.cashcaretaker.adapters.AccountAdapter_R;
import com.androidessence.cashcaretaker.core.CoreRecyclerViewFragment;
import com.androidessence.cashcaretaker.data.CCDataSource;
import com.androidessence.cashcaretaker.dataTransferObjects.Account;

import java.sql.SQLException;
import java.util.List;

import timber.log.Timber;

/**
 * Fragment that displays a list of accounts to the user.
 *
 * Created by adam.mcneilly on 9/8/16.
 */
public class AccountsFragment extends CoreRecyclerViewFragment {
    // UI Elements
    private AccountAdapter_R accountAdapter;
    private FloatingActionButton floatingActionButton;
    private TextView addFirstAccount;

    // Loader identifier for the CursorLoader.
    private static final int ACCOUNT_LOADER = 0;

    public static AccountsFragment newInstance() {
        Bundle args = new Bundle();

        AccountsFragment fragment = new AccountsFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setRoot((CoordinatorLayout) inflater.inflate(R.layout.fragment_accounts, container, false));

        getElements(getRoot());
        setupRecyclerView(LinearLayoutManager.VERTICAL, false);
        setupFloatingActionButton();

        return getRoot();
    }

    @Override
    protected void getElements(View view){
        setRecyclerView((RecyclerView) view.findViewById(R.id.account_recycler_view));
        floatingActionButton = (FloatingActionButton) view.findViewById(R.id.add_account_fab);
        addFirstAccount = (TextView) view.findViewById(R.id.add_first_account_text_view);
    }

    @Override
    protected void setupRecyclerView(int orientation, boolean reverseLayout) {
        super.setupRecyclerView(orientation, reverseLayout);

        CCDataSource dataSource = new CCDataSource(getContext());
        try {
            dataSource.open();
            List<Account> accounts = dataSource.getAccounts(null, null, null, null, null, null);
            dataSource.close();
            accountAdapter = new AccountAdapter_R(accounts);
            getRecyclerView().setAdapter(accountAdapter);
        } catch (SQLException se) {
            Timber.e(se);
        }
    }

    /**
     * Prepares the FloatingActionButton of the fragment by setting its click listener.
     */
    private void setupFloatingActionButton(){
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
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
}
