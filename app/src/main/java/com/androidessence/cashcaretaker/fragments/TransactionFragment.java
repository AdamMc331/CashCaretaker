package com.androidessence.cashcaretaker.fragments;

import android.app.DatePickerDialog;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.widget.AppCompatAutoCompleteTextView;
import android.text.InputFilter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FilterQueryProvider;
import android.widget.RadioButton;

import com.androidessence.cashcaretaker.DecimalDigitsInputFilter;
import com.androidessence.cashcaretaker.R;
import com.androidessence.cashcaretaker.core.CoreFragment;
import com.androidessence.cashcaretaker.data.CCContract;
import com.androidessence.cashcaretaker.dataTransferObjects.Category;
import com.androidessence.cashcaretaker.dataTransferObjects.Transaction;
import com.androidessence.cashcaretaker.dataTransferObjects.TransactionDetails;
import com.androidessence.utility.Utility;

import java.util.Calendar;
import java.util.Date;

/**
 * Fragment that allows the user to add and edit a transaction.
 *
 * Created by adam.mcneilly on 9/8/16.
 */
public class TransactionFragment extends CoreFragment implements DatePickerDialog.OnDateSetListener, CategoryDialog.OnCategorySelectedListener{
    public static final String FRAGMENT_TAG_ADD = "AddTransactionFragment";
    public static final String FRAGMENT_TAG_EDIT = "EditTransactionFragment";

    // UI elements
    private AppCompatAutoCompleteTextView description;
    private EditText amount;
    private EditText notes;
    private EditText dateEditText;
    private Date date;
    private EditText categoryEditText;
    private Category category;
    private RadioButton withdrawal;
    private RadioButton deposit;
    private Button submit;

    // Account that we are adding a transaction for,
    private long account;

    // Form mode we have
    private int formMode;
    private TransactionDetails transaction;

    // Arguments for variables to record or receive
    private static final String ARG_ACCOUNT = "accountArg";
    private static final String ARG_DATE = "dateArg";
    private static final String ARG_CATEGORY = "categoryArg";
    private static final String ARG_FORM_MODE = "formModeArg";
    private static final String ARG_TRANSACTION = "transactionArg";

    // Possible modes of fragment
    public static final int MODE_ADD = 1;
    public static final int MODE_EDIT = 2;

    public static final String[] TRANSACTION_COLUMNS = new String[] {
            CCContract.TransactionEntry.TABLE_NAME + "." + CCContract.TransactionEntry._ID,
            CCContract.TransactionEntry.COLUMN_DESCRIPTION
    };

    private static final int DESCRIPTION_INDEX = 1;

