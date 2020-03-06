package com.androidessence.cashcaretaker.addaccount

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.androidessence.cashcaretaker.DecimalDigitsInputFilter
import com.androidessence.cashcaretaker.data.DatabaseService
import com.androidessence.cashcaretaker.database.RoomDatabase
import com.androidessence.cashcaretaker.databinding.DialogAddAccountBinding

/**
 * Dialog to insert an account.
 */
class AddAccountDialog : DialogFragment() {
    private lateinit var binding: DialogAddAccountBinding
    private lateinit var viewModel: AddAccountViewModel

    private val viewModelFactory: ViewModelProvider.Factory by lazy {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                val repository = DatabaseService(
                    database = RoomDatabase(requireContext())
                )

                @Suppress("UNCHECKED_CAST")
                return AddAccountViewModel(
                    repository = repository,
                    accountInserted = {
                        dismiss()
                    }
                ) as T
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
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

        initializeViewModel()

        return dialog
    }

    override fun onResume() {
        super.onResume()

        dialog?.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
    }

    private fun initializeViewModel() {
        viewModel = ViewModelProvider(this, viewModelFactory).get(AddAccountViewModel::class.java)

        viewModel.accountNameError.observe(
            this,
            Observer { errorRes ->
                errorRes?.let {
                    binding.accountNameEditText.error = getString(errorRes)
                }
            }
        )

        viewModel.accountBalanceError.observe(
            this,
            Observer { errorRes ->
                errorRes?.let {
                    binding.accountBalanceEditText.error = getString(errorRes)
                }
            }
        )
    }

    companion object {
        val FRAGMENT_NAME: String = AddAccountDialog::class.java.simpleName
    }
}
