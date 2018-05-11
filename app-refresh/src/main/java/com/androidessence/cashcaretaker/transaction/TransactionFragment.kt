package com.androidessence.cashcaretaker.transaction

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
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
import com.androidessence.cashcaretaker.data.CCDatabase
import com.androidessence.cashcaretaker.data.CCRepository
import com.androidessence.cashcaretaker.databinding.FragmentTransactionBinding
import io.reactivex.disposables.CompositeDisposable

/**
 * Fragment that displays a list of Transactions.
 */
class TransactionFragment : Fragment() {
    //region Properties
    private val adapter = TransactionAdapter()
    private val compositeDisposable = CompositeDisposable()
    private lateinit var viewModel: TransactionViewModel
    private lateinit var binding: FragmentTransactionBinding

    private val viewModelFactory: ViewModelProvider.Factory by lazy {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                val database = CCDatabase.getInMemoryDatabase(context!!)
                val repository = CCRepository(database)

                @Suppress("UNCHECKED_CAST")
                return TransactionViewModel(repository) as T
            }
        }
    }
    //endregion

    private val accountName: String by lazy { arguments?.getString(ARG_ACCOUNT).orEmpty() }

    //region Lifecycle Methods
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setupTitle()
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(TransactionViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentTransactionBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initializeRecyclerView()
        subscribeToAdapter()
        subscribeToViewModel()

        binding.addTransaction.setOnClickListener { showAddTransaction() }

        viewModel.fetchTransactionForAccount(accountName)
    }

    override fun onPause() {
        super.onPause()
        compositeDisposable.dispose()
    }
    //endregion

    //region Initializations
    private fun setupTitle() {
        val title = if (accountName.isEmpty()) getString(R.string.app_name) else getString(R.string.account_transactions, accountName)
        (activity as AppCompatActivity).supportActionBar?.title = title
    }

    private fun subscribeToAdapter() {
        compositeDisposable.add(
                adapter.transactionLongClicked.subscribe(this::onTransactionLongClicked)
        )
    }

    private fun subscribeToViewModel() {
        compositeDisposable.addAll(
                viewModel.transactionList.subscribe { adapter.items = it },
                viewModel.editClicked.subscribe(this::showEditTransaction)
        )
    }

    private fun initializeRecyclerView() {
        binding.transactions.adapter = adapter
        binding.transactions.layoutManager = LinearLayoutManager(context)
        binding.transactions.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
    }
    //endregion

    //region UI Events
    private fun showAddTransaction() {
        val dialog = AddTransactionDialog.newInstance(accountName, true)
        dialog.show(fragmentManager, AddTransactionDialog.FRAGMENT_NAME)
    }

    private fun showEditTransaction(transaction: Transaction) {
        val dialog = AddTransactionDialog.newInstance(transaction)
        dialog.show(fragmentManager, AddTransactionDialog.FRAGMENT_NAME)
    }

    private fun onTransactionLongClicked(transaction: Transaction) {
        viewModel.startActionModeForTransaction(transaction, activity as AppCompatActivity)
    }
    //endregion

    companion object {
        val FRAGMENT_NAME: String = TransactionFragment::class.java.simpleName
        private const val ARG_ACCOUNT = "accountName"

        fun newInstance(accountName: String): TransactionFragment {
            val args = Bundle()
            args.putString(ARG_ACCOUNT, accountName)

            val fragment = TransactionFragment()
            fragment.arguments = args

            return fragment
        }
    }
}