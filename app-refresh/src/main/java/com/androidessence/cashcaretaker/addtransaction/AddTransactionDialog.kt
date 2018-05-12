package com.androidessence.cashcaretaker.addtransaction

import android.app.DatePickerDialog
import android.app.Dialog
import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import com.androidessence.cashcaretaker.DatePickerFragment
import com.androidessence.cashcaretaker.DecimalDigitsInputFilter
import com.androidessence.cashcaretaker.R
import com.androidessence.cashcaretaker.data.CCDatabase
import com.androidessence.cashcaretaker.data.CCRepository
import com.androidessence.cashcaretaker.databinding.DialogAddTransactionBinding
import com.androidessence.cashcaretaker.transaction.Transaction
import com.androidessence.utility.asUIString
import io.reactivex.disposables.CompositeDisposable
import java.util.*


/**
 * Dialog for adding a new transaction.
 *
 * @property[accountName] The name of the account that we're creating a transaction for.
 * @property[withdrawalArgument] The initial argument for whether this transaction is a withdrawal.
 * @property[isWithdrawal] Flag determining if this transaction is a withdrawal based on the switch.
 * @property[selectedDate] The date that will be applied to the transaction.
 */
class AddTransactionDialog : DialogFragment(), DatePickerDialog.OnDateSetListener {
    private val compositeDisposable = CompositeDisposable()
    private val isEditing: Boolean by lazy { arguments?.containsKey(ARG_TRANSACTION) ?: false }
    private val initialTransaction: Transaction? by lazy { arguments?.getParcelable<Transaction>(ARG_TRANSACTION) }
    private val accountName: String by lazy { arguments?.getString(ARG_ACCOUNT_NAME).orEmpty() }
    private val withdrawalArgument: Boolean by lazy { arguments?.getBoolean(ARG_IS_WITHDRAWAL) ?: true }
    private val isWithdrawal: Boolean
        get() = binding.withdrawalSwitch.isChecked

    private lateinit var viewModel: AddTransactionViewModel
    private lateinit var binding: DialogAddTransactionBinding

    private var selectedDate: Date = Date()
        set(value) {
            binding.transactionDate.setText(value.asUIString())
            field = value
        }


    private val viewModelFactory: ViewModelProvider.Factory by lazy {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                val database = CCDatabase.getInMemoryDatabase(context!!)
                val repository = CCRepository(database)

                @Suppress("UNCHECKED_CAST")
                return AddTransactionViewModel(repository) as T
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DialogAddTransactionBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (isEditing) {
            initialTransaction?.let { transaction ->
                binding.withdrawalSwitch.isChecked = transaction.withdrawal
                binding.transactionDescription.setText(transaction.description)
                binding.transactionAmount.setText(transaction.amount.toString())
                selectedDate = transaction.date

                binding.submitButton.setOnClickListener {
                    viewModel.updateTransaction(
                            transaction.id,
                            transaction.accountName,
                            binding.transactionDescription.text.toString(),
                            binding.transactionAmount.text.toString(),
                            isWithdrawal,
                            selectedDate
                    )
                }
            }
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

        subscribeToViewModel()
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)

        val titleResource = if (isEditing) R.string.edit_transaction else R.string.add_transaction
        dialog.setTitle(getString(titleResource))

        viewModel = ViewModelProviders.of(this, viewModelFactory).get(AddTransactionViewModel::class.java)
        return dialog
    }

    override fun onResume() {
        super.onResume()

        dialog?.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
    }

    private fun subscribeToViewModel() {
        compositeDisposable.addAll(
                viewModel.transactionDescriptionError.subscribe(binding.transactionDescription::setError),
                viewModel.transactionAmountError.subscribe(binding.transactionAmount::setError),
                viewModel.transactionInserted.subscribe { dismiss() },
                viewModel.transactionUpdated.subscribe { dismiss() }
        )
    }

    private fun showDatePicker() {
        val datePickerFragment = DatePickerFragment.newInstance(selectedDate)
        datePickerFragment.setTargetFragment(this, REQUEST_DATE)
        datePickerFragment.show(fragmentManager, AddTransactionDialog::class.java.simpleName)
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        val calendar = Calendar.getInstance()
        calendar.set(year, month, dayOfMonth)
        selectedDate = calendar.time
    }

    companion object {
        /**
         * Key for the account name argument.
         */
        private const val ARG_ACCOUNT_NAME = "AccountName"

        /**
         * Key for the withdrawal flag argument.
         */
        private const val ARG_IS_WITHDRAWAL = "IsWithdrawal"

        /**
         * Key used when we're editing an existing transaction instead of starting a new one.
         */
        private const val ARG_TRANSACTION = "Transaction"

        /**
         * Request code for the date picker.
         */
        private const val REQUEST_DATE = 0

        /**
         * The tag used when displaying this dialog.
         */
        val FRAGMENT_NAME: String = AddTransactionDialog::class.java.simpleName

        /**
         * Creates a new dialog fragment to add a transaction.
         *
         * @param[accountName] The name of the account to create a transaction for.
         * @param[isWithdrawal] Flag for the initial status of the withdrawal switch.
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
         * Creates a new dialog fragment to edit a transaction.
         *
         * @param[transaction] The transaction that we'll be editing.
         */
        fun newInstance(transaction: Transaction): AddTransactionDialog {
            val args = Bundle()
            args.putParcelable(ARG_TRANSACTION, transaction)

            val fragment = AddTransactionDialog()
            fragment.arguments = args

            return fragment
        }
    }

    override fun onPause() {
        super.onPause()
        compositeDisposable.dispose()
    }
}