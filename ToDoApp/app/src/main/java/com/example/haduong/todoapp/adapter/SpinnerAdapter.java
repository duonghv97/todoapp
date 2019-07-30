package com.example.haduong.todoapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.haduong.todoapp.R;
import com.example.haduong.todoapp.model.LoaiMonAn;

import java.util.ArrayList;

// đã là custom thì phải có apdapter. Do spinner mình còn lưu cả mã nữa nên cần phải custom
// chứ k chỉ mình string
public class SpinnerAdapter extends BaseAdapter {
    private Context context;
    private int layout;
    private ArrayList<LoaiMonAn> listTypeMenuFood;
    private ViewHolderTypeMenuFood viewHolderTypeMenuFood;
    // bao giờ adapter chả có 3 tham số, 1 là context,2 là layout, 3 là dữ liệu
    public SpinnerAdapter(Context context, int layout, ArrayList<LoaiMonAn> listTypeMenuFood) {
        this.context = context;
        this.layout = layout;
        this.listTypeMenuFood = listTypeMenuFood;
    }
    @Override
    public int getCount() {
        return listTypeMenuFood.size();
    }

    @Override
    public Object getItem(int position) {
        return listTypeMenuFood.get(position);
    }

    @Override
    public long getItemId(int position) {
        return listTypeMenuFood.get(position).getMaLoai();
    }

    private class ViewHolderTypeMenuFood {
        private TextView txtNameTypeMenuFood;
    }

    // thêm một thằng getDropDown view tức là khi bấm thì nó xổ xuống

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if(view == null) {
            viewHolderTypeMenuFood = new ViewHolderTypeMenuFood();
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.layout_custom_spinner,parent,false);

            viewHolderTypeMenuFood.txtNameTypeMenuFood = (TextView) view.findViewById(R.id.txtTenLoai);
            view.setTag(viewHolderTypeMenuFood);
        } else {
            viewHolderTypeMenuFood = (ViewHolderTypeMenuFood) view.getTag();
        }

        LoaiMonAn loaiMonAn = listTypeMenuFood.get(position);
        viewHolderTypeMenuFood.txtNameTypeMenuFood.setText(loaiMonAn.getTenLoai());
        // lưu mã vào tên luôn trong tag. Do một spinner thì có hiển thị text nhưng mình
        // chọn thì nó có cả mã nữa
        viewHolderTypeMenuFood.txtNameTypeMenuFood.setTag(loaiMonAn.getMaLoai());
        return view;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if(view == null) {
            viewHolderTypeMenuFood = new ViewHolderTypeMenuFood();
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.layout_custom_spinner,parent,false);

            viewHolderTypeMenuFood.txtNameTypeMenuFood = (TextView) view.findViewById(R.id.txtTenLoai);
            view.setTag(viewHolderTypeMenuFood);
        } else {
            viewHolderTypeMenuFood = (ViewHolderTypeMenuFood) view.getTag();
        }

        LoaiMonAn loaiMonAn = listTypeMenuFood.get(position);
        viewHolderTypeMenuFood.txtNameTypeMenuFood.setText(loaiMonAn.getTenLoai());
        // lưu mã vào tên luôn trong tag. Do một spinner thì có hiển thị text nhưng mình
        // chọn thì nó có cả mã nữa
        viewHolderTypeMenuFood.txtNameTypeMenuFood.setTag(loaiMonAn.getMaLoai());

        return view;
    }
}
