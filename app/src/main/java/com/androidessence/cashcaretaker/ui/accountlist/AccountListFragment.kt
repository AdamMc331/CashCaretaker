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
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.adammcneilly.cashcaretaker.analytics.AnalyticsTracker
import com.androidessence.cashcaretaker.R
import com.androidessence.cashcaretaker.core.models.Account
import com.androidessence.cashcaretaker.databinding.FragmentAccountBinding
import com.androidessence.cashcaretaker.ui.addaccount.AddAccountDialog
import com.androidessence.cashcaretaker.ui.addtransaction.AddTransactionDialog
import com.androidessence.cashcaretaker.ui.main.MainController
import com.androidessence.cashcaretaker.ui.transfer.AddTransferDialog
import com.androidessence.cashcaretaker.ui.utils.launchWhenResumed
import com.androidessence.cashcaretaker.ui.utils.visibleIf
import kotlinx.coroutines.flow.onEach
import me.ibrahimyilmaz.kiel.adapterOf
import org.koin.android.ext.android.get
import org.koin.android.viewmodel.ext.android.viewModel

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
class AccountListFragment : Fragment() {
    //region Properties

    private val adapter = adapterOf<Account> {
        register(
            layoutResource = R.layout.list_item_account,
            viewHolder = ::AccountViewHolder,
            onViewHolderCreated = { vh ->
                vh.apply { setClickListeners() }
            }
        )
    }

    private lateinit var binding: FragmentAccountBinding

    private val viewModel: AccountListViewModel by viewModel()

    private val analyticsTracker: AnalyticsTracker = get()
    //endregion

    //region Lifecycle Methods
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)

        subscribeToViewModel()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAccountBinding.inflate(inflater, container, false)
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
        menu.findItem(R.id.action_transfer)?.isVisible = viewModel.viewState.value.allowTransfers
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
        binding.accountsRecyclerView.addOnScrollListener(
            object : RecyclerView.OnScrollListener() {
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
            }
        )
    }

    /**
     * Subscribes to any subjects that the [viewModel] is exposing. This includes the [viewModel] state,
     * which we use to update the adapter when a list is pulled successfully.
     */
    private fun subscribeToViewModel() {
        viewModel.viewState
            .onEach { viewState ->
                processViewState(viewState)
            }
            .launchWhenResumed(lifecycleScope)
    }

    private fun processViewState(viewState: AccountListViewState) {
        binding.progressBar.visibleIf(viewState.showLoading)
        binding.accountsRecyclerView.visibleIf(viewState.showContent)
        binding.emptyStateGroup.visibleIf(viewState.showEmptyState)

        adapter.submitList(viewState.accounts)
        activity?.invalidateOptionsMenu()
    }

    private fun AccountViewHolder.setClickListeners() {
        binding.withdrawalButton.setOnClickListener {
            viewModel.account?.let(::onWithdrawalButtonClicked)
        }

        binding.depositButton.setOnClickListener {
            viewModel.account?.let(::onDepositButtonClicked)
        }

        itemView.setOnClickListener {
            viewModel.account?.let(::onAccountSelected)
        }
        itemView.setOnLongClickListener {
            viewModel.account?.let(::onAccountLongClicked)
            true
        }
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
