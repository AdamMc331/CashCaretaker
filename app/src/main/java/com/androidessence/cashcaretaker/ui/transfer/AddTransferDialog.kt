package com.androidessence.cashcaretaker.ui.transfer

import android.app.DatePickerDialog
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.androidessence.cashcaretaker.R
import com.androidessence.cashcaretaker.core.models.Account
import com.androidessence.cashcaretaker.databinding.DialogAddTransferBinding
import com.androidessence.cashcaretaker.graph
import com.androidessence.cashcaretaker.ui.addtransaction.AddTransactionDialog
import com.androidessence.cashcaretaker.ui.views.DatePickerFragment
import com.androidessence.cashcaretaker.ui.views.SpinnerInputEditText
import com.androidessence.cashcaretaker.util.DecimalDigitsInputFilter
import com.androidessence.cashcaretaker.util.asUIString
import java.util.Calendar
import java.util.Date

/**
 * Dialog that allows a user to transfer money from one account to another.
 */
class AddTransferDialog : DialogFragment(), DatePickerDialog.OnDateSetListener {
    private lateinit var viewModel: AddTransferViewModel
    private lateinit var binding: DialogAddTransferBinding

    private lateinit var fromAccount: SpinnerInputEditText<Account>
    private lateinit var toAccount: SpinnerInputEditText<Account>

    private var selectedDate: Date = Date()
        set(value) {
            binding.transferDate.setText(value.asUIString())
            field = value
        }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DialogAddTransferBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)

        val viewModelFactory = requireContext().graph()
            .viewModelFactoryGraph
            .addTransferViewModelFactory(
                transferInserted = {
                    dismiss()
                }
            )

        viewModel = ViewModelProvider(this, viewModelFactory).get(AddTransferViewModel::class.java)

        return dialog
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fromAccount = view.findViewById(R.id.transferFromAccount)
        toAccount = view.findViewById(R.id.transferToAccount)

        binding.transferAmount.filters = arrayOf(DecimalDigitsInputFilter())

        selectedDate = Date()
        binding.transferDate.setOnClickListener { showDatePicker() }

        binding.submitButton.setOnClickListener {
            addTransfer(
                fromAccount.selectedItem,
                toAccount.selectedItem,
                binding.transferAmount.text.toString(),
                selectedDate
            )
        }

        subscribeToViewModel()
    }

    override fun onResume() {
        super.onResume()
        dialog?.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
    }

    private fun subscribeToViewModel() {
        viewModel.accounts.observe(
            viewLifecycleOwner,
            androidx.lifecycle.Observer { accounts ->
                accounts?.let {
                    fromAccount.items = it
                    toAccount.items = it
                }
            }
        )

        viewModel.fromAccountError.observe(
            viewLifecycleOwner,
            androidx.lifecycle.Observer { errorRes ->
                errorRes?.let {
                    fromAccount.error = getString(it)
                }
            }
        )

        viewModel.toAccountError.observe(
            viewLifecycleOwner,
            androidx.lifecycle.Observer { errorRes ->
                errorRes?.let {
                    toAccount.error = getString(it)
                }
            }
        )

        viewModel.amountError.observe(
            viewLifecycleOwner,
            androidx.lifecycle.Observer { errorRes ->
                errorRes?.let {
                    binding.transferAmount.error = getString(it)
                }
            }
        )
    }

    private fun addTransfer(
        fromAccount: Account?,
        toAccount: Account?,
        amount: String,
        date: Date
    ) {
        viewModel.addTransfer(fromAccount, toAccount, amount, date)
    }

    private fun showDatePicker() {
        val datePickerFragment = DatePickerFragment.newInstance(selectedDate)
        datePickerFragment.setTargetFragment(this, REQUEST_DATE)
        datePickerFragment.show(
            childFragmentManager,
            AddTransactionDialog::class.java.simpleName
        )
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        val calendar = Calendar.getInstance()
        calendar.set(year, month, dayOfMonth)
        selectedDate = calendar.time
    }

    companion object {
        private const val REQUEST_DATE = 0
    }
}