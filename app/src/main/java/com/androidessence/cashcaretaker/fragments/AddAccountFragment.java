package com.androidessence.cashcaretaker.fragments;

import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.InputFilter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.androidessence.cashcaretaker.data.CCContract;
import com.androidessence.cashcaretaker.DecimalDigitsInputFilter;
import com.androidessence.cashcaretaker.R;
import com.androidessence.cashcaretaker.dataTransferObjects.Account;

/**
 * Fragment container for the UI of adding an account to the database.
 *
 * Created by adammcneilly on 11/1/15.
 */
public class AddAccountFragment extends Fragment{
    // UI Elements
    private EditText mAccountName;
    private EditText mStartingBalance;
    private Button mSubmit;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_account, container, false);

        getUIElements(view);
        setInputFilters();
        setClickListeners();

        return view;
    }

    /**
     * Retrieves necessary UI elements from the fragment.
     */
    private void getUIElements(View view){
        mAccountName = (EditText) view.findViewById(R.id.account_name);
        mStartingBalance = (EditText) view.findViewById(R.id.starting_balance);
        mSubmit = (Button) view.findViewById(R.id.submit);
    }

    /**
     * Sets any InputFilters to the EditTexts in the fragment.
     */
    private void setInputFilters(){
        InputFilter[] inputFilters = new InputFilter[] {new DecimalDigitsInputFilter()};
        mStartingBalance.setFilters(inputFilters);
    }

    /**
     * Sets click listeners to the appropriate UI elements.
     */
    private void setClickListeners(){
        mSubmit.setOnClickListener(new View.OnClickListener() {
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
                mAccountName.getText().toString(),
                Double.parseDouble(mStartingBalance.getText().toString())
        );

        try {
            getActivity().getContentResolver().insert(CCContract.AccountEntry.CONTENT_URI, account.getContentValues());
            getActivity().finish();
        } catch(SQLiteException se){
            mAccountName.setError("Account name already exists.");
        }
    }

    /**
     * Validates the input of the Fragment.
     * @return True if the required input fields have values, false otherwise.
     */
    private boolean validateInput(){
        boolean isValid = true;

        if(mAccountName.getText().toString().isEmpty()){
            mAccountName.setError("Account name cannot be blank.");
            isValid = false;
        }

        if(mStartingBalance.getText().toString().isEmpty()){
            mStartingBalance.setError("Starting balance cannot be blank.");
            isValid = false;
        }

        return isValid;
    }
}
