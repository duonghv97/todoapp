package com.example.haduong.todoapp;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.haduong.todoapp.DAO.LoaiMonAnDAO;
import com.example.haduong.todoapp.DAO.MonAnDAO;
import com.example.haduong.todoapp.adapter.LoaiMonAnAdapter;
import com.example.haduong.todoapp.adapter.SpinnerAdapter;
import com.example.haduong.todoapp.model.LoaiMonAn;
import com.example.haduong.todoapp.model.MonAn;

import java.io.IOException;
import java.util.ArrayList;

public class AddMenuFoodActivity extends AppCompatActivity implements View.OnClickListener {
    private ImageButton ibtnThemLoaiThucDon;
    private Spinner spinLoaiThucDon;
    private int REQUEST_CODE_ADDMENUFOOD = 111;
    private int REQUEST_CODE_OPENIMAGE = 112;
    private LoaiMonAnDAO loaiMonAnDAO;
    private MonAnDAO monAnDAO;
    private ArrayList<LoaiMonAn> listTypeMenuFood;
    private SpinnerAdapter spinnerAdapter;
    private ImageView imgViewHinhThucDon;
    private Button btnYesAddMenuFood;
    private Button btnExitAddMenuFood;
    private String url;
    private EditText edtNameFood,edtPrice;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_add_menu_food);
        loaiMonAnDAO = new LoaiMonAnDAO(this);
        monAnDAO = new MonAnDAO(this);
        initViews();
        displaySpinnerTypeMenuFood();
        ibtnThemLoaiThucDon.setOnClickListener(this);
        imgViewHinhThucDon.setOnClickListener(this);

        btnYesAddMenuFood.setOnClickListener(this);
        btnExitAddMenuFood.setOnClickListener(this);

    }

    private void displaySpinnerTypeMenuFood() {
        listTypeMenuFood = loaiMonAnDAO.getAllTypeMenuFood();
        spinnerAdapter = new SpinnerAdapter(this,R.layout.layout_custom_spinner,listTypeMenuFood);
        spinLoaiThucDon.setAdapter(spinnerAdapter);
        spinnerAdapter.notifyDataSetChanged();

    }
    private void initViews() {
        ibtnThemLoaiThucDon = (ImageButton) findViewById(R.id.ibtnThemLoaiThucDon);
        spinLoaiThucDon = (Spinner) findViewById(R.id.spinLoaiThucDon);
        imgViewHinhThucDon = (ImageView) findViewById(R.id.imgViewHinhThucDon);
        btnYesAddMenuFood = (Button) findViewById(R.id.btnYesAddMenuFood);
        btnExitAddMenuFood = (Button) findViewById(R.id.btnExitAddMenuFood);
        edtNameFood = (EditText) findViewById(R.id.edtNameFood);
        edtPrice = (EditText) findViewById(R.id.edtPrice);
    }
    @Override
    public void onClick(View v) {
            int id = v.getId();
            switch (id) {
                case R.id.ibtnThemLoaiThucDon:
                    Intent intent = new Intent(AddMenuFoodActivity.this,AddTypeMenuFoodActivity.class);
                    startActivityForResult(intent,REQUEST_CODE_ADDMENUFOOD);
                    break;
                case R.id.imgViewHinhThucDon:
                    // intent vào của hệ thống
                    Intent intentOpenImage = new Intent();
                    // set định dạng là image là lấy tất cả hình ảnh
                    intentOpenImage.setType("image/*");
                    // lấy content là dạng image thôi
                    intentOpenImage.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(Intent.createChooser(intentOpenImage,"CHỌN HÌNH THỰC ĐƠN"),
                            REQUEST_CODE_OPENIMAGE);
                    break;

                case R.id.btnYesAddMenuFood:
                    // lấy ra vị trí của của item trong spiner
                    int position = spinLoaiThucDon.getSelectedItemPosition();
                    int maLoai = listTypeMenuFood.get(position).getMaLoai();
                    String nameFood = edtNameFood.getText().toString();
                    String price = edtPrice.getText().toString();
                    if(nameFood.trim().length() > 0 && price.trim().length() > 0) {
                        MonAn monAn = new MonAn();
                        monAn.setTenMonAn(nameFood);
                        monAn.setGiaTien(price);
                        monAn.setMaLoai(maLoai);
                        // đặt url là biesn toàn cục
                        monAn.setHinhAnh(url);
                        boolean check = monAnDAO.themMonAn(monAn);
                        if(check) {
                            Intent intentAddMenu = new Intent();
                            intentAddMenu.putExtra("checkAddMenu",check);
                            setResult(Activity.RESULT_OK,intentAddMenu);
                            Toast.makeText(this, getResources().getString(R.string.add_good), Toast.LENGTH_SHORT).show();
                            finish();
                        } else {
                            Toast.makeText(this, getResources().getString(R.string.add_false), Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(this, getResources().getString(R.string.loithemmonan), Toast.LENGTH_SHORT).show();
                    }
                    break;

                case R.id.btnExitAddMenuFood:
                    finish();
                    break;
            }
    }

    // khi nhận kết quả trả về, ở typemenu đã thêm vào csdl xong trả về kết quả thôi.
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_CODE_ADDMENUFOOD) {
            if(resultCode == Activity.RESULT_OK) {
                Intent duLieu = data;
                boolean check =  duLieu.getBooleanExtra("nameTypeMenuFood",false);
                if(check) {
                    // thêm vào thành công thì hiển thị lên thôi
                    displaySpinnerTypeMenuFood();
                    Toast.makeText(this, getResources().getString(R.string.add_good), Toast.LENGTH_SHORT).show();
                } else {
                    finish();
                    Toast.makeText(this, getResources().getString(R.string.add_false), Toast.LENGTH_SHORT).show();
                }
            }

        } else if(requestCode == REQUEST_CODE_OPENIMAGE) {
            if(resultCode == Activity.RESULT_OK && data != null) {
                // trả lại đường dẫn. data là đường dẫn image
                // biến đường dẫn thành tấm hình sử dụng BitMap
                // sử dụng image button sử dụng bitmap, parse đường dẫn sang bitmap
                url = data.getData().toString();
                // lấy oke đường dẫn
//                Log.d("duongDan",data.getData() + "");
//                try {
//                    // convert image sang bitmap
//                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),data.getData());
//                    imgViewHinhThucDon.setImageBitmap(bitmap);  // set lên
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
                // c2 sử dụng setimageuri
                imgViewHinhThucDon.setImageURI(data.getData());
            }
        }
    }
}
