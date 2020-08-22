package com.androidessence.cashcaretaker.ui.transactionlist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.androidessence.cashcaretaker.R
import com.androidessence.cashcaretaker.core.models.Transaction
import com.androidessence.cashcaretaker.databinding.FragmentTransactionBinding
import com.androidessence.cashcaretaker.ui.addtransaction.AddTransactionDialog
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import me.ibrahimyilmaz.kiel.adapterOf
import org.koin.android.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

/**
 * Fragment that displays a list of Transactions.
 */
class TransactionListFragment : Fragment() {
    //region Properties
    private val adapter = adapterOf<Transaction> {
        register(
            layoutResource = R.layout.list_item_transaction,
            viewHolder = ::TransactionViewHolder,
            onViewHolderCreated = { viewHolder ->
                viewHolder.apply {
                    setOnTransactionLongClickedListener()
                }
            }
        )
    }

    private val accountName: String
        get() = arguments?.getString(ARG_ACCOUNT).orEmpty()

    private lateinit var binding: FragmentTransactionBinding

    private val viewModel: TransactionListViewModel by viewModel {
        parametersOf(accountName)
    }
    //endregion

    //region Lifecycle Methods
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initializeViewModel()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTransactionBinding.inflate(inflater, container, false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
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
        val title = if (accountName.isEmpty()) getString(R.string.app_name) else getString(
            R.string.account_transactions,
            accountName
        )
        (activity as AppCompatActivity).supportActionBar?.title = title
    }

    /**
     * Subscribes to any subjects the [viewModel] exposes such as the state (which is used to update the adapter),
     * and the click subject to edit a transaction.
     */
    private fun initializeViewModel() {
        viewModel.transactions.observe(
            this,
            Observer(adapter::submitList)
        )

        lifecycleScope.launch {
            viewModel.editClickedFlow.collect { transaction ->
                showEditTransaction(transaction)
            }
        }
    }

    private fun initializeRecyclerView() {
        binding.transactionsRecyclerView.adapter = adapter
        binding.transactionsRecyclerView.layoutManager = LinearLayoutManager(context)
        binding.transactionsRecyclerView.addItemDecoration(
            DividerItemDecoration(
                context,
                DividerItemDecoration.VERTICAL
            )
        )

        // https://stackoverflow.com/a/39813266/3131147
        binding.transactionsRecyclerView.addOnScrollListener(
            object : RecyclerView.OnScrollListener() {
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
            }
        )
    }

    private fun TransactionViewHolder.setOnTransactionLongClickedListener() {
        itemView.setOnLongClickListener {
            viewModel.transaction?.let(::onTransactionLongClicked)
            true
        }
    }
    //endregion

    //region UI Events
    private fun showAddTransaction() {
        val dialog = AddTransactionDialog.newInstance(accountName, true)
        dialog.show(childFragmentManager, AddTransactionDialog.FRAGMENT_NAME)
    }

    private fun showEditTransaction(transaction: Transaction) {
        val dialog = AddTransactionDialog.newInstance(transaction)
        dialog.show(childFragmentManager, AddTransactionDialog.FRAGMENT_NAME)
    }

    private fun onTransactionLongClicked(transaction: Transaction) {
        viewModel.startActionModeForTransaction(transaction, activity as AppCompatActivity)
    }
    //endregion

    companion object {
        val FRAGMENT_NAME: String = TransactionListFragment::class.java.simpleName
        private const val ARG_ACCOUNT = "accountName"

        fun newInstance(accountName: String): TransactionListFragment {
            val args = Bundle()
            args.putString(ARG_ACCOUNT, accountName)

            val fragment = TransactionListFragment()
            fragment.arguments = args

            return fragment
        }
    }
}
