package com.example.haduong.todoapp.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.example.haduong.todoapp.DAO.BanAnDAO;
import com.example.haduong.todoapp.DAO.GoiMonDAO;
import com.example.haduong.todoapp.HomepageActivity;
import com.example.haduong.todoapp.PaymentActivity;
import com.example.haduong.todoapp.R;
import com.example.haduong.todoapp.fragment.DisplayMenuFoodFragment;
import com.example.haduong.todoapp.model.BanAn;
import com.example.haduong.todoapp.model.GoiMon;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

// base adapter linh dong hon
public class BanAnAdapter extends BaseAdapter implements View.OnClickListener {
    private Context context;
    private int layout;
    private ArrayList<BanAn> listBanAn;
    private ViewHolderBanAn viewHolderBanAn;
    private BanAnDAO banAnDAO;
    private GoiMonDAO goiMonDAO;
    private FragmentManager fragmentManager;
    // 3 tham số: thứ nhất là context (màn hình), thứ 2 là layout để hiển thị, thứ 3 là dữ liệu
    public BanAnAdapter(Context context, int layout,ArrayList<BanAn> listBanAn) {
        this.context = context;
        this.layout = layout;
        this.listBanAn = listBanAn;
        banAnDAO = new BanAnDAO(context); // tạo đối tượng trong hàm tạo luôn
        goiMonDAO = new GoiMonDAO(context);
        fragmentManager = ((HomepageActivity)context).getSupportFragmentManager();
    }
    @Override
    public int getCount() {
        return listBanAn.size();
    }

    // trả về item thì có position rồi lấy ra
    @Override
    public Object getItem(int position) {
        return listBanAn.get(position);
    }

    @Override
    public long getItemId(int position) {
        return listBanAn.get(position).getMaBanAn();
    }



    //  sử dụng ViewHolder để lưu lại các view mà tìm rồi k phải load lại nữa, viết thêm class
    // ViewHolder trong class adapter, nên ta khai báo các view trong ViewHolder
    // giảm cho việc findViewById nhiều lần
    private class ViewHolderBanAn {
        private TextView txtTenBanAn;
        private ImageView imgViewThanhToan;
        private ImageView imgViewAnButton;
        private ImageView imgViewBanAn;
        private ImageView imgViewGoiMon;


    }
    // convertView chính là phần ở giữa
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if(view == null) { // chưa có dữ liệu gì nên convertView bị null thì khởi tạo
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            viewHolderBanAn = new ViewHolderBanAn();
            // thông qua thằng view để lấy, nếu mà null thì khởi tạo khi có rồi thì lấy ra thôi
            view = inflater.inflate(R.layout.layout_custom_display_table,parent,false);
            viewHolderBanAn.imgViewBanAn = (ImageView) view.findViewById(R.id.imgViewBanAn);
            viewHolderBanAn.imgViewGoiMon = (ImageView) view.findViewById(R.id.imgViewGoiMon);
            viewHolderBanAn.imgViewThanhToan = (ImageView) view.findViewById(R.id.imgViewThanhToan);
            viewHolderBanAn.imgViewAnButton = (ImageView) view.findViewById(R.id.imgViewAnButton);
            viewHolderBanAn.txtTenBanAn = (TextView) view.findViewById(R.id.txtTenBanAn);
            // Nếu mà file activity nó liên quan đến layout luôn thì chỉ cần gọi findviewByid
            // còn apdapter thì phải thông qua một thằng view để đến layout đó mới findviewbyid được

            view.setTag(viewHolderBanAn); // lưu trữ lại viewHolder vào trong view
        } else {
            // nguowcj lai phần convertView có giá trị rồi thì lấy id đã lưu trữ ra thôi
            // lấy từ trong view trả ra gán lại cho viewHolder
            viewHolderBanAn = (ViewHolderBanAn) view.getTag();
        }

        // check xem đã được chọn hay chưa
        if(listBanAn.get(position).isTinhTrang()) {
            visibleButton();
        } else {
            invisibleButton();
        }


        //position laf vị trí mà gridview truyền vào
        BanAn banAn = listBanAn.get(position);
        String tinhTrang = banAnDAO.getStatusTable(banAn.getMaBanAn());
        if(tinhTrang.equals("true")) {
            viewHolderBanAn.imgViewBanAn.setImageResource(R.drawable.banantrue);
        } else {
            viewHolderBanAn.imgViewBanAn.setImageResource(R.drawable.banan);
        }
//        Log.d("Position:" ,position + "");
        viewHolderBanAn.txtTenBanAn.setText(banAn.getTenBanAn());
        // lưu vị trí item
        viewHolderBanAn.imgViewBanAn.setTag(position);
        // mỗi thằng getView tương ứng với thằng item trong gridview
        // ta bắt sự kiện click cho item này luôn tại chỗ getView ở adapter

