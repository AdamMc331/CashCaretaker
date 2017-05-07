package com.androidessence.cashcaretaker.fragments

import android.database.Cursor
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v4.app.LoaderManager
import android.support.v4.content.CursorLoader
import android.support.v4.content.Loader
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ListView

import com.androidessence.cashcaretaker.R
import com.androidessence.cashcaretaker.adapters.SimpleAccountAdapter
import com.androidessence.cashcaretaker.data.CCContract
import com.androidessence.cashcaretaker.dataTransferObjects.Account

/**
 * Dialog that displays a list of account names for the user to select.

 * Created by adammcneilly on 11/17/15.
 */
class AccountDialog : DialogFragment(), LoaderManager.LoaderCallbacks<Cursor> {
    private var simpleAccountAdapter: SimpleAccountAdapter? = null

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater?.inflate(R.layout.dialog_account, container, false)

        val listView = view?.findViewById(R.id.account_list_view) as ListView
        simpleAccountAdapter = SimpleAccountAdapter(activity)
        listView.adapter = simpleAccountAdapter

        dialog.setTitle("Account")

        listView.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id ->
            val item = listView.getItemAtPosition(position) as Cursor
            (targetFragment as OnAccountSelectedListener).onAccountSelected(Account(item))
            dismiss()
        }

        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        loaderManager.initLoader(CATEGORY_LOADER, null, this)
    }

    override fun onCreateLoader(id: Int, args: Bundle): Loader<Cursor> {
        when (id) {
            CATEGORY_LOADER -> return CursorLoader(
                    activity,
                    CCContract.AccountEntry.CONTENT_URI,
                    SimpleAccountAdapter.ACCOUNT_COLUMNS, null, null,
                    CCContract.AccountEntry.COLUMN_NAME + " ASC"
            )
            else -> throw UnsupportedOperationException("Unknown loader id: " + id)
        }
    }

    override fun onLoadFinished(loader: Loader<Cursor>, data: Cursor) {
        when (loader.id) {
            CATEGORY_LOADER -> simpleAccountAdapter?.swapCursor(data)
            else -> throw UnsupportedOperationException("Unknown loader id: " + loader.id)
        }
    }

    override fun onLoaderReset(loader: Loader<Cursor>) {
        when (loader.id) {
            CATEGORY_LOADER -> simpleAccountAdapter?.swapCursor(null)
            else -> throw UnsupportedOperationException("Unknown loader id: " + loader.id)
        }
    }

    interface OnAccountSelectedListener {
        fun onAccountSelected(account: Account)
    }

    companion object {

        private val CATEGORY_LOADER = 0
    }
}
