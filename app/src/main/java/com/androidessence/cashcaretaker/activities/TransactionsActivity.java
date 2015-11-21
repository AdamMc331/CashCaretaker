package com.androidessence.cashcaretaker.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.androidessence.cashcaretaker.R;
import com.androidessence.cashcaretaker.dataTransferObjects.Account;
import com.androidessence.cashcaretaker.fragments.AccountTransactionsFragment;
import com.androidessence.cashcaretaker.fragments.TransactionFragment;

/**
 * Context for both displaying a list of Transactions for an Account and allowing the user to add a transaction for that account.
 */
public class TransactionsActivity extends AppCompatActivity implements AccountTransactionsFragment.OnAddTransactionFABClickedListener, TransactionFragment.OnTransactionSubmittedListener {
    /**
     * An argument representing the account to show transactions for, or to add a transaction to.
     */
    public static final String ARG_ACCOUNT = "accountArg";

    /**
     * The current view state of the Activity. Either showing a list of transactions, or the controls to add a transaction.
     */
    private int mState;

    /**
     * A flag representing the view state for a list of transactions.
     */
    private static final int STATE_TRANSACTIONS = 0;

    /**
     * A flag representing the view state for adding a transaction.
     */
    private static final int STATE_ADD_TRANSACTION = 1;

    /**
     * An argument used for saving the view state of the context.
     */
    private static final String ARG_STATE = "stateArg";

    private static final String ADD_TRANSACTION_FRAGMENT_TAG = "addTransactionFragment";

    private static final String TRANSACTIONS_FRAGMENT_TAG = "transactionsFragmentTag";

    /**
     * The account we are viewing/adding transactions for.
     */
    private Account mAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transactions);

        // Set toolbar, allow going back.
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        assert getSupportActionBar() != null;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Read arguments.
        mState = savedInstanceState != null ? savedInstanceState.getInt(ARG_STATE, STATE_TRANSACTIONS) : STATE_TRANSACTIONS;
        mAccount = getIntent().getParcelableExtra(ARG_ACCOUNT);

        /**
         * Switch based on the view state. Show the fragment if it is null, otherwise display the same fragment.
         */
        switch (mState) {
            case STATE_TRANSACTIONS:
                /*
      The current AccountTransactionsFragment being shown.
     */
                AccountTransactionsFragment accountTransactionsFragment = (AccountTransactionsFragment) getSupportFragmentManager().findFragmentByTag(TRANSACTIONS_FRAGMENT_TAG);
                if(accountTransactionsFragment == null){
                    showTransactionsFragment();
                }
                break;
            case STATE_ADD_TRANSACTION:
                // If fragment exists already, don't recreate it
                /*
      The current TransactionFragment being shown.
     */
                TransactionFragment transactionFragment = (TransactionFragment) getSupportFragmentManager().findFragmentByTag(ADD_TRANSACTION_FRAGMENT_TAG);
                if (transactionFragment == null) {
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

        // Store view state.
        outState.putInt(ARG_STATE, mState);
    }

    /**
     * Displays the fragment used for adding a transaction.
     */
    private void showAddTransactionFragment() {
        // Display fragment
        TransactionFragment transactionFragment = TransactionFragment.NewInstance(mAccount.getIdentifier());
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_transactions, transactionFragment, ADD_TRANSACTION_FRAGMENT_TAG).commit();

        // Set title
        assert getSupportActionBar() != null;
        getSupportActionBar().setTitle(getString(R.string.add_transaction));

        // Set state
        mState = STATE_ADD_TRANSACTION;
    }

    /**
     * Displays a fragment with a list of transactions for the current account.
     */
    private void showTransactionsFragment() {
        // Display fragment.
        AccountTransactionsFragment accountTransactionsFragment = AccountTransactionsFragment.NewInstance(mAccount.getIdentifier());
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_transactions, accountTransactionsFragment).commit();

        // Set title
        assert getSupportActionBar() != null;
        getSupportActionBar().setTitle(mAccount.getName() + " Transactions");

        // Set state
        mState = STATE_TRANSACTIONS;
    }

    /**
     * Interface implementation that handles when the FAB is clicked inside the AccountTransactionsFragment.
     */
    @Override
    public void addTransactionFABClicked() {
        showAddTransactionFragment();
    }

    /**
     * Override onOptionsItemSelected because we will take different actions depending on which fragment is being shown.
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // If we are in STATE_TRANSACTIONS, finish to go back to accounts
                if (mState == STATE_TRANSACTIONS) {
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

    /**
     * Interface implementation that handles when a transaction is submitted so the `AccountTransactionsFragment` is displayed again.
     */
    @Override
    public void onTransactionSubmitted() {
        // Show transactions again
        showTransactionsFragment();
    }
}
