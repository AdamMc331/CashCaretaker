package com.androidessence.cashcaretaker.account

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.view.*
import com.androidessence.cashcaretaker.R
import com.androidessence.cashcaretaker.addaccount.AddAccountDialog
import com.androidessence.cashcaretaker.addtransaction.AddTransactionDialog
import com.androidessence.cashcaretaker.base.BaseFragment
import com.androidessence.cashcaretaker.data.CCDatabase
import com.androidessence.cashcaretaker.data.CCRepository
import com.androidessence.cashcaretaker.data.DataViewState
import com.androidessence.cashcaretaker.databinding.FragmentAccountBinding
import com.androidessence.cashcaretaker.main.MainController
import com.androidessence.cashcaretaker.transfer.AddTransferDialog
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
import android.support.v7.widget.RecyclerView


/**
 * Fragment for displaying a list of accounts to the user.
 *
 * The Fragment is responsible for any UI related actions, which are triggered by subscribing to
 * subjects exposed by the [viewModel].
 *
 * @property[viewModel] A LifeCycle aware component that is responsible for fetching accounts and
 * notifying the fragment. This component also handles the ActionMode behavior.
 */
class AccountFragment : BaseFragment() {
    //region Properties
    private val adapter = AccountAdapter()
    private lateinit var viewModel: AccountViewModel
    private lateinit var binding: FragmentAccountBinding

    private val viewModelFactory: ViewModelProvider.Factory by lazy {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                val database = CCDatabase.getInMemoryDatabase(context!!)
                val repository = CCRepository(database)

                @Suppress("UNCHECKED_CAST")
                return AccountViewModel(repository) as T
            }
        }
    }
    //endregion

    //region Lifecycle Methods
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setHasOptionsMenu(true)

        subscribeToAdapterClicks()
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

        viewModel.fetchAccounts()

        fragmentManager?.addOnBackStackChangedListener { viewModel.clearActionMode() }
    }
    //endregion

    //region Menu
    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater?.inflate(R.menu.menu_accounts, menu)

        // Only allow transfers if we have greater than or equal to 2 numAccounts.
        val numAccounts = adapter.items.size
        menu?.findItem(R.id.action_transfer)?.isVisible = numAccounts >= 2
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when (item?.itemId) {
            R.id.action_transfer -> {
                val dialog = AddTransferDialog()
                dialog.show(fragmentManager, AddTransferDialog::class.java.simpleName)
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
            override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
                if (dy > 0 || dy < 0 && binding.addAccountButton.isShown)
                    binding.addAccountButton.hide()
            }

            override fun onScrollStateChanged(recyclerView: RecyclerView?, newState: Int) {
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
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(AccountViewModel::class.java)

        viewModel.state
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { state ->
                    when (state) {
                        is DataViewState.Success<*> -> {
                            @Suppress("UNCHECKED_CAST")
                            (state.result as? List<Account>)?.let { accounts ->
                                adapter.items = accounts
                                activity?.invalidateOptionsMenu()
                            }
                        }
                    }
                }
                .addToComposite()
    }

    private fun subscribeToAdapterClicks() {
        adapter.accountClickSubject
                .subscribe {
                    onAccountSelected(it)
                    Timber.d("AccountClicked")
                }
                .addToComposite()
        adapter.accountLongClickSubject.subscribe(this::onAccountLongClicked).addToComposite()
        adapter.withdrawalClickSubject.subscribe(this::onWithdrawalButtonClicked).addToComposite()
        adapter.depositClickSubject.subscribe(this::onDepositButtonClicked).addToComposite()
    }
    //endregion

    //region UI Events
    private fun onWithdrawalButtonClicked(account: Account) {
        val dialog = AddTransactionDialog.newInstance(account.name, true)
        dialog.show(fragmentManager, AddTransactionDialog.FRAGMENT_NAME)
    }

    private fun onDepositButtonClicked(account: Account) {
        val dialog = AddTransactionDialog.newInstance(account.name, false)
        dialog.show(fragmentManager, AddTransactionDialog.FRAGMENT_NAME)
    }

    private fun onAccountSelected(account: Account) {
        (activity as? MainController)?.showTransactions(account.name)
    }

    private fun onAccountLongClicked(account: Account) {
        viewModel.startActionModeForAccount(account, activity as AppCompatActivity)
    }

    private fun showAddAccountView() {
        val dialog = AddAccountDialog()
        dialog.show(fragmentManager, AddAccountDialog.FRAGMENT_NAME)
    }
    //endregion

    companion object {
        val FRAGMENT_NAME: String = AccountFragment::class.java.simpleName
        fun newInstance(): AccountFragment = AccountFragment()
    }
}