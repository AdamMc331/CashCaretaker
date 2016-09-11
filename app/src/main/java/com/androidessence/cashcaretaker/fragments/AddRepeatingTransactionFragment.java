package com.androidessence.cashcaretaker.fragments;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
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
import com.androidessence.cashcaretaker.alarms.RepeatingTransactionService;
import com.androidessence.cashcaretaker.core.CoreFragment;
import com.androidessence.cashcaretaker.data.CCContract;
import com.androidessence.cashcaretaker.dataTransferObjects.Account;
import com.androidessence.cashcaretaker.dataTransferObjects.Category;
import com.androidessence.cashcaretaker.dataTransferObjects.RepeatingPeriod;
import com.androidessence.cashcaretaker.dataTransferObjects.RepeatingTransaction;
import com.androidessence.utility.Utility;

import org.joda.time.LocalDate;

/**
 * Fragment that allows the user to add a repeating transaction.
 *
 * Created by adam.mcneilly on 9/8/16.
 */
public class AddRepeatingTransactionFragment extends CoreFragment implements RepeatingPeriodDialog.OnRepeatingPeriodSelectedListener, AccountDialog.OnAccountSelectedListener, CategoryDialog.OnCategorySelectedListener, DatePickerDialog.OnDateSetListener{
    private EditText repeatingPeriodEditText;
    private RepeatingPeriod repeatingPeriod;
    private EditText accountEditText;
    private Account account;
    private EditText description;
    private EditText amount;
    private EditText notes;
    private EditText dateEditText;
    private LocalDate date;
    private EditText categoryEditText;
    private Category category;
    private RadioButton withdrawal;
    private Button submit;

    private static final String ARG_REPEATING_PERIOD = "argRepeatingPeriod";
    private static final String ARG_ACCOUNT = "argAccount";
    private static final String ARG_DATE = "argDate";
    private static final String ARG_CATEGORY = "argCategory";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        root = (CoordinatorLayout) inflater.inflate(R.layout.fragment_add_repeating_transaction, container, false);

        getElements(root);
        setTextFilters();
        setClickListeners();

        // Retrieve any saved values.
        if(savedInstanceState != null && savedInstanceState.containsKey(ARG_REPEATING_PERIOD)) {
            setRepeatingPeriod((RepeatingPeriod) savedInstanceState.getParcelable(ARG_REPEATING_PERIOD));
        } else {
            getDefaultRepeatingPeriod();
        }

        if(savedInstanceState != null && savedInstanceState.containsKey(ARG_ACCOUNT)) {
            setAccount((Account) savedInstanceState.getParcelable(ARG_ACCOUNT));
        }

        if(savedInstanceState != null && savedInstanceState.containsKey(ARG_DATE)) {
            setDate((LocalDate) savedInstanceState.getSerializable(ARG_DATE));
        } else {
            setDate(LocalDate.now());
        }

        if(savedInstanceState != null && savedInstanceState.containsKey(ARG_CATEGORY)) {
            setCategory((Category) savedInstanceState.getParcelable(ARG_CATEGORY));
        } else {
            getDefaultCategory();
        }

