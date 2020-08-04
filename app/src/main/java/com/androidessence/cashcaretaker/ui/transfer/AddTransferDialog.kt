package com.androidessence.cashcaretaker.ui.transfer

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.lifecycleScope
import com.androidessence.cashcaretaker.R
import com.androidessence.cashcaretaker.core.models.Account
import com.androidessence.cashcaretaker.databinding.DialogAddTransferBinding
import com.androidessence.cashcaretaker.ui.addtransaction.AddTransactionDialog
import com.androidessence.cashcaretaker.ui.views.DatePickerFragment
import com.androidessence.cashcaretaker.ui.views.SpinnerInputEditText
import com.androidessence.cashcaretaker.util.DecimalDigitsInputFilter
import com.androidessence.cashcaretaker.util.asUIString
import java.util.Calendar
import java.util.Date
import kotlinx.android.synthetic.main.dialog_add_transfer.transferAmount
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.koin.android.viewmodel.ext.android.viewModel

/**
 * Dialog that allows a user to transfer money from one account to another.
 */
@ExperimentalCoroutinesApi
class AddTransferDialog : DialogFragment(), DatePickerDialog.OnDateSetListener {
    private lateinit var binding: DialogAddTransferBinding

    private lateinit var fromAccount: SpinnerInputEditText<Account>
    private lateinit var toAccount: SpinnerInputEditText<Account>

    private val viewModel: AddTransferViewModel by viewModel()

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
        lifecycleScope.launch {
            viewModel.viewState.collect { viewState ->
                viewState.accounts?.let { accounts ->
                    fromAccount.items = accounts
                    toAccount.items = accounts
                }

                viewState.fromAccountErrorRes
                    ?.let(::getString)
                    .let(fromAccount::setError)

                viewState.toAccountErrorRes
                    ?.let(::getString)
                    .let(toAccount::setError)

                viewState.amountErrorRes
                    ?.let(::getString)
                    .let(binding.transferAmount::setError)
            }
        }

        lifecycleScope.launch {
            viewModel.dismissEvents.collect { shouldDismiss ->
                if (shouldDismiss) {
                    dismiss()
                }
            }
        }
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
