package com.example.haduong.todoapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.LongDef;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.haduong.todoapp.DAO.BanAnDAO;

public class EditTableDinnerActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText edtSuaBanAn;
    private Button btnSuaBanAn;
    private BanAnDAO banAnDAO;
    private int maBan = 0;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_edit_tabledinner);
        initViews();
        banAnDAO = new BanAnDAO(this);
        Intent intent = getIntent();
        maBan = intent.getIntExtra("maBan",0);
        String nameTable = banAnDAO.getNameTable(maBan);
        edtSuaBanAn.setText(nameTable);
        btnSuaBanAn.setOnClickListener(this);
    }
    private void initViews() {
        edtSuaBanAn = (EditText) findViewById(R.id.edtSuaBanAn);
        btnSuaBanAn = (Button) findViewById(R.id.btnSuaBanAn);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.btnSuaBanAn:
                String tenBan = edtSuaBanAn.getText().toString();
                if (tenBan.trim().length() > 0) {
                    boolean check = banAnDAO.updateTableDinner(maBan,tenBan);
                    Intent intent = new Intent();
                    intent.putExtra("checkEditBanAn",check);
                    setResult(Activity.RESULT_OK,intent);
                    finish();
                } else {
                    Toast.makeText(this, getResources().getString(R.string.not_null), Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }
}
