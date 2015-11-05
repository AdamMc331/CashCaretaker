package com.androidessence.cashcaretaker.activities;

import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.androidessence.cashcaretaker.R;
import com.androidessence.cashcaretaker.dataTransferObjects.Account;
import com.androidessence.cashcaretaker.fragments.AddTransactionFragment;
import com.androidessence.cashcaretaker.fragments.TransactionsFragment;

import org.joda.time.LocalDate;

public class TransactionsActivity extends AppCompatActivity implements TransactionsFragment.OnAddTransactionFABClickedListener, AddTransactionFragment.OnTransactionSubmittedListener{
    public static final String ARG_ACCOUNT = "accountArg";

    private int mState;

    private static final int STATE_TRANSACTIONS = 0;
    private static final int STATE_ADD_TRANSACTION = 1;
    private static final String ARG_STATE = "stateArg";

    private AddTransactionFragment addTransactionFragment;
    private TransactionsFragment transactionsFragment;
    private static final String ADD_TRANSACTION_FRAGMENT_TAG = "addTransactionFragment";
    private static final String TRANSACTIONS_FRAGMENT_TAG = "transactionsFragmentTag";

    private Account mAccount;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transactions);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        assert getSupportActionBar() != null;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mState = savedInstanceState != null ? savedInstanceState.getInt(ARG_STATE, STATE_TRANSACTIONS) : STATE_TRANSACTIONS;
        mAccount = getIntent().getParcelableExtra(ARG_ACCOUNT);

        switch(mState){
            case STATE_TRANSACTIONS:
                showTransactionsFragment();
                break;
            case STATE_ADD_TRANSACTION:
                // If fragment exists already, don't recreate it
                addTransactionFragment = (AddTransactionFragment) getSupportFragmentManager().findFragmentByTag(ADD_TRANSACTION_FRAGMENT_TAG);
                if(addTransactionFragment == null){
                    showAddTransactionFragment();
                }
                break;
            default:
                throw new UnsupportedOperationException("Unknown transaction state: " + mState);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(ARG_STATE, mState);
    }

    private void showAddTransactionFragment(){
        AddTransactionFragment addTransactionFragment = AddTransactionFragment.NewInstance(mAccount.getIdentifier());
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_transactions, addTransactionFragment, ADD_TRANSACTION_FRAGMENT_TAG).commit();
        assert getSupportActionBar() != null;
        getSupportActionBar().setTitle(getString(R.string.add_transaction));
        mState = STATE_ADD_TRANSACTION;
    }

    private void showTransactionsFragment(){
        TransactionsFragment transactionsFragment = TransactionsFragment.NewInstance(mAccount.getIdentifier());
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_transactions, transactionsFragment).commit();
        assert getSupportActionBar() != null;
        getSupportActionBar().setTitle(mAccount.getName() + " Transactions");
        mState = STATE_TRANSACTIONS;
    }

    @Override
    public void addTransactionFABClicked() {
        showAddTransactionFragment();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case android.R.id.home:
                // If we are in STATE_TRANSACTIONS, finish to go back to accounts
                if(mState == STATE_TRANSACTIONS) {
                    finish();
                } else {
                    // If we are in STATE_ADD_TRANSACTION, show the transactions fragment again
                    showTransactionsFragment();
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onTransactionSubmitted() {
        // Show transactions again
        showTransactionsFragment();
    }
}
