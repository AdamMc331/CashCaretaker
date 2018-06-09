package com.androidessence.cashcaretaker

import android.app.DatePickerDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import java.util.*


/**
 * Dialog fragment that allows user to pick a date.
 */
class DatePickerFragment : androidx.fragment.app.DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dateArg = arguments?.getSerializable(ARG_DATE) as Date

        val calendar = Calendar.getInstance()
        calendar.time = dateArg

        // Use mDate
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        //TODO: Consider a better callback than the target fragment.
        return DatePickerDialog(activity, targetFragment as DatePickerDialog.OnDateSetListener, year, month, day)
    }

    companion object {
        private const val ARG_DATE = "dateArg"

        fun newInstance(date: Date): DatePickerFragment {
            val args = Bundle()
            args.putSerializable(ARG_DATE, date)

            val fragment = DatePickerFragment()
            fragment.arguments = args
            return fragment
        }
    }
}