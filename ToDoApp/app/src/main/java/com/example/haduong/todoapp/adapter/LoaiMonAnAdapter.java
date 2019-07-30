package com.example.haduong.todoapp.adapter;


import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.haduong.todoapp.DAO.LoaiMonAnDAO;
import com.example.haduong.todoapp.R;
import com.example.haduong.todoapp.model.LoaiMonAn;

import java.util.ArrayList;

// chuyển tất cả các loại món ăn vào grid view
public class LoaiMonAnAdapter extends BaseAdapter {
    private Context context;
    private int layout;
    private ArrayList<LoaiMonAn> listTypeMenuFood;
    private ViewHolderDisplayMenuFood viewHolderDisplayMenuFood;
    private LoaiMonAnDAO loaiMonAnDAO;

    public LoaiMonAnAdapter(Context context,int layout, ArrayList<LoaiMonAn> listTypeMenuFood) {
        this.context = context;
        this.layout = layout;
        this.listTypeMenuFood = listTypeMenuFood;
        loaiMonAnDAO = new LoaiMonAnDAO(context);
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

    private class ViewHolderDisplayMenuFood {
        private ImageView imgViewTenLoaiThucDon;
        private TextView txtViewTenLoaiThucDon;

    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if(view == null) {
            viewHolderDisplayMenuFood = new ViewHolderDisplayMenuFood();
            LayoutInflater inflater = (LayoutInflater) context.getSystemService
                    (Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.layout_custom_display_type_food,null);
            viewHolderDisplayMenuFood.imgViewTenLoaiThucDon = (ImageView)
                    view.findViewById(R.id.imgViewHienThiMonAn);
            viewHolderDisplayMenuFood.txtViewTenLoaiThucDon = (TextView)
                    view.findViewById(R.id.txtTenLoaiMonAn);

            view.setTag(viewHolderDisplayMenuFood);


        } else {
            viewHolderDisplayMenuFood = (ViewHolderDisplayMenuFood) view.getTag();
        }

        // set dữ liệu
        LoaiMonAn loaiMonAn = listTypeMenuFood.get(position);

        String hinhAnh = loaiMonAnDAO.getImageTypeMenuFood(loaiMonAn.getMaLoai());
        Uri uri = Uri.parse(hinhAnh);
        viewHolderDisplayMenuFood.txtViewTenLoaiThucDon.setText(loaiMonAn.getTenLoai());
        viewHolderDisplayMenuFood.imgViewTenLoaiThucDon.setImageURI(uri);

        return view;
    }
}