        return root;
    }

    @Override
    protected void getElements(View view) {
        repeatingPeriodEditText = (EditText) view.findViewById(R.id.transaction_repeating_period);
        accountEditText = (EditText) view.findViewById(R.id.transaction_account);
        description = (EditText) view.findViewById(R.id.transaction_description);
        amount = (EditText) view.findViewById(R.id.transaction_amount);
        notes = (EditText) view.findViewById(R.id.transaction_notes);
        dateEditText = (EditText) view.findViewById(R.id.transaction_date);
        categoryEditText = (EditText) view.findViewById(R.id.transaction_category);
        withdrawal = (RadioButton) view.findViewById(R.id.transaction_withdrawal);
        submit = (Button) view.findViewById(R.id.submit);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(ARG_REPEATING_PERIOD, repeatingPeriod);
        outState.putParcelable(ARG_ACCOUNT, account);
        outState.putSerializable(ARG_DATE, date);
        outState.putParcelable(ARG_CATEGORY, category);
    }

    /**
     * Sets input filters to necessary EditTexts.
     */
    private void setTextFilters() {
        InputFilter[] inputFilters = new InputFilter[] {new DecimalDigitsInputFilter()};
        amount.setFilters(inputFilters);
    }

    /**
     * Sets the appropriate click listeners for this fragment.
     */
    private void setClickListeners() {
        repeatingPeriodEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showRepeatingPeriodDialog();
            }
        });
        accountEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAccountDialog();
            }
        });
        categoryEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCategoryDialog();
            }
        });
        dateEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerFragment();
            }
        });
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitRepeatingTransaction();
            }
        });
    }

    /**
     * Displays the dialog for selecting a repeating period.
     */
    private void showRepeatingPeriodDialog() {
        RepeatingPeriodDialog repeatingPeriodDialog = new RepeatingPeriodDialog();
        repeatingPeriodDialog.setTargetFragment(this, 0);
        repeatingPeriodDialog.show(getFragmentManager(), "repeatingPeriod");
    }

    /**
     * Displays the dialog for selecting an account.
     */
    private void showAccountDialog() {
        AccountDialog accountDialog = new AccountDialog();
        accountDialog.setTargetFragment(this, 0);
        accountDialog.show(getFragmentManager(), "account");
    }

    /**
     * Submits a repeating transaction entry to the database.
     */
    private void submitRepeatingTransaction() {
        if(!validateInput()) {
            return;
        }

        RepeatingTransaction repeatingTransaction = new RepeatingTransaction(
                repeatingPeriod.getIdentifier(),
                account.getIdentifier(),
                description.getText().toString(),
                Double.parseDouble(amount.getText().toString()),
                notes.getText().toString(),
                date,
                category.getIdentifier(),
                withdrawal.isChecked()
        );

        // Submit row
        getActivity().getContentResolver().insert(CCContract.RepeatingTransactionEntry.CONTENT_URI, repeatingTransaction.getContentValues());

        // Start service
        getActivity().startService(new Intent(getActivity(), RepeatingTransactionService.class));

        // Finish activity.
        getActivity().finish();
    }

    /**
     * Validates all required input.
     * @return True if the input in the fragment is valid, false otherwise.
     */
    private boolean validateInput() {
        boolean isValid = true;

        // Do not check RepeatingPeriod, Category they are both set by default.
        if(account == null) {
            accountEditText.setError("Account must be selected.");
            isValid = false;
        }

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
     * Retrieves the default category to be used.
     */
    private void getDefaultCategory(){

        Cursor cursor = getActivity().getContentResolver().query(
                CCContract.CategoryEntry.CONTENT_URI,
                null,
                CCContract.CategoryEntry.COLUMN_IS_DEFAULT + " = ?",
                new String[] {String.valueOf("1")},
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
     * Gets the default repeating period, which is just the first entry.
     */
    private void getDefaultRepeatingPeriod() {
        Cursor cursor = getActivity().getContentResolver().query(
                CCContract.RepeatingPeriodEntry.CONTENT_URI,
                null,
                null,
                null,
                null
        );

        assert cursor != null;

        if(cursor.moveToFirst()) {
            setRepeatingPeriod(new RepeatingPeriod(cursor));
        } else {
            // Bad input, set empty.
            setRepeatingPeriod(new RepeatingPeriod());
        }

        cursor.close();
    }

    private void setDate(LocalDate date) {
        this.date = date;
        dateEditText.setText(Utility.getUIDateString(this.date));
    }

    /**
     * Sets the repeating period for the transaction.
     */
    private void setRepeatingPeriod(RepeatingPeriod repeatingPeriod) {
        this.repeatingPeriod = repeatingPeriod;
        repeatingPeriodEditText.setText(repeatingPeriod.getName());
    }

    /**
     * Sets the transaction category.
     */
    private void setCategory(Category category){
        this.category = category;
        this.categoryEditText.setText(this.category.getDescription());
    }

    /**
     * Sets the account for the transaction.
     */
    private void setAccount(Account account) {
        this.account = account;
        accountEditText.setText(this.account.getName());
        // Remove error if it was there
        accountEditText.setError(null);
    }

    @Override
    public void onRepeatingPeriodSelected(RepeatingPeriod repeatingPeriod) {
        setRepeatingPeriod(repeatingPeriod);
    }

    @Override
    public void onAccountSelected(Account account) {
        setAccount(account);
    }

    /**
     * Displays the category dialog.
     */
    private void showCategoryDialog(){
        CategoryDialog dialog = new CategoryDialog();
        dialog.setTargetFragment(this, 0);
        dialog.show(getFragmentManager(), "transactionCategory");
    }

    @Override
    public void onCategorySelected(Category category) {
        setCategory(category);
    }

    /**
     * Handles the selection of a date in the DatePickerDialog.
     */
    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        setDate(new LocalDate(year, monthOfYear + 1, dayOfMonth));
    }

    /**
     * Displays a date picker dialog.
     */
    private void showDatePickerFragment(){
        DatePickerFragment datePickerFragment = DatePickerFragment.NewInstance(date);
        datePickerFragment.setTargetFragment(this, 0);
        datePickerFragment.show(getFragmentManager(), "transactionDate");
    }
}
