package com.androidessence.cashcaretaker.addaccount

import android.app.Dialog
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.androidessence.cashcaretaker.DecimalDigitsInputFilter
import com.androidessence.cashcaretaker.R
import com.androidessence.cashcaretaker.base.BaseDialogFragment
import com.androidessence.cashcaretaker.data.CCDatabase
import com.androidessence.cashcaretaker.data.CCRepository
import com.androidessence.cashcaretaker.databinding.DialogAddAccountBinding

/**
 * Dialog to insert an account.
 */
class AddAccountDialog : BaseDialogFragment() {
    private lateinit var binding: DialogAddAccountBinding
    private lateinit var viewModel: AddAccountViewModel

    private val viewModelFactory: ViewModelProvider.Factory by lazy {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                val database = CCDatabase.getInMemoryDatabase(context!!)
                val repository = CCRepository(database)

                @Suppress("UNCHECKED_CAST")
                return AddAccountViewModel(repository) as T
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DialogAddAccountBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.accountBalanceEditText.filters = arrayOf(DecimalDigitsInputFilter())

        setSubmitListener()

        binding.accountNameEditText.requestFocus()
    }

    private fun setSubmitListener() {
        binding.submitButton.setOnClickListener {
            val name = binding.accountNameEditText.text.toString()
            val balance = binding.accountBalanceEditText.text.toString()
            viewModel.addAccount(name, balance)
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.setTitle(R.string.add_account)

        initializeViewModel()

        return dialog
    }

    override fun onResume() {
        super.onResume()

        dialog?.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
    }

    private fun initializeViewModel() {
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(AddAccountViewModel::class.java)

        viewModel.accountNameError.subscribe {
            binding.accountNameEditText.error = getString(it)
        }.addToComposite()

        viewModel.accountBalanceError.subscribe {
            binding.accountBalanceEditText.error = getString(it)
        }.addToComposite()

        viewModel.accountInserted.subscribe { dismiss() }.addToComposite()
    }

    companion object {
        val FRAGMENT_NAME: String = AddAccountDialog::class.java.simpleName
    }
}