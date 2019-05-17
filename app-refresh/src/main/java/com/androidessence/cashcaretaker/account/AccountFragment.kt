package com.androidessence.cashcaretaker.account

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.androidessence.cashcaretaker.R
import com.androidessence.cashcaretaker.addaccount.AddAccountDialog
import com.androidessence.cashcaretaker.addtransaction.AddTransactionDialog
import com.androidessence.cashcaretaker.data.CCDatabase
import com.androidessence.cashcaretaker.data.CCDatabaseService
import com.androidessence.cashcaretaker.data.DataViewState
import com.androidessence.cashcaretaker.databinding.FragmentAccountBinding
import com.androidessence.cashcaretaker.main.MainController
import com.androidessence.cashcaretaker.transfer.AddTransferDialog

/**
 * Fragment for displaying a list of accounts to the user.
 *
 * The Fragment is responsible for any UI related actions, which are triggered by subscribing to
 * subjects exposed by the [viewModel].
 *
 * @property[viewModel] A LifeCycle aware component that is responsible for fetching accounts and
 * notifying the fragment. This component also handles the ActionMode behavior.
 */
class AccountFragment : Fragment() {
    //region Properties
    private val adapter = AccountAdapter(
            accountClicked = this::onAccountSelected,
            accountLongClicked = this::onAccountLongClicked,
            withdrawalClicked = this::onWithdrawalButtonClicked,
            depositClicked = this::onDepositButtonClicked
    )
    private lateinit var viewModel: AccountFragmentViewModel
    private lateinit var binding: FragmentAccountBinding

    private val viewModelFactory: ViewModelProvider.Factory by lazy {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                val database = CCDatabase.getInMemoryDatabase(context!!)
                val repository = CCDatabaseService(database)

                @Suppress("UNCHECKED_CAST")
                return AccountFragmentViewModel(repository) as T
            }
        }
    }
    //endregion

    //region Lifecycle Methods
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)

        initializeViewModel()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentAccountBinding.inflate(inflater, container, false)
        binding.viewModel = viewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initializeRecyclerView()

        binding.addAccountButton.setOnClickListener { showAddAccountView() }

        fragmentManager?.addOnBackStackChangedListener { viewModel.clearActionMode() }
    }
    //endregion

    //region Menu
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_accounts, menu)
        menu.findItem(R.id.action_transfer)?.isVisible = viewModel.allowTransfers
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_transfer -> {
                val dialog = AddTransferDialog()
                dialog.show(requireFragmentManager(), AddTransferDialog::class.java.simpleName)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
    //endregion

    //region Initializations
    private fun initializeRecyclerView() {
        val layoutManager = LinearLayoutManager(context)
        binding.accountsRecyclerView.adapter = adapter
        binding.accountsRecyclerView.layoutManager = layoutManager
        binding.accountsRecyclerView.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))

        // https://stackoverflow.com/a/39813266/3131147
        binding.accountsRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (dy > 0 || dy < 0 && binding.addAccountButton.isShown)
                    binding.addAccountButton.hide()
            }

            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    binding.addAccountButton.show()
                }
                super.onScrollStateChanged(recyclerView, newState)
            }
        })
    }

    /**
     * Subscribes to any subjects that the [viewModel] is exposing. This includes the [viewModel] state,
     * which we use to update the adpater when a list is pulled successfully.
     */
    private fun initializeViewModel() {
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(AccountFragmentViewModel::class.java)

        viewModel.state.observe(this, Observer { state ->
            when (state) {
                is DataViewState.Success<*> -> {
                    @Suppress("UNCHECKED_CAST")
                    (state.result as? List<Account>)?.let { accounts ->
                        adapter.items = accounts
                        activity?.invalidateOptionsMenu()
                    }
                }
            }
        })
    }
    //endregion

    //region UI Events
    private fun onWithdrawalButtonClicked(account: Account) {
        val dialog = AddTransactionDialog.newInstance(account.name, true)
        dialog.show(requireFragmentManager(), AddTransactionDialog.FRAGMENT_NAME)
    }

    private fun onDepositButtonClicked(account: Account) {
        val dialog = AddTransactionDialog.newInstance(account.name, false)
        dialog.show(requireFragmentManager(), AddTransactionDialog.FRAGMENT_NAME)
    }

    private fun onAccountSelected(account: Account) {
        (activity as? MainController)?.showTransactions(account.name)
    }

    private fun onAccountLongClicked(account: Account) {
        viewModel.startActionModeForAccount(account, activity as AppCompatActivity)
    }

    private fun showAddAccountView() {
        val dialog = AddAccountDialog()
        dialog.show(requireFragmentManager(), AddAccountDialog.FRAGMENT_NAME)
    }
    //endregion

    companion object {
        val FRAGMENT_NAME: String = AccountFragment::class.java.simpleName
        fun newInstance(): AccountFragment = AccountFragment()
    }
}