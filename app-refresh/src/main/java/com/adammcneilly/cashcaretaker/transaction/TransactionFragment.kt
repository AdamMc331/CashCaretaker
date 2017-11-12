package com.adammcneilly.cashcaretaker.transaction

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import com.adammcneilly.cashcaretaker.R
import com.androidessence.utility.hide
import com.androidessence.utility.show

/**
 * Fragment that displays a list of Transactions.
 */
class TransactionFragment: Fragment(), TransactionView {
    private val adapter = TransactionAdapter()
    private lateinit var progressBar: ProgressBar
    private lateinit var recyclerView: RecyclerView
    private lateinit var presenter: TransactionPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val accountName = savedInstanceState?.getString(ARG_ACCOUNT).orEmpty()
        presenter = TransactionPresenterImpl(this, TransactionInteractorImpl(), accountName)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_transaction, container, false)

        progressBar = view.findViewById<ProgressBar>(R.id.progress) as ProgressBar
        recyclerView = view.findViewById<RecyclerView>(R.id.transactions) as RecyclerView

        return view
    }

    override fun showProgress() {
        progressBar.show()
    }

    override fun hideProgress() {
        progressBar.hide()
    }

    override fun setTransactions(transactions: List<Transaction>) {
        adapter.items = transactions
    }

    companion object {
        val FRAGMENT_NAME = TransactionFragment::class.java.simpleName
        private val ARG_ACCOUNT = "accountName"

        fun newInstance(accountName: String): TransactionFragment {
            val args = Bundle()
            args.putString(ARG_ACCOUNT, accountName)

            val fragment = TransactionFragment()
            fragment.arguments = args

            return fragment
        }
    }
}