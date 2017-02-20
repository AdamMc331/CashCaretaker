package com.androidessence.cashcaretaker.refresh

import android.os.Bundle
import android.support.design.widget.TextInputEditText
import android.support.v4.app.Fragment
import android.text.InputFilter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.androidessence.cashcaretaker.DecimalDigitsInputFilter
import com.androidessence.cashcaretaker.R

/**
 * Allows the user to add an account.
 *
 * Created by adam.mcneilly on 2/6/17.
 */
class AddAccountFragment: Fragment(), View.OnClickListener {

    var accountName: TextInputEditText? = null
    var startingBalance: TextInputEditText? = null
    var submit: Button? = null

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater?.inflate(R.layout.fragment_add_account, container, false)

        accountName = view?.findViewById(R.id.account_name) as? TextInputEditText
        startingBalance = view?.findViewById(R.id.starting_balance) as? TextInputEditText
        submit = view?.findViewById(R.id.submit) as? Button

        val inputFilters = arrayOf<InputFilter>(DecimalDigitsInputFilter())
        startingBalance?.filters = inputFilters

        submit?.setOnClickListener(this)

        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()

        accountName = null
        startingBalance = null
        submit = null
    }

    private fun validateInput(): Boolean {
        return accountName?.text?.toString()?.isNotEmpty().orFalse()
                && startingBalance?.text?.toString()?.isNotEmpty().orFalse()
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.submit -> {
                if (validateInput()) {
                    val account = Account()
                    account.name = accountName?.text?.toString()!!
                    account.balance = startingBalance?.text?.toString()!!.toDouble()
                }
            }
        }
    }
}