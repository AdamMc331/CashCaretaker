package com.androidessence.cashcaretaker.account

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.androidessence.cashcaretaker.R
import com.androidessence.cashcaretaker.addaccount.AddAccountDialog
import com.androidessence.cashcaretaker.addtransaction.AddTransactionDialog
import com.androidessence.cashcaretaker.data.DataViewState
import com.androidessence.cashcaretaker.main.MainController
import com.androidessence.utility.hide
import com.androidessence.utility.show
import kotlinx.android.synthetic.main.fragment_account.*
import timber.log.Timber

/**
 * Displays a list of accounts to the user.
 *
 * @property[adapter] An adapter responsible for the list of accounts in this fragment.
 * @property[presenter] The presenter that will connect with the data layer for a list of accounts.
 * @property[mainController] A controller that connects to the activity of this fragment when necessary.
 */
class AccountFragment: Fragment(), AccountController {
    override var viewState: DataViewState = DataViewState.Initialized()
        set(value) {
            when (value) {
                is DataViewState.Loading -> showProgress()
                is DataViewState.ListSuccess<*> -> {
                    hideProgress()

                    adapter.items = value.items.filterIsInstance<Account>()
                }
                is DataViewState.ItemsRemoved -> {
                    hideProgress()
                    presenter.actionMode?.finish()
                }
                is DataViewState.Error -> {
                    hideProgress()

                    //TODO: Show Snackbar
                    Timber.e(value.error)
                }
            }
        }

    private val adapter = AccountAdapter(this)
    private val presenter: AccountPresenter by lazy { AccountPresenterImpl(this, AccountInteractorImpl()) }
    private val mainController: MainController by lazy { (activity as MainController) }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.fragment_account, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val layoutManager = LinearLayoutManager(context)
        accountsRecyclerView.adapter = adapter
        accountsRecyclerView.layoutManager = layoutManager
        accountsRecyclerView.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))

        add_account.setOnClickListener { showAddAccountView() }

        fragmentManager?.addOnBackStackChangedListener { presenter.actionMode?.finish() }
    }

    override fun onResume() {
        super.onResume()
        presenter.onAttach()
    }

    override fun onDestroy() {
        presenter.onDestroy()
        super.onDestroy()
    }

    override fun showProgress() {
        accountsRecyclerView.hide()
        progressBar.show()
    }

    override fun hideProgress() {
        progressBar.hide()
        accountsRecyclerView.show()
    }

    override fun onTransactionButtonClicked(account: Account, withdrawal: Boolean) {
        val dialog = AddTransactionDialog.newInstance(account.name, withdrawal)
        dialog.show(fragmentManager, AddTransactionDialog.FRAGMENT_NAME)
    }

    override fun onAccountSelected(account: Account) {
        mainController.showTransactions(account.name)
    }

    override fun onAccountLongClicked(account: Account) {
        presenter.selectedAccount = account
        presenter.actionMode = (activity as AppCompatActivity).startSupportActionMode(presenter.actionModeCallback)
    }

    override fun showAddAccountView() {
        val dialog = AddAccountDialog()
        dialog.show(fragmentManager, AddAccountDialog.FRAGMENT_NAME)
    }

    companion object {
        /**
         * Tag used when we want to display this fragment.
         */
        val FRAGMENT_NAME: String = AccountFragment::class.java.simpleName

        /**
         * Creates a new fragment to display all accounts.
         */
        fun newInstance(): AccountFragment {
            val fragment = AccountFragment()

            val args = Bundle()
            fragment.arguments = args

            return fragment
        }
    }
}