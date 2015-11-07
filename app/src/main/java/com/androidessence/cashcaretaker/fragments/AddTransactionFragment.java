package com.androidessence.cashcaretaker.fragments;

import android.app.DatePickerDialog;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.InputFilter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;

import com.androidessence.cashcaretaker.DecimalDigitsInputFilter;
import com.androidessence.cashcaretaker.R;
import com.androidessence.cashcaretaker.Utility;
import com.androidessence.cashcaretaker.data.CCContract;
import com.androidessence.cashcaretaker.dataTransferObjects.Category;
import com.androidessence.cashcaretaker.dataTransferObjects.Transaction;

import org.joda.time.LocalDate;

/**
 * Fragment container for the UI to add a transaction.
 *
 * Created by adammcneilly on 11/2/15.
 */
public class AddTransactionFragment extends Fragment implements DatePickerDialog.OnDateSetListener, CategoryDialog.OnCategorySelectedListener{
    // UI elements
    private EditText mDescription;
    private EditText mAmount;
    private EditText mNotes;
    private EditText mDateEditText;
    private LocalDate mDate;
    private EditText mCategoryEditText;
    private Category mCategory;
    private RadioButton mWithdrawal;
    private Button mSubmit;

    // Account that we are adding a transaction for, and arguments for variables we want to record.
    private long mAccount;
    private static final String ARG_ACCOUNT = "accountArg";
    private static final String ARG_DATE = "dateArg";
    private static final String ARG_CATEGORY = "categoryArg";

    public static AddTransactionFragment NewInstance(long account){
        Bundle args = new Bundle();
        args.putLong(ARG_ACCOUNT, account);
        AddTransactionFragment fragment = new AddTransactionFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAccount = getArguments().getLong(ARG_ACCOUNT, 0);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_transaction, container, false);

        getUIElements(view);
        setClickListeners();
        setInputFilters();

        if(savedInstanceState != null && savedInstanceState.containsKey(ARG_DATE)) {
            setDate((LocalDate) savedInstanceState.getSerializable(ARG_DATE));
        } else{
            setDate(LocalDate.now());
        }

        if(savedInstanceState != null && savedInstanceState.containsKey(ARG_CATEGORY)) {
            setCategory((Category) savedInstanceState.getParcelable(ARG_CATEGORY));
        } else {
            getDefaultCategory();
        }

        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(ARG_DATE, mDate);
        outState.putParcelable(ARG_CATEGORY, mCategory);
    }

    /**
     * Retrieves all necessary UI elements in the fragment.
     */
    private void getUIElements(View view){
        mDescription = (EditText) view.findViewById(R.id.transaction_description);
        mAmount = (EditText) view.findViewById(R.id.transaction_amount);
        mNotes = (EditText) view.findViewById(R.id.transaction_notes);
        mDateEditText = (EditText) view.findViewById(R.id.transaction_date);
        mCategoryEditText = (EditText) view.findViewById(R.id.transaction_category);
        mWithdrawal = (RadioButton) view.findViewById(R.id.transaction_withdrawal);
        mSubmit = (Button) view.findViewById(R.id.submit);
    }

    /**
     * Sets all necessary click listeners used in the fragment.
     */
    private void setClickListeners(){
        mDateEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerFragment();
            }
        });
        mCategoryEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCategoryDialog();
            }
        });
        mSubmit.setOnClickListener(new View.OnClickListener() {
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
        DatePickerFragment datePickerFragment = DatePickerFragment.NewInstance(mDate);
        datePickerFragment.setTargetFragment(this, 0);
        datePickerFragment.show(getFragmentManager(), "transactionDate");
    }

    /**
     * Sets an input filter to necessary EditTexts.
     */
    private void setInputFilters(){
        InputFilter[] inputFilters = new InputFilter[] {new DecimalDigitsInputFilter()};
        mAmount.setFilters(inputFilters);
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
    private void setDate(LocalDate date) {
        this.mDate = date;
        this.mDateEditText.setText(Utility.getUIDateString(date));
    }

    /**
     * Handles the selection of a date in the DatePickerDialog.
     */
    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        setDate(new LocalDate(year, monthOfYear + 1, dayOfMonth));
    }

    /**
     * Retrieves the default category to be used.
     */
    private void getDefaultCategory(){
        //TODO: Don't hard code this, put it in settings or something
        String defaultCategory = "None";

        Cursor cursor = getActivity().getContentResolver().query(
                CCContract.CategoryEntry.CONTENT_URI,
                null,
                CCContract.CategoryEntry.COLUMN_DESCRIPTION + " = ?",
                new String[] {defaultCategory},
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
        this.mCategory = category;
        this.mCategoryEditText.setText(mCategory.getDescription());
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

        if(mDescription.getText().toString().isEmpty()) {
            mDescription.setError("Description cannot be blank.");
            isValid = false;
        }

        if(mAmount.getText().toString().isEmpty()) {
            mAmount.setError("Amount cannot be blank.");
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
                mAccount,
                mDescription.getText().toString(),
                Double.parseDouble(mAmount.getText().toString()),
                mNotes.getText().toString(),
                mDate,
                mCategory.getIdentifier(),
                mWithdrawal.isChecked()
        );

        // Insert
        getActivity().getContentResolver().insert(CCContract.TransactionEntry.CONTENT_URI, transaction.getContentValues());

        ((OnTransactionSubmittedListener)getActivity()).onTransactionSubmitted();
    }

    /**
     * Interface that calls back to the activity when a transaction is submitted.
     */
    public interface OnTransactionSubmittedListener {
        void onTransactionSubmitted();
    }
}
