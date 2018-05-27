package com.androidessence.cashcaretaker.transaction

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.androidessence.cashcaretaker.R
import com.androidessence.cashcaretaker.addtransaction.AddTransactionDialog
import com.androidessence.cashcaretaker.base.BaseFragment
import com.androidessence.cashcaretaker.data.CCDatabase
import com.androidessence.cashcaretaker.data.CCRepository
import com.androidessence.cashcaretaker.data.DataViewState
import com.androidessence.cashcaretaker.databinding.FragmentTransactionBinding
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * Fragment that displays a list of Transactions.
 */
class TransactionFragment : BaseFragment() {
    //region Properties
    private val adapter = TransactionAdapter()
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

    private lateinit var accountName: String

    //region Lifecycle Methods
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        readArguments()
        setupTitle()
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(TransactionViewModel::class.java)
        subscribeToAdapter()
        subscribeToViewModel()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentTransactionBinding.inflate(inflater, container, false)
        binding.viewModel = viewModel
        setHasOptionsMenu(false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initializeRecyclerView()

        binding.addTransactionButton.setOnClickListener { showAddTransaction() }

        viewModel.fetchTransactionForAccount(accountName)
    }
    //endregion

    //region Initializations
    private fun readArguments() {
        accountName = arguments?.getString(ARG_ACCOUNT).orEmpty()
    }

    private fun setupTitle() {
        val title = if (accountName.isEmpty()) getString(R.string.app_name) else getString(R.string.account_transactions, accountName)
        (activity as AppCompatActivity).supportActionBar?.title = title
    }

    private fun subscribeToAdapter() {
        adapter.transactionLongClicked.subscribe(this::onTransactionLongClicked).addToComposite()
    }

    /**
     * Subscribes to any subjects the [viewModel] exposes such as the state (which is used to update the adapter),
     * and the click subject to edit a transaction.
     */
    private fun subscribeToViewModel() {
        viewModel.state
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { state ->
                    when (state) {
                        is DataViewState.Success<*> -> {
                            @Suppress("UNCHECKED_CAST")
                            (state.result as? List<Transaction>)?.let { transactions ->
                                adapter.items = transactions
                            }
                        }
                    }
                }.addToComposite()
        viewModel.editClicked.subscribe(this::showEditTransaction).addToComposite()
    }

    private fun initializeRecyclerView() {
        binding.transactionsRecyclerView.adapter = adapter
        binding.transactionsRecyclerView.layoutManager = LinearLayoutManager(context)
        binding.transactionsRecyclerView.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
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