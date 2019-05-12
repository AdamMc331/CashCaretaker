package com.androidessence.cashcaretaker.transaction

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.androidessence.cashcaretaker.R
import com.androidessence.cashcaretaker.addtransaction.AddTransactionDialog
import com.androidessence.cashcaretaker.base.BaseFragment
import com.androidessence.cashcaretaker.data.CCDatabase
import com.androidessence.cashcaretaker.data.CCDatabaseService
import com.androidessence.cashcaretaker.data.DataViewState
import com.androidessence.cashcaretaker.databinding.FragmentTransactionBinding

/**
 * Fragment that displays a list of Transactions.
 */
class TransactionFragment : BaseFragment() {
    //region Properties
    private val adapter = TransactionAdapter(
            this::onTransactionLongClicked
    )

    private val accountName: String
        get() = arguments?.getString(ARG_ACCOUNT).orEmpty()

    private lateinit var viewModel: TransactionViewModel
    private lateinit var binding: FragmentTransactionBinding

    private val viewModelFactory: ViewModelProvider.Factory by lazy {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                val database = CCDatabase.getInMemoryDatabase(context!!)
                val repository = CCDatabaseService(database)

                @Suppress("UNCHECKED_CAST")
                return TransactionViewModel(
                        repository = repository,
                        accountName = accountName,
                        editClicked = this@TransactionFragment::showEditTransaction
                ) as T
            }
        }
    }
    //endregion

    //region Lifecycle Methods
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initializeViewModel()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentTransactionBinding.inflate(inflater, container, false)
        binding.viewModel = viewModel
        setHasOptionsMenu(false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupTitle()

        initializeRecyclerView()

        binding.addTransactionButton.setOnClickListener { showAddTransaction() }
    }
    //endregion

    //region Initializations
    private fun setupTitle() {
        val title = if (accountName.isEmpty()) getString(R.string.app_name) else getString(R.string.account_transactions, accountName)
        (activity as AppCompatActivity).supportActionBar?.title = title
    }

    /**
     * Subscribes to any subjects the [viewModel] exposes such as the state (which is used to update the adapter),
     * and the click subject to edit a transaction.
     */
    private fun initializeViewModel() {
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(TransactionViewModel::class.java)

        viewModel.state.observe(this, Observer { state ->
            when (state) {
                is DataViewState.Success<*> -> {
                    @Suppress("UNCHECKED_CAST")
                    (state.result as? List<Transaction>)?.let { transactions ->
                        adapter.items = transactions
                    }
                }
            }
        })
    }

    private fun initializeRecyclerView() {
        binding.transactionsRecyclerView.adapter = adapter
        binding.transactionsRecyclerView.layoutManager = LinearLayoutManager(context)
        binding.transactionsRecyclerView.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))

        // https://stackoverflow.com/a/39813266/3131147
        binding.transactionsRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (dy > 0 || dy < 0 && binding.addTransactionButton.isShown) {
                    binding.addTransactionButton.hide()
                }
            }

            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    binding.addTransactionButton.show()
                }
                super.onScrollStateChanged(recyclerView, newState)
            }
        })
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