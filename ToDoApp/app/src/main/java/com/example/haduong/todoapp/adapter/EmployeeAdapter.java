package com.example.haduong.todoapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.haduong.todoapp.R;
import com.example.haduong.todoapp.model.NhanVien;

import java.util.ArrayList;

public class EmployeeAdapter extends BaseAdapter {
    private Context context;
    private int layout;
    private ArrayList<NhanVien> listEmployee;
    private ViewHolderEmployee viewHolderEmployee;
    public EmployeeAdapter(Context context, int layout, ArrayList<NhanVien> listEmployee) {
        this.context = context;
        this.layout = layout;
        this.listEmployee = listEmployee;
    }

    @Override
    public int getCount() {
        return listEmployee.size();
    }

    @Override
    public Object getItem(int position) {
        return listEmployee.get(position);
    }

    @Override
    public long getItemId(int position) {
        return listEmployee.get(position).getManv();
    }
    private class ViewHolderEmployee {
        private ImageView imgViewAvata;
        private TextView txtTenNhanVien,txtCMNDNhanvien;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if(view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.layout_custom_display_employee,parent,false);
            viewHolderEmployee = new ViewHolderEmployee();
            viewHolderEmployee.imgViewAvata = (ImageView) view.findViewById(R.id.imgViewAvata);
            viewHolderEmployee.txtTenNhanVien = (TextView) view.findViewById(R.id.txtTenNhanVien);
            viewHolderEmployee.txtCMNDNhanvien = (TextView) view.findViewById(R.id.txtCMNDNhanVien);

            view.setTag(viewHolderEmployee);
        } else {
            viewHolderEmployee = (ViewHolderEmployee) view.getTag();
        }

        NhanVien nhanVien = listEmployee.get(position);
        viewHolderEmployee.txtTenNhanVien.setText(nhanVien.getTenDN());
        viewHolderEmployee.txtCMNDNhanvien.setText(String.valueOf(nhanVien.getCMND()));

        return view;
    }
}
