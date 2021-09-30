package com.example.workMemo;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;

import java.util.Calendar;

public class DatePickerDialogFragment extends DialogFragment{

    @Override
    public Dialog onCreateDialog(Bundle savedInstantState){

        //デフォルトのタイムゾーンおよびロケールを使用してカレンダを取得
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), (DatePickerDialog.OnDateSetListener)getActivity(), year, month, day); //this はonDateSetListener
        return datePickerDialog;
    }

}