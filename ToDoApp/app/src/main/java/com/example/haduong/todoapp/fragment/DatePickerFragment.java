package com.example.haduong.todoapp.fragment;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;

import com.example.haduong.todoapp.R;

import java.util.Calendar;

// tạo ra một fragment kế thừa từ dialogframent
public class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        // sử dụng Calendar
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        // trả ra một datepickerdialog, this trỏ đến thằng ondateSetlistener
        //getActivity chính là thằng chứa datepickerfragment
        return new DatePickerDialog(getActivity(),this,year,month,day);
    }
// khi chọn ngày xong thì trả về vào hàm ondateset
    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        // sẽ lấy được activity chứa fragment đó
        EditText edtBirthday = getActivity().findViewById(R.id.edtBirthday);
        String birthday = dayOfMonth + "/" + (month +1 ) + "/" + year;
        edtBirthday.setText(birthday); // chọn xong sẽ vào ondate set và setext để nó hiện lên
    }
}
