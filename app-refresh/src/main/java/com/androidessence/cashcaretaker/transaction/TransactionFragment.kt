package com.androidessence.cashcaretaker.transaction

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.androidessence.cashcaretaker.R
import com.androidessence.cashcaretaker.addtransaction.AddTransactionDialog
import com.androidessence.cashcaretaker.core.showError
import com.androidessence.cashcaretaker.data.CCDatabase
import com.androidessence.cashcaretaker.data.CCRepository
import com.androidessence.cashcaretaker.data.DataViewState
import com.androidessence.utility.hide
import com.androidessence.utility.show
import kotlinx.android.synthetic.main.fragment_transaction.*

/**
 * Fragment that displays a list of Transactions.
 */
class TransactionFragment: Fragment(), TransactionController {
    override var viewState: DataViewState = DataViewState.Initialized()
        set(value) {
            when (value) {
                is DataViewState.Loading -> showProgress()
                is DataViewState.ListSuccess<*> -> {
                    hideProgress()

                    adapter.items = value.items.filterIsInstance<Transaction>()
                }
                is DataViewState.ItemsRemoved -> {
                    hideProgress()
                    presenter.actionMode?.finish()
                }
                is DataViewState.ItemsUpdated -> {
                    hideProgress()
                    presenter.actionMode?.finish()
                }
                is DataViewState.Error -> {
                    hideProgress()

                    showError(value.error)
                }
            }
        }

    private val adapter = TransactionAdapter(this)
    private lateinit var presenter: TransactionPresenter
    private val accountName: String by lazy { arguments?.getString(ARG_ACCOUNT).orEmpty() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        presenter = TransactionPresenterImpl(this, TransactionInteractorImpl(CCRepository(CCDatabase.getInMemoryDatabase(context!!))), accountName)

        val title = if (accountName.isEmpty()) getString(R.string.app_name) else getString(R.string.account_transactions, accountName)
        (activity as AppCompatActivity).supportActionBar?.title = title
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.fragment_transaction, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val layoutManager = LinearLayoutManager(context)
        transactions.adapter = adapter
        transactions.layoutManager = layoutManager
        transactions.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))

        add_transaction.setOnClickListener({
            showAddTransaction()
        })
    }

    override fun onResume() {
        super.onResume()
        presenter.onAttach()
    }

    override fun onDestroyView() {
        presenter.onDestroy()
        super.onDestroyView()
    }

    override fun showProgress() {
        transactions.hide()
        progressBar.show()
    }

    override fun hideProgress() {
        progressBar.hide()
        transactions.show()
    }

    override fun showAddTransaction() {
        val dialog = AddTransactionDialog.newInstance(accountName, true)
        dialog.show(fragmentManager, AddTransactionDialog.FRAGMENT_NAME)
    }

    override fun showEditTransaction(transaction: Transaction) {
        val dialog = AddTransactionDialog.newInstance(transaction)
        dialog.show(fragmentManager, AddTransactionDialog.FRAGMENT_NAME)

        // Hide the action mode because once the dialog is shown the fingerprintController is out of our hands.
        //TODO: This feels like a code smell, but idk how to fix yet.
        presenter.actionMode?.finish()
    }

    override fun onTransactionLongClicked(transaction: Transaction) {
        presenter.selectedTransaction = transaction
        presenter.actionMode = (activity as AppCompatActivity).startSupportActionMode(presenter.actionModeCallback)
    }

    companion object {
        val FRAGMENT_NAME: String = TransactionFragment::class.java.simpleName
        private val ARG_ACCOUNT = "accountName"

        fun newInstance(accountName: String): TransactionFragment {
            val args = Bundle()
            args.putString(ARG_ACCOUNT, accountName)

            val fragment = TransactionFragment()
            fragment.arguments = args

            return fragment
        }
    }
}