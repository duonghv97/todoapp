package com.example.haduong.todoapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.haduong.todoapp.DAO.GoiMonDAO;
import com.example.haduong.todoapp.model.ChiTietGoiMon;

public class QuantityActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText edtAddQuantity;
    private Button btnAddQuantity;
    private GoiMonDAO goiMonDAO;
    private int maBan = 0;
    private int maMonAn = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_add_quantity);
        initViews();
        goiMonDAO = new GoiMonDAO(this);
        Intent intent = getIntent();
        // lấy được mã bàn sang
        maBan = intent.getIntExtra("maBan",0);
        // lấy được mã món ăn
        maMonAn = intent.getIntExtra("maMonAn",0);
//        Log.d("maBan",maBan+"");
//        Log.d("maMonAn",maMonAn+"");
        btnAddQuantity.setOnClickListener(this);
    }

    private void initViews() {
        edtAddQuantity = (EditText) findViewById(R.id.edtAddQuantity);
        btnAddQuantity = (Button) findViewById(R.id.btnAddQuantity);
    }

    @Override
    public void onClick(View v) {
        // false ở đây là chưa thanh toán
        int maGoiMon = goiMonDAO.getMaGoiMonByMaBan(maBan,"false");
        Log.d("maGoiMon",maGoiMon+"");
        boolean check = goiMonDAO.checkFoodExist(maGoiMon,maMonAn);
        if(check) {
            // update
            int quantityOld = (int) goiMonDAO.getQuantityFoodByMaGoiMon(maGoiMon,maMonAn);
            if(edtAddQuantity.getText().toString().trim().length() > 0) {
                int quantityNew = Integer.parseInt(edtAddQuantity.getText().toString());
                int totalQuantity = quantityNew + quantityOld;
                // capaj nhaajt lại số lượng
                ChiTietGoiMon chiTietGoiMon = new ChiTietGoiMon();
                chiTietGoiMon.setMaGoiMon(maGoiMon);
                chiTietGoiMon.setMaMonAn(maMonAn);
                chiTietGoiMon.setSoLuong(totalQuantity);
                boolean checkUpdate = goiMonDAO.updateQuantity(chiTietGoiMon);
                if(checkUpdate) {
                    Toast.makeText(this, getResources().getString(R.string.add_good), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, getResources().getString(R.string.add_false), Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, getResources().getString(R.string.not_null), Toast.LENGTH_SHORT).show();
            }

        } else {
            // thêm mới
            int quantity = Integer.parseInt(edtAddQuantity.getText().toString());
            // capaj nhaajt lại số lượng
            ChiTietGoiMon chiTietGoiMon = new ChiTietGoiMon();
            chiTietGoiMon.setMaGoiMon(maGoiMon);
            chiTietGoiMon.setMaMonAn(maMonAn);
            chiTietGoiMon.setSoLuong(quantity);
            // thêm chi tiết món ăn vào
            boolean checkUpdate = goiMonDAO.addChiTietGoiMon(chiTietGoiMon);
            if(checkUpdate) {
                Toast.makeText(this, getResources().getString(R.string.add_good), Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, getResources().getString(R.string.add_false), Toast.LENGTH_SHORT).show();
            }
        }

    }
}
