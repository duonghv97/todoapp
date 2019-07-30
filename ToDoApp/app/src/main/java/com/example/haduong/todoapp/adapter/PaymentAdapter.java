package com.example.haduong.todoapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.haduong.todoapp.R;
import com.example.haduong.todoapp.model.Payment;

import java.util.ArrayList;

public class PaymentAdapter extends BaseAdapter {
    private Context context;
    private int layout;
    private ArrayList<Payment> listMenuFoodPayment;
    private ViewHolderPayment viewHolderPayment;

    public PaymentAdapter(Context context, int layout, ArrayList<Payment> listMenuFoodPayment) {
        this.context = context;
        this.layout = layout;
        this.listMenuFoodPayment = listMenuFoodPayment;
    }
    @Override
    public int getCount() {
        return listMenuFoodPayment.size();
    }

    @Override
    public Object getItem(int position) {
        return listMenuFoodPayment.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    private class ViewHolderPayment {
        private TextView txtTenMonAnPayment;
        private TextView txtSoLuongPayment;
        private TextView txtGiaTienPayment;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if(view == null) {
            viewHolderPayment = new ViewHolderPayment();
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.layout_custom_menufood_payment,parent,false);

            viewHolderPayment.txtTenMonAnPayment = view.findViewById(R.id.txtTenMonAnPayment);
            viewHolderPayment.txtSoLuongPayment = view.findViewById(R.id.txtSoLuongPayment);
            viewHolderPayment.txtGiaTienPayment = view.findViewById(R.id.txtGiaBanPayment);

            view.setTag(viewHolderPayment);
        } else {
            viewHolderPayment = (ViewHolderPayment) view.getTag();
        }
        // lấy ra giá trị tại position

        Payment payment = listMenuFoodPayment.get(position);
        viewHolderPayment.txtTenMonAnPayment.setText(payment.getTenMonAn());
        // String.valueOf chuyển số int sang String
        viewHolderPayment.txtSoLuongPayment.setText(String.valueOf(payment.getSoLuong()));
        viewHolderPayment.txtGiaTienPayment.setText(String.valueOf(payment.getGiaTien()));


        return view;
    }
}
