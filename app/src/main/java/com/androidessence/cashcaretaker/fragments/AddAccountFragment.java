package com.androidessence.cashcaretaker.fragments;

import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.text.InputFilter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.androidessence.cashcaretaker.DecimalDigitsInputFilter;
import com.androidessence.cashcaretaker.R;
import com.androidessence.cashcaretaker.core.CoreFragment;
import com.androidessence.cashcaretaker.data.CCContract;
import com.androidessence.cashcaretaker.dataTransferObjects.Account;

/**
 * Fragment that allows the user to add an account.
 *
 * Created by adam.mcneilly on 9/8/16.
 */
public class AddAccountFragment extends CoreFragment{
    // UI Elements
    private EditText accountName;
    private EditText startingBalance;
    private Button submit;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setRoot((CoordinatorLayout) inflater.inflate(R.layout.fragment_add_account, container, false));

        getElements(getRoot());
        setInputFilters();
        setClickListeners();

        return getRoot();
    }

    @Override
    protected void getElements(View view) {
        accountName = (EditText) view.findViewById(R.id.account_name);
        startingBalance = (EditText) view.findViewById(R.id.starting_balance);
        submit = (Button) view.findViewById(R.id.submit);
    }

    /**
     * Sets any InputFilters to the EditTexts in the fragment.
     */
    private void setInputFilters(){
        InputFilter[] inputFilters = new InputFilter[] {new DecimalDigitsInputFilter()};
        startingBalance.setFilters(inputFilters);
    }

    /**
     * Sets click listeners to the appropriate UI elements.
     */
    private void setClickListeners(){
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitAccount();
            }
        });
    }

    /**
     * Submits an Account to the database.
     */
    private void submitAccount(){
        if(!validateInput()){
            return;
        }

        Account account = new Account(
                accountName.getText().toString(),
                Double.parseDouble(startingBalance.getText().toString())
        );

        try {
            getActivity().getContentResolver().insert(CCContract.AccountEntry.Companion.getCONTENT_URI(), account.getContentValues());
            getActivity().finish();
        } catch(SQLiteException se){
            accountName.setError(getString(R.string.err_account_exists));
        }
    }

    /**
     * Validates the input of the Fragment.
     * @return True if the required input fields have values, false otherwise.
     */
    private boolean validateInput(){
        boolean isValid = true;

        if(accountName.getText().toString().isEmpty()){
            accountName.setError(getString(R.string.err_account_blank));
            isValid = false;
        }

        if(startingBalance.getText().toString().isEmpty()){
            startingBalance.setError(getString(R.string.err_starting_balance_blank));
            isValid = false;
        }

        return isValid;
    }
}
