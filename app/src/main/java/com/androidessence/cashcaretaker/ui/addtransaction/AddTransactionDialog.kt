package com.androidessence.cashcaretaker.ui.addtransaction

import android.app.DatePickerDialog
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.lifecycleScope
import com.androidessence.cashcaretaker.R
import com.androidessence.cashcaretaker.core.models.Transaction
import com.androidessence.cashcaretaker.databinding.DialogAddTransactionBinding
import com.androidessence.cashcaretaker.ui.views.DatePickerFragment
import com.androidessence.cashcaretaker.util.DecimalDigitsInputFilter
import com.androidessence.cashcaretaker.util.asUIString
import java.util.Calendar
import java.util.Date
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.koin.android.viewmodel.ext.android.viewModel

/**
 * Dialog for adding a new transaction.
 */
class AddTransactionDialog : DialogFragment(), DatePickerDialog.OnDateSetListener {
    //region Properties
    private lateinit var accountName: String
    private var isEditing: Boolean = false
    private var initialTransaction: Transaction? = null
    private var withdrawalArgument: Boolean = true

    private val isWithdrawal: Boolean
        get() = binding.withdrawalSwitch.isChecked

    private lateinit var binding: DialogAddTransactionBinding

    private val viewModel: AddTransactionViewModel by viewModel()

    private var selectedDate: Date = Date()
        set(value) {
            binding.transactionDate.setText(value.asUIString())
            field = value
        }
    //endregion

    //region Lifecycle Methods
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DialogAddTransactionBinding.inflate(inflater, container, false)
        setupTitle()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (isEditing) {
            displayInitialTransaction()
        } else {
            binding.withdrawalSwitch.isChecked = withdrawalArgument
            selectedDate = Date()

            binding.submitButton.setOnClickListener {
                viewModel.addTransaction(
                    accountName,
                    binding.transactionDescription.text.toString(),
                    binding.transactionAmount.text.toString(),
                    isWithdrawal,
                    selectedDate
                )
            }
        }

        binding.transactionDate.setOnClickListener { showDatePicker() }
        binding.transactionAmount.filters = arrayOf(DecimalDigitsInputFilter())
        binding.transactionDescription.requestFocus()
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)

        subscribeToViewModel()
        readArguments()
        return dialog
    }

    private fun setupTitle() {
        val titleResource = if (isEditing) R.string.edit_transaction else R.string.add_transaction
        binding.title.setText(titleResource)
    }

    override fun onResume() {
        super.onResume()

        dialog?.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
    }
    //endregion

    //region Helper Functions
    /**
     * Takes all the fields from the [initialTransaction] and displays them.
     *
     * @see [newInstance]
     */
    private fun displayInitialTransaction() {
        initialTransaction?.let { transaction ->
            binding.withdrawalSwitch.isChecked = transaction.withdrawal
            binding.transactionDescription.setText(transaction.description)
            binding.transactionAmount.setText(transaction.amount.toString())
            selectedDate = transaction.date

            binding.submitButton.setOnClickListener {
                val input = AddTransactionViewModel.TransactionInput(
                    transaction.id,
                    transaction.accountName,
                    binding.transactionDescription.text.toString(),
                    binding.transactionAmount.text.toString(),
                    isWithdrawal,
                    selectedDate
                )

                viewModel.updateTransaction(input)
            }
        }
    }

    /**
     * Reads the fragment arguments and sets the appropriate properties.
     */
    private fun readArguments() {
        isEditing = arguments?.containsKey(ARG_TRANSACTION) ?: false
        initialTransaction = arguments?.getParcelable(ARG_TRANSACTION)
        accountName = arguments?.getString(ARG_ACCOUNT_NAME).orEmpty()
        withdrawalArgument = arguments?.getBoolean(ARG_IS_WITHDRAWAL) ?: true
    }

    /**
     * Subscribes to ViewModel events for errors and transaction actions.
     */
    private fun subscribeToViewModel() {
        viewModel.transactionDescriptionError.observe(
            this,
            androidx.lifecycle.Observer { errorRes ->
                errorRes?.let {
                    binding.transactionDescription.error = getString(errorRes)
                }
            }
        )

        viewModel.transactionAmountError.observe(
            this,
            androidx.lifecycle.Observer { errorRes ->
                errorRes?.let {
                    binding.transactionAmount.error = getString(it)
                }
            }
        )

        lifecycleScope.launch {
            viewModel.dismissEvents.collect { shouldDismiss ->
                if (shouldDismiss) {
                    dismiss()
                }
            }
        }
    }

    /**
     * Displays a date picker dialog for the user to select a date. The [DatePickerFragment] uses
     * this fragment for the [DatePickerDialog.OnDateSetListener].
     *
     * @see [onDateSet]
     */
    private fun showDatePicker() {
        val datePickerFragment = DatePickerFragment.newInstance(selectedDate)
        datePickerFragment.setTargetFragment(this, REQUEST_DATE)
        datePickerFragment.show(
            childFragmentManager,
            AddTransactionDialog::class.java.simpleName
        )
    }

    /**
     * Sets the [selectedDate] for the fragment.
     */
    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        val calendar = Calendar.getInstance()
        calendar.set(year, month, dayOfMonth)
        selectedDate = calendar.time
    }

    companion object {
        private const val ARG_ACCOUNT_NAME = "AccountName"
        private const val ARG_IS_WITHDRAWAL = "IsWithdrawal"
        private const val ARG_TRANSACTION = "Transaction"
        private const val REQUEST_DATE = 0

        val FRAGMENT_NAME: String = AddTransactionDialog::class.java.simpleName

        /**
         * Creates a new dialog fragment to add a transaction. This method takes in the account we'll be
         * adding a transaction for, and the initial withdrawal state.
         */
        fun newInstance(accountName: String, isWithdrawal: Boolean): AddTransactionDialog {
            val args = Bundle()
            args.putString(ARG_ACCOUNT_NAME, accountName)
            args.putBoolean(ARG_IS_WITHDRAWAL, isWithdrawal)

            val fragment = AddTransactionDialog()
            fragment.arguments = args

            return fragment
        }

        /**
         * Creates a new dialog fragment to edit a transaction, with the initial transaction to be edited.
         */
        fun newInstance(transaction: Transaction): AddTransactionDialog {
            val args = Bundle()
            args.putParcelable(ARG_TRANSACTION, transaction)

            val fragment = AddTransactionDialog()
            fragment.arguments = args

            return fragment
        }
    }
}
