package com.androidessence.cashcaretaker.ui.addaccount

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.androidessence.cashcaretaker.databinding.DialogAddAccountBinding
import com.androidessence.cashcaretaker.util.DecimalDigitsInputFilter
import dagger.hilt.EntryPoint
import javax.inject.Inject
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

/**
 * Dialog to insert an account.
 */
@EntryPoint
class AddAccountDialog : DialogFragment() {
    private lateinit var binding: DialogAddAccountBinding

    @Inject
    lateinit var viewModel: AddAccountViewModel

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
        subscribeToViewModel()

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

    private fun subscribeToViewModel() {
        lifecycleScope.launch {
            viewModel.dismissEvents.collect { shouldDismiss ->
                if (shouldDismiss) {
                    dismiss()
                }
            }
        }
    }

    companion object {
        val FRAGMENT_NAME: String = AddAccountDialog::class.java.simpleName
    }
}
