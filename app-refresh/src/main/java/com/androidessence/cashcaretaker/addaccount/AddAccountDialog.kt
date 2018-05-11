package com.androidessence.cashcaretaker.addaccount

import android.app.Dialog
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.androidessence.cashcaretaker.DecimalDigitsInputFilter
import com.androidessence.cashcaretaker.R
import com.androidessence.cashcaretaker.databinding.DialogAddAccountBinding
import io.reactivex.disposables.CompositeDisposable

/**
 * Dialog to insert an account.
 */
class AddAccountDialog : DialogFragment() {
    private val compositeDisposable = CompositeDisposable()
    private lateinit var binding: DialogAddAccountBinding
    private lateinit var viewModel: AddAccountViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DialogAddAccountBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.accountBalanceEditText.filters = arrayOf(DecimalDigitsInputFilter())

        binding.submitButton.setOnClickListener {
            val name = binding.accountNameEditText.text.toString()
            val balance = binding.accountBalanceEditText.text.toString()
            viewModel.addAccount(name, balance)
        }

        binding.accountNameEditText.requestFocus()

        subscribeToViewModel()
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.setTitle(R.string.add_account)
        return dialog
    }

    override fun onResume() {
        super.onResume()

        dialog?.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
    }

    override fun onPause() {
        super.onPause()
        compositeDisposable.dispose()
    }

    private fun subscribeToViewModel() {
        compositeDisposable.addAll(
                viewModel.accountNameError.subscribe(binding.accountNameEditText::setError),
                viewModel.accountBalanceError.subscribe(binding.accountBalanceEditText::setError),
                viewModel.accountInserted.subscribe { dismiss() }
        )
    }

    companion object {
        /**
         * The tag that is used when this account is inserted.
         */
        val FRAGMENT_NAME: String = AddAccountDialog::class.java.simpleName
    }
}