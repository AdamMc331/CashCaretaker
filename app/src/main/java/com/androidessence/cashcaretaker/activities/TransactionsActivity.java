//package com.androidessence.cashcaretaker.activities;
//
//import android.content.DialogInterface;
//import android.os.Build;
//import android.os.Bundle;
//import android.support.design.widget.AppBarLayout;
//import android.support.v7.app.AlertDialog;
//import android.support.v7.app.AppCompatActivity;
//import android.support.v7.view.ActionMode;
//import android.support.v7.widget.Toolbar;
//import android.view.Menu;
//import android.view.MenuInflater;
//import android.view.MenuItem;
//
//import com.androidessence.cashcaretaker.R;
//import com.androidessence.cashcaretaker.adapters.TransactionAdapter;
//import com.androidessence.cashcaretaker.data.CCContract;
//import com.androidessence.cashcaretaker.dataTransferObjects.Account;
//import com.androidessence.cashcaretaker.dataTransferObjects.Transaction;
//import com.androidessence.cashcaretaker.dataTransferObjects.TransactionDetails;
//import com.androidessence.cashcaretaker.fragments.AccountTransactionsFragment;
//import com.androidessence.cashcaretaker.fragments.TransactionFragment;
//
///**
// * Context for both displaying a list of Transactions for an Account and allowing the user to add a transaction for that account.
// */
//public class TransactionsActivity extends AppCompatActivity implements AccountTransactionsFragment.OnAddTransactionFABClickedListener, TransactionFragment.OnTransactionSubmittedListener, TransactionAdapter.OnTransactionLongClickListener{
//    private AppBarLayout mAppBar;
//
//    /**
//     * An argument representing the account to show transactions for, or to add a transaction to.
//     */
//    public static final String ARG_ACCOUNT = "accountArg";
//
//    /**
//     * The current view state of the Activity. Either showing a list of transactions, or the controls to add a transaction.
//     */
//    private int mState;
//
//    /**
//     * A flag representing the view state for a list of transactions.
//     */
//    private static final int STATE_TRANSACTIONS = 0;
//
//    /**
//     * A flag representing the view state for adding a transaction.
//     */
//    private static final int STATE_ADD_TRANSACTION = 1;
//
//    /**
//     * A flag representing the view state for editing a transaction.
//     */
//    private static final int STATE_EDIT_TRANSACTION = 2;
//
//    /**
//     * An argument used for saving the view state of the context.
//     */
//    private static final String ARG_STATE = "stateArg";
//
//    private static final String ADD_TRANSACTION_FRAGMENT_TAG = "addTransactionFragment";
//
//    private static final String TRANSACTIONS_FRAGMENT_TAG = "transactionsFragmentTag";
//
//    private static final String EDIT_TRANSACTION_FRAGMENT_TAG = "editTransactionFragment";
//
//    // Fragments
//    private TransactionFragment mTransactionFragment;
//    private AccountTransactionsFragment mAccountTransactionsFragment;
//
//    /**
//     * The account we are viewing/adding transactions for.
//     */
//    private Account mAccount;
//
//    /**
//     * The ActionMode to be displayed for deleting a Transaction.
//     */
//    private ActionMode mActionMode;
//
//    /**
//     * An Action mode callback used for the context menu.
//     */
//    private final ActionMode.Callback mActionModeCallback = new ActionMode.Callback() {
//        // Called when the action mode is created; startActionMode() was called
//        @Override
//        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
//            // Inflate a menu resource providing context menu items
//            MenuInflater inflater = mode.getMenuInflater();
//            inflater.inflate(R.menu.transaction_context_menu, menu);
//            return true;
//        }
//
//        // Called each time the action mode is shown. Always called after onCreateActionMode, but
//        // may be called multiple times if the mode is invalidated.
//        @Override
//        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
//            return false; // Return false if nothing is done
//        }
//
//        // Called when the user selects a contextual menu item
//        @Override
//        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
//            switch (item.getItemId()) {
//                case R.id.action_delete_transaction:
//                    // The transaction that was selected is passed as the tag
//                    // for the action mode.
//                    showDeleteAlertDialog((Transaction) mActionMode.getTag());
//                    mode.finish(); // Action picked, so close the CAB
//                    return true;
//                case R.id.action_edit_transaction:
//                    // Edit the transaction selected. Close CAB when done.
//                    showEditTransactionFragment((TransactionDetails) mActionMode.getTag());
//                    mode.finish();
//                    return true;
//                default:
//                    return false;
//            }
//        }
//
//        // Called when the user exits the action mode
//        @Override
//        public void onDestroyActionMode(ActionMode mode) {
//            mActionMode = null;
//        }
//    };
//
//    /**
//     * Alerts the user that they are about to delete a transaction and ensures that they
//     * are okay with it.
//     *
//     * Returns true if the transaction was deleted, false otherwise.
//     */
//    private void showDeleteAlertDialog(final Transaction transaction){
//        final AlertDialog alertDialog = new AlertDialog.Builder(this).create();
//        alertDialog.setTitle("Delete Transaction");
//        alertDialog.setMessage("Are you sure you want to delete " + transaction.getDescription() + "?");
//        alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "Yes",
//                new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        //TODO: Handle update
//                        // Remove
//                        getContentResolver().delete(
//                                CCContract.TransactionEntry.CONTENT_URI,
//                                CCContract.TransactionEntry._ID + " = ?",
//                                new String[]{String.valueOf(transaction.getIdentifier())}
//                        );
//                        // Restart loader in fragment
//                        mAccountTransactionsFragment.restartAccountBalanceLoader();
//                        alertDialog.dismiss();
//                    }
//                });
//        alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "No", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                alertDialog.dismiss();
//            }
//        });
//        alertDialog.show();
//    }
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_transactions);
//
//        // Get appbar
//        mAppBar = (AppBarLayout) findViewById(R.id.appbar);
//
//        // Set toolbar, allow going back.
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//        assert getSupportActionBar() != null;
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//
//        // Read arguments.
//        mState = savedInstanceState != null ? savedInstanceState.getInt(ARG_STATE, STATE_TRANSACTIONS) : STATE_TRANSACTIONS;
//        mAccount = getIntent().getParcelableExtra(ARG_ACCOUNT);
//
//        /**
//         * Switch based on the view state. Show the fragment if it is null, otherwise display the same fragment.
//         */
//        switch (mState) {
//            case STATE_TRANSACTIONS:
//                // When showing transactions we also show account balance, so remove elevation
//                // on appbar. Requires API 21
//                setAppBarElevation(false);
//                /*
//                The current AccountTransactionsFragment being shown.
//                */
//                AccountTransactionsFragment accountTransactionsFragment = (AccountTransactionsFragment) getSupportFragmentManager().findFragmentByTag(TRANSACTIONS_FRAGMENT_TAG);
//                if(accountTransactionsFragment == null){
//                    showTransactionsFragment();
//                }
//                break;
//            case STATE_ADD_TRANSACTION:
//                // When showing transactions we also show account balance, so make sure elevation
//                // appears now. Requires API 21.
//                setAppBarElevation(true);
//                // If fragment exists already, don't recreate it
//                /*
//                The current TransactionFragment being shown.
//                */
//                TransactionFragment transactionFragment = (TransactionFragment) getSupportFragmentManager().findFragmentByTag(ADD_TRANSACTION_FRAGMENT_TAG);
//                if (transactionFragment == null) {
//                    showAddTransactionFragment();
//                }
//                break;
//            case STATE_EDIT_TRANSACTION:
//                // When showing transactions we also show account balance, so make sure elevation
//                // appears now. Requires API 21.
//                setAppBarElevation(true);
//                // If fragment exists already, don't recreate it
//                /*
//                The current TransactionFragment being shown.
//                */
//                TransactionFragment editTransactionFragment = (TransactionFragment) getSupportFragmentManager().findFragmentByTag(EDIT_TRANSACTION_FRAGMENT_TAG);
//                if (editTransactionFragment == null) {
//                    showAddTransactionFragment();
//                }
//                break;
//            default:
//                throw new UnsupportedOperationException("Unknown transaction state: " + mState);
//        }
//    }
//
//    @Override
//    protected void onSaveInstanceState(Bundle outState) {
//        super.onSaveInstanceState(outState);
//
//        // Store view state.
//        outState.putInt(ARG_STATE, mState);
//    }
//
//    /**
//     * Displays the fragment used for adding a transaction.
//     */
//    private void showAddTransactionFragment() {
//        // Display fragment
//        mTransactionFragment = TransactionFragment.NewInstance(mAccount.getIdentifier(), TransactionFragment.MODE_ADD, null);
//        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_transactions, mTransactionFragment, ADD_TRANSACTION_FRAGMENT_TAG).commit();
//
//        // Set title
//        assert getSupportActionBar() != null;
//        getSupportActionBar().setTitle(getString(R.string.add_transaction));
//
//        // Set state
//        mState = STATE_ADD_TRANSACTION;
//
//        // When showing transactions we also show account balance, so show elevation
//        // on appbar.
//        setAppBarElevation(true);
//    }
//
//    /**
//     * Displays the fragment used for editing a transaction.
//     */
//    private void showEditTransactionFragment(TransactionDetails transaction) {
//        mTransactionFragment = TransactionFragment.NewInstance(mAccount.getIdentifier(), TransactionFragment.MODE_EDIT, transaction);
//        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_transactions, mTransactionFragment, EDIT_TRANSACTION_FRAGMENT_TAG).commit();
//
//        // Set title
//        assert getSupportActionBar() != null;
//        getSupportActionBar().setTitle(getString(R.string.edit_transaction));
//
//        // Set state
//        mState = STATE_EDIT_TRANSACTION;
//
//        // When showing transactions we also show account balance, so show elevation
//        // on appbar.
//        setAppBarElevation(true);
//    }
//
//    /**
//     * Displays a fragment with a list of transactions for the current account.
//     */
//    private void showTransactionsFragment() {
//        // Display fragment.
//        mAccountTransactionsFragment = AccountTransactionsFragment.NewInstance(mAccount.getIdentifier());
//        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_transactions, mAccountTransactionsFragment).commit();
//
//        // Set title
//        assert getSupportActionBar() != null;
//        getSupportActionBar().setTitle(mAccount.getName() + " Transactions");
//
//        // Set state
//        mState = STATE_TRANSACTIONS;
//
//        // When showing transactions we also show account balance, so remove elevation
//        // on appbar. Requires API 21
//        setAppBarElevation(false);
//    }
//
//    /**
//     * Interface implementation that handles when the FAB is clicked inside the AccountTransactionsFragment.
//     */
//    @Override
//    public void addTransactionFABClicked() {
//        // Close action mode if necessary
//        if(mActionMode != null) {
//            mActionMode.finish();
//        }
//        showAddTransactionFragment();
//    }
//
//    /**
//     * Override onOptionsItemSelected because we will take different actions depending on which fragment is being shown.
//     */
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()) {
//            case android.R.id.home:
//                // If we are in STATE_TRANSACTIONS, finish to go back to accounts
//                if (mState == STATE_TRANSACTIONS) {
//                    finish();
//                } else {
//                    // If we are in STATE_ADD_TRANSACTION, show the transactions fragment again
//                    showTransactionsFragment();
//                }
//                return true;
//            default:
//                return super.onOptionsItemSelected(item);
//        }
//    }
//
//    /**
//     * Interface implementation that handles when a transaction is submitted so the `AccountTransactionsFragment` is displayed again.
//     */
//    @Override
//    public void onTransactionSubmitted() {
//        // Show transactions again
//        showTransactionsFragment();
//    }
//
//    /**
//     * Displays the action menu when a transaction is long clicked in the adapter.
//     * @param transaction The transaction that was long clicked.
//     */
//    @Override
//    public void onTransactionLongClick(TransactionDetails transaction) {
//        startActionMode(transaction);
//    }
//
//    private void startActionMode(TransactionDetails transaction){
//        // Don't fire if action mode is already being used
//        if(mActionMode == null){
//            // Start the CAB using the ActionMode.Callback already defined
//            mActionMode = startSupportActionMode(mActionModeCallback);
//            // Get name to set as title for action bar
//            // Need to subtract one to account for Header position
//            mActionMode.setTitle(transaction.getDescription());
//            // Get account ID to pass as tag.
//            mActionMode.setTag(transaction);
//        }
//    }
//
//    private void setAppBarElevation(boolean showElevation) {
//        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            mAppBar.setElevation(showElevation ? getResources().getDimension(R.dimen.appbar_elevation) : 0);
//        }
//    }
//}
