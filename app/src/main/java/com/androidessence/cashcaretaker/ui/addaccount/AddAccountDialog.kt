package com.androidessence.cashcaretaker.ui.addaccount

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.lifecycleScope
import com.androidessence.cashcaretaker.databinding.DialogAddAccountBinding
import com.androidessence.cashcaretaker.ui.utils.launchWhenResumed
import com.androidessence.cashcaretaker.util.DecimalDigitsInputFilter
import kotlinx.coroutines.flow.onEach
import org.koin.android.viewmodel.ext.android.viewModel

/**
 * Dialog to insert an account.
 */
class AddAccountDialog : DialogFragment() {
    private lateinit var binding: DialogAddAccountBinding

    private val viewModel: AddAccountViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
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

    private fun processViewState(viewState: AddAccountViewState) {
        viewState.accountNameErrorTextRes
            ?.let(::getString)
            ?.let(binding.accountNameEditText::setError)

        viewState.accountBalanceErrorTextRes
            ?.let(::getString)
            ?.let(binding.accountBalanceEditText::setError)
    }

    private fun subscribeToViewModel() {
        viewModel.dismissEvents
            .onEach { shouldDismiss ->
                if (shouldDismiss) {
                    dismiss()
                }
            }
            .launchWhenResumed(lifecycleScope)

        viewModel.viewState
            .onEach { viewState ->
                processViewState(viewState)
            }
            .launchWhenResumed(lifecycleScope)
    }

    companion object {
        val FRAGMENT_NAME: String = AddAccountDialog::class.java.simpleName
    }
}
