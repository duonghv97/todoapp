package com.example.haduong.todoapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.haduong.todoapp.DAO.BanAnDAO;
import com.example.haduong.todoapp.DAO.GoiMonDAO;
import com.example.haduong.todoapp.adapter.PaymentAdapter;
import com.example.haduong.todoapp.model.Payment;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class PaymentActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView txtTenMonAn;
    private TextView txtSoLuong;
    private TextView txtGiaBan;
    private TextView txtTotalPrice;
    private GridView gvPayment;
    private Button btnPayment,btnPaymentExit;
    private PaymentAdapter paymentAdapter;
    private ArrayList<Payment> listMenuFoodPayment;
    private GoiMonDAO goiMonDAO;
    private int maBan = 0;
    private BanAnDAO banAnDAO;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_payment);
        initViews();
        // lấy từ bên ban ăn chuyển snag cái mã gọi món
        Intent intent = getIntent();
        maBan = intent.getIntExtra("maBan",0);
        goiMonDAO = new GoiMonDAO(this);
        banAnDAO = new BanAnDAO(this);
        // từ mà bàn thì lấy mã gọi món. từ mã gọi món xong lấy ra list
        if(maBan != 0) {
            // false là chưa thanh toán
            displayPayment();

            int number = 0, giaTien = 0;
            long tongTien = 0;
            for(int i = 0 ; i < listMenuFoodPayment.size() ;i++) {
                number = listMenuFoodPayment.get(i).getSoLuong();
                giaTien = listMenuFoodPayment.get(i).getGiaTien();
                tongTien += number*giaTien;
            }
            txtTotalPrice.setText(getResources().getString(R.string.tongcong) + tongTien);
        }
        btnPayment.setOnClickListener(this);
        btnPaymentExit.setOnClickListener(this);
    }

    private void displayPayment() {
        int maGoiMon = goiMonDAO.getMaGoiMonByMaBan(maBan, "false");
        listMenuFoodPayment = goiMonDAO.getListFoodByMaGoiMon(maGoiMon);
        paymentAdapter = new PaymentAdapter(this, R.layout.layout_custom_menufood_payment, listMenuFoodPayment);
        gvPayment.setAdapter(paymentAdapter);
        paymentAdapter.notifyDataSetChanged();
    }
    private void initViews() {
        txtTenMonAn = (TextView) findViewById(R.id.txtTenMonAn);
        txtSoLuong = (TextView) findViewById(R.id.txtSoLuong);
        txtGiaBan = (TextView) findViewById(R.id.txtGiaBan);
        txtTotalPrice = (TextView) findViewById(R.id.txtTotalPrice);
        gvPayment = (GridView) findViewById(R.id.gvPayment);
        btnPayment = (Button) findViewById(R.id.btnPayment);
        btnPaymentExit = (Button) findViewById(R.id.btnPaymentExit);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.btnPayment:
                // cập nhật lại tình trạng bàn là k có người ngồi
                boolean checkTable = banAnDAO.updateStatusTable(maBan,"false");
                // cập nhật trạng thái của gọi món là đã thanh toán
                boolean checkPayment = goiMonDAO.updateStatusGoiMonByMaBan(maBan,"true");
                if(checkPayment && checkTable) {
                    displayPayment();
                    Toast.makeText(this, getResources().getString(R.string.payment_successful), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, getResources().getString(R.string.payment_error), Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.btnPaymentExit:
                finish();
                break;
        }
    }
}
