package com.example.haduong.todoapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.haduong.todoapp.DAO.LoaiMonAnDAO;

public class AddTypeMenuFoodActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText edtAddTypeMenuFood;
    private Button btnAddTypeMenuFood;
    private LoaiMonAnDAO loaiMonAnDAO;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // layout_add_type_menufood hienr thi dưới dạng popup khi
        // cấu hình trong androidmanifest
        setContentView(R.layout.layout_add_type_menufood);
        loaiMonAnDAO = new LoaiMonAnDAO(this);
        initViews();
        btnAddTypeMenuFood.setOnClickListener(this);
    }
    private void initViews() {
        edtAddTypeMenuFood = (EditText) findViewById(R.id.edtThemLoaiThucDon);
        btnAddTypeMenuFood = (Button) findViewById(R.id.btnThemLoaiThucDon);
    }


    @Override
    public void onClick(View v) {
        String nameTypeMenuFood = edtAddTypeMenuFood.getText().toString();
        try{
            boolean check = loaiMonAnDAO.addTypeMenuFood(nameTypeMenuFood);
            // check rồi gửi kết quả về menufood
            Intent intent = new Intent();
            intent.putExtra("nameTypeMenuFood",check);
            setResult(RESULT_OK,intent);
            finish();// chuyển màn hình trả về dữ liệu thì finnish luôn
        }catch (NumberFormatException e) {
            Toast.makeText(this, "Not a number!", Toast.LENGTH_SHORT).show();
        }
    }
}