    public static TransactionFragment newInstance(long account, int fragmentMode, Transaction transaction){
        // If mode is add and transaction is not null, bad input
        if(fragmentMode == MODE_ADD && transaction != null) {
            throw new IllegalArgumentException("Cannot specify a transaction for ADD form mode.");
        } else if(fragmentMode == MODE_EDIT && transaction == null) {
            throw new IllegalArgumentException("Must specify a transaction for EDIT form mode.");
        } else if(fragmentMode != MODE_ADD && fragmentMode != MODE_EDIT) {
            throw new IllegalArgumentException("Must specify a valid form mode.");
        }

        Bundle args = new Bundle();
        args.putLong(ARG_ACCOUNT, account);
        args.putInt(ARG_FORM_MODE, fragmentMode);
        args.putParcelable(ARG_TRANSACTION, transaction);
        TransactionFragment fragment = new TransactionFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        account = getArguments().getLong(ARG_ACCOUNT, 0);

        // Didn't set default - handled all errors in NewInstance method
        formMode = getArguments().getInt(ARG_FORM_MODE);

        if(formMode == MODE_EDIT) {
            transaction = getArguments().getParcelable(ARG_TRANSACTION);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        root = (CoordinatorLayout) inflater.inflate(R.layout.fragment_add_transaction, container, false);

        getElements(root);
        setClickListeners();
        setInputFilters();
        setupDescriptionTextView();

        // If saved instance state is not null pull values.
        // Should be there, do checks anyways
        if(savedInstanceState != null) {
            if(savedInstanceState.containsKey(ARG_DATE)) {
                setDate((Date) savedInstanceState.getSerializable(ARG_DATE));
            } else{
                setDate(new Date());
            }

            if(savedInstanceState.containsKey(ARG_CATEGORY)) {
                setCategory((Category) savedInstanceState.getParcelable(ARG_CATEGORY));
            } else {
                getDefaultCategory();
            }
        } else {
            // If saved instance state is null, and we are editing, populate fields.
            // If we are not editing, get defaults.
            if(areEditing()) {
                populateFieldsForTransaction();
            } else {
                setDate(new Date());
                getDefaultCategory();
            }
        }

        return root;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(ARG_DATE, date);
        outState.putParcelable(ARG_CATEGORY, category);
    }

    /**
     * Determines whether or not we are editing a transaction.
     */
    private boolean areEditing() {
        return formMode == MODE_EDIT;
    }

    /**
     * Populates the fields for the transaction to be edited.
     */
    private void populateFieldsForTransaction() {
        description.setText(transaction.getDescription());
        amount.setText(String.valueOf(transaction.getAmount()));
        notes.setText(transaction.getNotes());
        setDate(transaction.getDate());
        setCategory(transaction.getCategory());
        if(transaction.isWithdrawal()) {
            withdrawal.setChecked(true);
        } else {
            deposit.setChecked(true);
        }

        // If we are editing, change submit button text
        submit.setText(getString(R.string.submit_changes));
    }

    @Override
    protected void getElements(View view) {
        description = (AppCompatAutoCompleteTextView) view.findViewById(R.id.transaction_description);
        amount = (EditText) view.findViewById(R.id.transaction_amount);
        notes = (EditText) view.findViewById(R.id.transaction_notes);
        dateEditText = (EditText) view.findViewById(R.id.transaction_date);
        categoryEditText = (EditText) view.findViewById(R.id.transaction_category);
        withdrawal = (RadioButton) view.findViewById(R.id.transaction_withdrawal);
        deposit = (RadioButton) view.findViewById(R.id.transaction_deposit);
        submit = (Button) view.findViewById(R.id.submit);
    }

    /**
     * Sets all necessary click listeners used in the fragment.
     */
    private void setClickListeners(){
        dateEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerFragment();
            }
        });
        categoryEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCategoryDialog();
            }
        });
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitTransaction();
            }
        });
    }

    /**
     * Displays a date picker dialog.
     */
    private void showDatePickerFragment(){
        DatePickerFragment datePickerFragment = DatePickerFragment.newInstance(date);
        datePickerFragment.setTargetFragment(this, 0);
        datePickerFragment.show(getFragmentManager(), "transactionDate");
    }

    /**
     * Sets an input filter to necessary EditTexts.
     */
    private void setInputFilters(){
        InputFilter[] inputFilters = new InputFilter[] {new DecimalDigitsInputFilter()};
        amount.setFilters(inputFilters);
    }

    /**
     * Displays the category dialog.
     */
    private void showCategoryDialog(){
        CategoryDialog dialog = new CategoryDialog();
        dialog.setTargetFragment(this, 0);
        dialog.show(getFragmentManager(), "transactionCategory");
    }

    /**
     * Sets the transaction date.
     */
    private void setDate(Date date) {
        this.date = date;
        this.dateEditText.setText(Utility.getUIDateString(date));
    }

    /**
     * Handles the selection of a date in the DatePickerDialog.
     */
    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, monthOfYear, dayOfMonth);
        setDate(calendar.getTime());
    }

    /**
     * Retrieves the default category to be used.
     */
    private void getDefaultCategory(){
        Cursor cursor = getActivity().getContentResolver().query(
                CCContract.CategoryEntry.CONTENT_URI,
                null,
                CCContract.CategoryEntry.COLUMN_IS_DEFAULT + " = ?",
                new String[] {"1"},
                null
        );

        assert cursor != null;
        if(cursor.moveToFirst()){
            setCategory(new Category(cursor));
        } else{
            // Bad input, just set empty for now.
            setCategory(new Category());
        }

        cursor.close();
    }

    /**
     * Sets the transaction category.
     */
    private void setCategory(Category category){
        this.category = category;
        this.categoryEditText.setText(this.category.getDescription());
    }

    /**
     * Handles the selection of a category in the CategoryDialog.
     */
    @Override
    public void onCategorySelected(Category category) {
        setCategory(category);
    }

    /**
     * Validates all required input for the transaction.
     */
    private boolean validateInput(){
        boolean isValid = true;

        if(description.getText().toString().isEmpty()) {
            description.setError("Description cannot be blank.");
            isValid = false;
        }

        if(amount.getText().toString().isEmpty()) {
            amount.setError("Amount cannot be blank.");
            isValid = false;
        }

        return isValid;
    }

    /**
     * Submits a transaction to the database.
     */
    private void submitTransaction(){
        if(!validateInput()) {
            return;
        }

        // Build transaction
        Transaction transaction = new Transaction(
                account,
                description.getText().toString(),
                Double.parseDouble(amount.getText().toString()),
                notes.getText().toString(),
                date,
                category.getIdentifier(),
                withdrawal.isChecked()
        );

        // Update if we are editing, insert otherwise
        if(areEditing()) {
            // update for this transaction
            getActivity().getContentResolver().update(
                    CCContract.TransactionEntry.CONTENT_URI,
                    transaction.getContentValues(),
                    CCContract.TransactionEntry._ID + " = ?",
                    new String[]{String.valueOf(this.transaction.getIdentifier())}
            );
        } else{
            // Insert
            getActivity().getContentResolver().insert(CCContract.TransactionEntry.CONTENT_URI, transaction.getContentValues());
        }

        ((OnTransactionSubmittedListener)getActivity()).onTransactionSubmitted();
    }

    private void setupDescriptionTextView() {
        SimpleCursorAdapter simpleCursorAdapter = new SimpleCursorAdapter(
                getActivity(),
                R.layout.list_textview,
                null,
                new String[] {CCContract.TransactionEntry.COLUMN_DESCRIPTION},
                new int[] {R.id.list_item_text_view},
                0
        );

        simpleCursorAdapter.setFilterQueryProvider(new FilterQueryProvider() {
            @Override
            public Cursor runQuery(CharSequence constraint) {
                return getCursor(constraint.toString());
            }
        });

        simpleCursorAdapter.setCursorToStringConverter(new SimpleCursorAdapter.CursorToStringConverter() {
            @Override
            public CharSequence convertToString(Cursor cursor) {
                return cursor.getString(DESCRIPTION_INDEX);
            }
        });

        description.setAdapter(simpleCursorAdapter);
    }

    private Cursor getCursor(String description) {
        return getActivity().getContentResolver().query(
                CCContract.TransactionEntry.buildTransactionsForAccountWithDescriptionUri(account, description),
                TRANSACTION_COLUMNS,
                null,
                null,
                CCContract.TransactionEntry.COLUMN_DESCRIPTION
        );
    }

    /**
     * Interface that calls back to the activity when a transaction is submitted.
     */
    public interface OnTransactionSubmittedListener {
        void onTransactionSubmitted();
    }
}