        viewHolderBanAn.imgViewBanAn.setOnClickListener(this);
        viewHolderBanAn.imgViewGoiMon.setOnClickListener(this);
        viewHolderBanAn.imgViewThanhToan.setOnClickListener(this);
        viewHolderBanAn.imgViewAnButton.setOnClickListener(this);

        return view;
    }

    private void visibleButton() {
        viewHolderBanAn.imgViewGoiMon.setVisibility(View.VISIBLE);
        viewHolderBanAn.imgViewThanhToan.setVisibility(View.VISIBLE);
        viewHolderBanAn.imgViewAnButton.setVisibility(View.VISIBLE);
    }

    private void invisibleButton() {
        viewHolderBanAn.imgViewGoiMon.setVisibility(View.INVISIBLE);
        viewHolderBanAn.imgViewThanhToan.setVisibility(View.INVISIBLE);
        viewHolderBanAn.imgViewAnButton.setVisibility(View.INVISIBLE);
    }

    // chuwa dduowc click
    @Override
    public void onClick(View v) {
        int id = v.getId();
        // tra ve viewHolder
        viewHolderBanAn = (ViewHolderBanAn) ((View)v.getParent()).getTag();
        int position1 = (int) viewHolderBanAn.imgViewBanAn.getTag(); // vị trí trong viewholder
        // lấy mã của bàn được chọn khi người dùng click vào btn gọi món
        int maBan = listBanAn.get(position1).getMaBanAn();
        // lấy vị trí thằng được click, nó chính là vị trí trong list.
        // tóm lại đây là 1 vị trí trong list bàn ăn

        switch (id) {
            case R.id.imgViewBanAn:
                int position = (int) v.getTag();
                // tình trạng bàn khác với tình trạng gọi món
                listBanAn.get(position).setTinhTrang(true);
                // visible là hiện lên
                visibleButton();
                break;
            case R.id.imgViewGoiMon:

                // adapter để chuyển dữ liệu lên fragment. Cũng có thể trỏ lên activity chứa fragment ngay tại đây
                Intent intent = ((HomepageActivity)context).getIntent();
                int maNhanVien = intent.getIntExtra("maNhanVien",0);
                String status = banAnDAO.getStatusTable(maBan);
//                Log.d("statustable",status);
                // khi bấm vào gọi món thì kiểm tra bàn đó trống không. Nếu trống thì
                // thêm dữ liệu vào bảng gọi món và cập nhật tình trạng bàn
                if(status.equals("false")) { // order
                    // nếu bàn k có người thì add gọi món
                    Calendar calendar = Calendar.getInstance();
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
                    GoiMon goiMon = new GoiMon();
                    goiMon.setMaBan(maBan);
                    goiMon.setMaNhanVien(maNhanVien);
                    goiMon.setStatusPayment("false");// chưa thay toán
                    goiMon.setDayOrder(simpleDateFormat.format(calendar.getTime()));
                    boolean check = goiMonDAO.addGoiMon(goiMon);
                    // xong cập nhật lại trnagj thái bàn là đã có người
                    banAnDAO.updateStatusTable(maBan,"true");
                    if(check) {
                        Toast.makeText(context, context.getResources().getString(R.string.order_successful),
                                Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(context, context.getResources().getString(R.string.order_failures),
                                Toast.LENGTH_SHORT).show();
                    }
                }
                // order xong chuyển sang fragment loại thực đơn
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                DisplayMenuFoodFragment displayMenuFoodFragment = new DisplayMenuFoodFragment();
                Bundle bundle = new Bundle();
                bundle.putInt("maBan",maBan);
                displayMenuFoodFragment.setArguments(bundle);
                fragmentTransaction.replace(R.id.content,displayMenuFoodFragment).addToBackStack("displayTable");
                fragmentTransaction.commit();

                break;

            case R.id.imgViewThanhToan:
                // khi click vào nút thanh toán thì lấy mã bàn đó đem đi thanh toán
                Intent intentPayment = new Intent(context,PaymentActivity.class);
                intentPayment.putExtra("maBan",maBan);
                context.startActivity(intentPayment);
                break;
        }
    }
}
