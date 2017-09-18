package com.adammcneilly.cashcaretaker.account

import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import com.adammcneilly.cashcaretaker.DividerItemDecoration
import com.adammcneilly.cashcaretaker.R
import com.adammcneilly.cashcaretaker.main.MainView
import com.androidessence.utility.hide
import com.androidessence.utility.show

/**
 * Displays a list of accounts to the user.
 */
class AccountFragment: Fragment(), AccountView {
    private val adapter = AccountAdapter()
    private lateinit var progressBar: ProgressBar
    private lateinit var recyclerView: RecyclerView
    private val presenter: AccountPresenter by lazy { AccountPresenterImpl(this, AccountInteractorImpl()) }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater?.inflate(R.layout.fragment_account, container, false)

        progressBar = view?.findViewById<ProgressBar>(R.id.progress) as ProgressBar
        recyclerView = view.findViewById<RecyclerView>(R.id.accounts) as RecyclerView

        val layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = layoutManager
        recyclerView.addItemDecoration(DividerItemDecoration(context))

        view.findViewById<FloatingActionButton>(R.id.fab).setOnClickListener({
            //TODO: Is there a better way?
            (activity as MainView).navigateToAddAccount()
        })

        return view
    }

    override fun onResume() {
        super.onResume()
        presenter.onResume()
    }

    override fun onDestroy() {
        presenter.onDestroy()
        super.onDestroy()
    }

    override fun showProgress() {
        recyclerView.hide()
        progressBar.show()
    }

    override fun hideProgress() {
        progressBar.hide()
        recyclerView.show()
    }

    override fun setAccounts(accounts: List<Account>) {
        adapter.items = accounts
    }

    companion object {
        val FRAGMENT_NAME: String = AccountFragment::class.java.simpleName

        fun newInstance(): AccountFragment {
            val fragment = AccountFragment()

            val args = Bundle()
            fragment.arguments = args

            return fragment
        }
    }
}