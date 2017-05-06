package com.androidessence.cashcaretaker.activities

import android.os.Build
import android.os.Bundle
import android.support.design.widget.AppBarLayout
import android.support.v7.app.AlertDialog
import android.support.v7.view.ActionMode
import android.view.Menu
import android.view.MenuItem
import com.androidessence.cashcaretaker.R
import com.androidessence.cashcaretaker.adapters.TransactionAdapter
import com.androidessence.cashcaretaker.core.CoreActivity
import com.androidessence.cashcaretaker.data.CCContract
import com.androidessence.cashcaretaker.dataTransferObjects.Account
import com.androidessence.cashcaretaker.dataTransferObjects.Transaction
import com.androidessence.cashcaretaker.dataTransferObjects.TransactionDetails
import com.androidessence.cashcaretaker.fragments.AccountTransactionsFragment
import com.androidessence.cashcaretaker.fragments.TransactionFragment
import com.androidessence.utility.default

/**
 * Activity that allows the user to add, edit, and view transactions.

 * Created by adam.mcneilly on 9/5/16.
 */
class TransactionsActivity : CoreActivity(),
        AccountTransactionsFragment.OnAddTransactionFABClickedListener,
        TransactionFragment.OnTransactionSubmittedListener,
        TransactionAdapter.OnTransactionLongClickListener {

    private var appBar: AppBarLayout? = null
    private var actionMode: ActionMode? = null

    private val actionModeCallback = object : ActionMode.Callback {
        override fun onCreateActionMode(mode: ActionMode, menu: Menu): Boolean {
            mode.menuInflater.inflate(R.menu.transaction_context_menu, menu)
            return true
        }

        override fun onPrepareActionMode(mode: ActionMode, menu: Menu): Boolean {
            return false
        }

        override fun onActionItemClicked(mode: ActionMode, item: MenuItem): Boolean {
            when (item.itemId) {
                R.id.action_delete_transaction -> {
                    // The transaction that was selected is passed as the tag
                    // for the action mode.
                    showDeleteAlertDialog(actionMode?.tag as? Transaction)
                    mode.finish() // Action picked, so close the CAB
                    return true
                }
                R.id.action_edit_transaction -> {
                    // Edit the transaction selected. Close CAB when done.
                    showEditTransactionFragment(actionMode?.tag as? TransactionDetails)
                    mode.finish()
                    return true
                }
                else -> return false
            }
        }

        override fun onDestroyActionMode(mode: ActionMode) {
            actionMode = null
        }
    }

    private var account: Account? = null
    private var viewState: ViewStates? = null

    private var transactionFragment: TransactionFragment? = null
    private var accountTransactionsFragment: AccountTransactionsFragment? = null

    private enum class ViewStates {
        VIEW,
        ADD,
        EDIT
    }

    private fun showDeleteAlertDialog(transaction: Transaction?) {
        AlertDialog.Builder(this)
                .setTitle("Delete Transaction")
                .setMessage("Are you sure you want to delete " + transaction?.description + "?")
                .setPositiveButton("Yes") { dialog, _ ->
                    contentResolver.delete(
                            CCContract.TransactionEntry.CONTENT_URI,
                            CCContract.TransactionEntry._ID + " = ?",
                            arrayOf(transaction?.identifier.toString())
                    )

                    accountTransactionsFragment?.restartAccountBalanceLoader()
                    dialog.dismiss()
                }
                .setNegativeButton("No") { dialog, _ -> dialog.dismiss() }
                .create().show()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_transactions)

        appBar = findViewById(R.id.appbar) as AppBarLayout

        setupToolbar(true)

        // Read arguments
        viewState = if (savedInstanceState != null && savedInstanceState.containsKey(ARG_VIEW_STATE))
            savedInstanceState.getSerializable(ARG_VIEW_STATE) as ViewStates
        else
            ViewStates.VIEW

        account = intent.getParcelableExtra<Account>(ARG_ACCOUNT) //TODO: Bad assumption?

        // Setup fragment depending on view state
        when (viewState) {
            TransactionsActivity.ViewStates.VIEW -> {
                // When showing transactions we also show account balance, so remove elevation
                // on app bar. Requires API 21
                setAppBarElevation(false)
                val accountTransactionsFragment = supportFragmentManager.findFragmentByTag(AccountTransactionsFragment.FRAGMENT_TAG) as? AccountTransactionsFragment

                if (accountTransactionsFragment == null) {
                    showTransactionsFragment()
                }
            }
            TransactionsActivity.ViewStates.ADD -> {
                setAppBarElevation(true)
                val transactionFragment = supportFragmentManager.findFragmentByTag(TransactionFragment.FRAGMENT_TAG_ADD) as? TransactionFragment

                if (transactionFragment == null) {
                    showAddTransactionFragment()
                }
            }
            TransactionsActivity.ViewStates.EDIT -> {
                setAppBarElevation(true)
                val editTransactionFragment = supportFragmentManager.findFragmentByTag(TransactionFragment.FRAGMENT_TAG_EDIT) as? TransactionFragment

                if (editTransactionFragment == null) {
                    showAddTransactionFragment()
                }
            }
            else -> throw UnsupportedOperationException("Unknown transaction state: " + viewState)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        outState.putSerializable(ARG_VIEW_STATE, viewState)
    }

    private fun showAddTransactionFragment() {
        transactionFragment = TransactionFragment.newInstance(account?.identifier.default(0), TransactionFragment.MODE_ADD, null)
        supportFragmentManager.beginTransaction().replace(R.id.fragment_transactions, transactionFragment, TransactionFragment.FRAGMENT_TAG_ADD).commit()

        setToolbarTitle(getString(R.string.add_transaction))

        viewState = ViewStates.ADD

        setAppBarElevation(true)
    }

    private fun showEditTransactionFragment(transactionDetails: TransactionDetails?) {
        transactionFragment = TransactionFragment.newInstance(account?.identifier.default(0), TransactionFragment.MODE_EDIT, transactionDetails)
        supportFragmentManager.beginTransaction().replace(R.id.fragment_transactions, transactionFragment, TransactionFragment.FRAGMENT_TAG_EDIT).commit()

        setToolbarTitle(getString(R.string.edit_transaction))

        viewState = ViewStates.EDIT

        setAppBarElevation(true)
    }

    private fun showTransactionsFragment() {
        accountTransactionsFragment = AccountTransactionsFragment.newInstance(account?.identifier.default(0))
        supportFragmentManager.beginTransaction().replace(R.id.fragment_transactions, accountTransactionsFragment, AccountTransactionsFragment.FRAGMENT_TAG).commit()

        setToolbarTitle(account?.name + " Transactions")

        viewState = ViewStates.VIEW

        setAppBarElevation(false)
    }

    override fun addTransactionFABClicked() {
        // Close action mode if necessary
        actionMode?.finish()
        showAddTransactionFragment()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                // If we are in view state, finish to go back to accounts
                if (viewState == ViewStates.VIEW) {
                    finish()
                } else {
                    // If we are in ADD or EDIT, show the transactions fragment again
                    showTransactionsFragment()
                }
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    override fun onTransactionSubmitted() {
        showTransactionsFragment()
    }

    override fun onTransactionLongClick(transaction: TransactionDetails) {
        startActionMode(transaction)
    }

    private fun startActionMode(transactionDetails: TransactionDetails) {
        // Don't fire if already being used
        if (actionMode == null) {
            actionMode = startSupportActionMode(actionModeCallback)
            actionMode?.title = transactionDetails.description
            actionMode?.tag = transactionDetails
        }
    }

    private fun setAppBarElevation(showElevation: Boolean) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            appBar?.elevation =
                    if (showElevation) resources.getDimension(R.dimen.appbar_elevation) else 0.toFloat()
        }
    }

    companion object {

        val ARG_ACCOUNT = "account"
        private val ARG_VIEW_STATE = "viewState"
    }
}
