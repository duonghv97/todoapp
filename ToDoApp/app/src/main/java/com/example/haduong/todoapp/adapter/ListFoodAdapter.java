package com.example.haduong.todoapp.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.haduong.todoapp.R;
import com.example.haduong.todoapp.model.MonAn;

import java.util.ArrayList;

// sử dụng chung layout với danh sách loại món ăn. Do có mỗi grid view còn
// lại dữ liệu trong từng item là do mình custom
public class ListFoodAdapter extends BaseAdapter {
    private ArrayList<MonAn> listFood;
    private Context context;
    private int layout; // adapter  trước tiên cần đủ 3 trường đã
    private ViewHolderListFood viewHolderListFood;

    public ListFoodAdapter(Context context,int layout,ArrayList<MonAn> listFood) {
        this.context = context;
        this.layout = layout;
        this.listFood = listFood;
    }

    @Override
    public int getCount() {
        return listFood.size();
    }

    @Override
    public Object getItem(int position) {
        return listFood.get(position);
    }

    @Override
    public long getItemId(int position) {
        return listFood.get(position).getMaMonAn();
    }

    private class ViewHolderListFood {
        private ImageView imgViewFood;
        private TextView txtNameFood, txtPriceFood;

    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if(view == null) {
            viewHolderListFood = new ViewHolderListFood();
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.layout_custom_list_food,parent,false);
            // ánh xạ
            viewHolderListFood.imgViewFood = (ImageView) view.findViewById(R.id.imgViewFood);
            viewHolderListFood.txtNameFood = (TextView) view.findViewById(R.id.txtNameFood);
            viewHolderListFood.txtPriceFood = (TextView) view.findViewById(R.id.txtPriceFood);

            view.setTag(viewHolderListFood);
        }else {
            viewHolderListFood = (ViewHolderListFood) view.getTag();
        }
        //
        MonAn monAn = listFood.get(position);
        String url = monAn.getHinhAnh().toString();
        if(url.equals("") && url == null) {
            // suwr dung hinh mac dinh, set image resource
            viewHolderListFood.imgViewFood.setImageResource(R.drawable.background_header);
        } else {
            Uri uri = Uri.parse(url);
            // hình ảnh thì sử dụng setImageURI
            viewHolderListFood.imgViewFood.setImageURI(uri);
        }
        viewHolderListFood.txtNameFood.setText(monAn.getTenMonAn());
        viewHolderListFood.txtPriceFood.setText
                (context.getResources().getString(R.string.priceInfo) + monAn.getGiaTien());
        return view;
    }
}
