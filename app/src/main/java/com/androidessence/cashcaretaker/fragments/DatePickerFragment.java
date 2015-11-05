package com.androidessence.cashcaretaker.fragments;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;

import org.joda.time.LocalDate;

/**
 * Created by adammcneilly on 10/15/15.
 */
public class DatePickerFragment extends DialogFragment{
    private LocalDate mDate;
    private static final String ARG_DATE = "dateArg";

    public static DatePickerFragment NewInstance(LocalDate date){
        DatePickerFragment fragment = new DatePickerFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_DATE, date);
        fragment.setArguments(args);
        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        mDate = (LocalDate) getArguments().getSerializable(ARG_DATE);

        // Use mDate
        assert mDate != null;
        int year = mDate.getYear();
        int month = mDate.getMonthOfYear() - 1;
        int day = mDate.getDayOfMonth();

        return new DatePickerDialog(getActivity(), (DatePickerDialog.OnDateSetListener)getTargetFragment(), year, month, day);
    }
}
