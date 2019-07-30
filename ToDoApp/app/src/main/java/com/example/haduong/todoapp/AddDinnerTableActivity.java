package com.example.haduong.todoapp;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.haduong.todoapp.DAO.BanAnDAO;

public class AddDinnerTableActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText edtThemBanAn;
    private Button btnThemBanAn;
    // trong file activity sẽ gọi DAO vào và gọi hàm thôi, còn phần thực thi ở trong DAO rồi
    private BanAnDAO banAnDAO;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_add_dinnertable);
        initView();
        banAnDAO = new BanAnDAO(this);
        btnThemBanAn.setOnClickListener(this);

    }
    private void initView() {
        edtThemBanAn = (EditText) findViewById(R.id.edtThemBanAn);
        btnThemBanAn = (Button) findViewById(R.id.btnThemBanAn);
    }

    @Override
    public void onClick(View v) {
        String tenBanAn = edtThemBanAn.getText().toString();
        if(tenBanAn.equals("")) {
            Toast.makeText(this, "Nhập đủ tên bàn ăn!", Toast.LENGTH_SHORT).show();
        } else {
            boolean check = banAnDAO.addBanAn(tenBanAn);
            // tra ve trang ban dau kết quả thêm có thành công hay không
            Intent intent = new Intent(); // trả về k phải đổi màn hình nên k cần tham số trong intent
            intent.putExtra("resultThemBanAn",check);
            setResult(Activity.RESULT_OK,intent);
            finish(); // để tắt popup đi
        }
    }
}
