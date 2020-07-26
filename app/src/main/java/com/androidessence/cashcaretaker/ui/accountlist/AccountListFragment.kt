package com.androidessence.cashcaretaker.ui.accountlist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.androidessence.cashcaretaker.R
import com.androidessence.cashcaretaker.core.models.Account
import com.androidessence.cashcaretaker.data.analytics.AnalyticsTracker
import com.androidessence.cashcaretaker.databinding.FragmentAccountBinding
import com.androidessence.cashcaretaker.ui.addaccount.AddAccountDialog
import com.androidessence.cashcaretaker.ui.addtransaction.AddTransactionDialog
import com.androidessence.cashcaretaker.ui.main.MainController
import com.androidessence.cashcaretaker.ui.transfer.AddTransferDialog
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

/**
 * Fragment for displaying a list of accounts to the user.
 *
 * The Fragment is responsible for any UI related actions, which are triggered by subscribing to
 * subjects exposed by the [viewModel].
 *
 * @property[viewModel] A LifeCycle aware component that is responsible for fetching accounts and
 * notifying the fragment. This component also handles the ActionMode behavior.
 */
@Suppress("TooManyFunctions")
@AndroidEntryPoint
class AccountListFragment : Fragment() {
    //region Properties
    private val adapter = AccountListAdapter(
        accountClicked = this::onAccountSelected,
        accountLongClicked = this::onAccountLongClicked,
        withdrawalClicked = this::onWithdrawalButtonClicked,
        depositClicked = this::onDepositButtonClicked
    )
    private lateinit var binding: FragmentAccountBinding

    private val viewModel: AccountListViewModel by viewModels()

    @Inject
    lateinit var analyticsTracker: AnalyticsTracker
    //endregion

    //region Lifecycle Methods
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)

        initializeViewModel()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAccountBinding.inflate(inflater, container, false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initializeRecyclerView()

        binding.addAccountButton.setOnClickListener { showAddAccountView() }

        childFragmentManager.addOnBackStackChangedListener { viewModel.clearActionMode() }
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
                dialog.show(childFragmentManager, AddTransferDialog::class.java.simpleName)
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
        binding.accountsRecyclerView.addItemDecoration(
            DividerItemDecoration(
                context,
                DividerItemDecoration.VERTICAL
            )
        )

        // https://stackoverflow.com/a/39813266/3131147
        binding.accountsRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (dy > 0 || dy < 0 && binding.addAccountButton.isShown) {
                    binding.addAccountButton.hide()
                }
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
        viewModel.accounts.observe(
            this,
            Observer { accounts ->
                adapter.items = accounts
                activity?.invalidateOptionsMenu()
            }
        )
    }
    //endregion

    //region UI Events
    private fun onWithdrawalButtonClicked(account: Account) {
        val dialog = AddTransactionDialog.newInstance(account.name, true)
        dialog.show(childFragmentManager, AddTransactionDialog.FRAGMENT_NAME)
    }

    private fun onDepositButtonClicked(account: Account) {
        val dialog = AddTransactionDialog.newInstance(account.name, false)
        dialog.show(childFragmentManager, AddTransactionDialog.FRAGMENT_NAME)
    }

    private fun onAccountSelected(account: Account) {
        analyticsTracker.trackAccountClicked()
        (activity as? MainController)?.showTransactions(account.name)
    }

    private fun onAccountLongClicked(account: Account) {
        viewModel.startActionModeForAccount(account, activity as AppCompatActivity)
    }

    private fun showAddAccountView() {
        val dialog = AddAccountDialog()
        dialog.show(childFragmentManager, AddAccountDialog.FRAGMENT_NAME)
    }
    //endregion

    companion object {
        val FRAGMENT_NAME: String = AccountListFragment::class.java.simpleName
        fun newInstance(): AccountListFragment = AccountListFragment()
    }
}
