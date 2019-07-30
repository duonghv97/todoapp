package com.example.haduong.todoapp;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.haduong.todoapp.DAO.NhanVienDAO;
import com.example.haduong.todoapp.DAO.RoleDAO;
import com.example.haduong.todoapp.fragment.DatePickerFragment;
import com.example.haduong.todoapp.fragment.DisplayEmployeeFragment;
import com.example.haduong.todoapp.model.NhanVien;
import com.example.haduong.todoapp.model.Role;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener,View.OnFocusChangeListener {
    private EditText edtUsername,edtPassword,edtBirthday,edtCMND;
    private Button btnYes,btnExit;
    private TextView txtTitle;
    private RadioGroup radioGroupSex;
    private RadioButton rbMale, rbFemale;
    private Spinner spinRole;
    private String sex;
    private NhanVienDAO nhanVienDAO; // sử dụng cho việc thao tác với bảng NHANVIEN
    private int maNV = 0; // check xem là thêm hay sửa, maNV là mã truyền từ fragment display employee
    private FragmentManager fragmentManager;
    private int firstLog = 0;
    private ArrayList<Role> listRole;
    // do sử dụng array adapter nên nó chỉ hỗ trợ array là string thôi
    private ArrayList<String> listNameRole;
    private RoleDAO roleDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_register);
        initView();
        // this ở đây là thằng Login nó đã implement rồi nên thoải mái onclick
        btnYes.setOnClickListener(this); // trỏe đến onclicklistenner
        btnExit.setOnClickListener(this);

        edtBirthday.setOnFocusChangeListener(this);
        // truyền context cho nó vào create database, kết nối với database
        nhanVienDAO = new NhanVienDAO(this);
        roleDAO = new RoleDAO(this);
        roleDAO.addRole("admin");
        roleDAO.addRole("employee");
        listRole = roleDAO.getAllRole();
        listNameRole = new ArrayList<>();
        for(int i = 0 ; i < listRole.size() ;i++) {
            listNameRole.add(listRole.get(i).getNameRole());
        }


        maNV = getIntent().getIntExtra("maNV",0);
        // lần đầu nhẫn mà là admin
        firstLog = getIntent().getIntExtra("firstLog",0);

        // nếu là lần log đăng kí đầu tiên thì là admin từ 1
        if(firstLog != 1) {
            // sử dụng array adapter, layout thì sử dụng layout mặc định là android.R.layout.simple_list
            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>
                    (this,android.R.layout.simple_list_item_1,listNameRole);
            spinRole.setAdapter(arrayAdapter);
            arrayAdapter.notifyDataSetChanged();
        } else {
            spinRole.setVisibility(View.GONE); // lần đầu ẩn spinner luôn
        }

        if(maNV != 0) { // sửa xong đẩy dữ liệu lên các trường, code cập nhật
            txtTitle.setText(getResources().getString(R.string.update_employee));
            NhanVien nhanVien = nhanVienDAO.getEmployeeById(maNV);
            edtUsername.setText(nhanVien.getTenDN());
            edtPassword.setText(nhanVien.getMatKhau());
            edtBirthday.setText(nhanVien.getNgaySinh());
            edtCMND.setText(String.valueOf(nhanVien.getCMND()));
            String gioiTinh = nhanVien.getGioiTinh();
            if(gioiTinh.equals("Nam")) {
                rbMale.setChecked(true);
            } else {
                rbFemale.setChecked(true);
            }
        }
    }
    private void initView() {
        txtTitle = (TextView) findViewById(R.id.txtTitle);
        edtUsername = (EditText) findViewById(R.id.edtUsername);
        edtPassword = (EditText) findViewById(R.id.edtPassword);
        edtBirthday = (EditText) findViewById(R.id.edtBirthday);
        edtCMND = (EditText) findViewById(R.id.edtCMND);
        btnYes = (Button) findViewById(R.id.btnYes);
        btnExit = (Button) findViewById(R.id.btnExit);
        radioGroupSex = (RadioGroup) findViewById(R.id.radioGroupSex);
        rbMale = (RadioButton) findViewById(R.id.rbMale);
        rbFemale = (RadioButton) findViewById(R.id.rbFemale);
        spinRole = (Spinner) findViewById(R.id.spinRole);
    }

    // View đại diện cho thằng gọi sự kiện click nên ta lấy được id
    // sẽ biết thằng nào gọi sự kiện click
    @Override
    public void onClick(View v) {
        int id = v.getId(); // id la int
        switch (id) {
            case R.id.btnYes:
                if(maNV != 0) { // sửa
                    suaNhanVien();
                    finish();
                } else { // thêm nhân viên
                    themNhanVien();
                }
                break;
            case R.id.btnExit:
                finish(); // thoát ra và dừng lại
                break;
        }

    }

    private void themNhanVien() {
        String username = edtUsername.getText().toString();
        String password = edtPassword.getText().toString();
        // thằng radiogroup cũng sử dụng getid để biết là chọn thằng nào
        switch (radioGroupSex.getCheckedRadioButtonId()) {
            case R.id.rbMale:
                sex = "Nam";
                break;
            case R.id.rbFemale:
                sex = "Nữ";
                break;
        }
        String birthday = edtBirthday.getText().toString();
        String CMND = edtCMND.getText().toString();

        if(username == null || username.equals("")) {
            // getResources().getString thì trả về kiểu string
            // còn R.String.ten thì trả về int
            Toast.makeText(this, getResources().getString(R.string.info_input_username), Toast.LENGTH_SHORT).show();
        } else if(password == null || username.equals("")) {
            Toast.makeText(this,getResources().getString(R.string.info_input_password), Toast.LENGTH_SHORT).show();
        } else {
            NhanVien nhanVien = new NhanVien();
            nhanVien.setTenDN(username);
            nhanVien.setMatKhau(password);
            nhanVien.setGioiTinh(sex);
            nhanVien.setNgaySinh(birthday);
            nhanVien.setCMND(Integer.parseInt(CMND));
            if(firstLog == 1) {
                // gán mặc định quyền nhân viên là admin
                nhanVien.setIdRole(1); // tức là admin
            } else {
                 // gàn quyền là quyền mà admin chọn
                int position = spinRole.getSelectedItemPosition(); // lấy được vị trí mình chọn
                // từ vị trí đó ta trỏ vào trong list đó lấy mã. Do cái list đó nó cho dữ liệu tên nên
                // spiner tính từ 1 á
                // idRole được lấy từ csdl set tự động tăng từ 1. Mà 1 trùng với admin rồi,
                // thì 2 sẽ là employee
                int idRole = listRole.get(position).getIdRole();
//                Log.d("idRole",idRole+"");
                nhanVien.setIdRole(idRole);
            }


            long check = nhanVienDAO.addNhanVien(nhanVien);
            if(check != 0) {
                Intent intent = new Intent(RegisterActivity.this,HomepageActivity.class);
                startActivity(intent);
                Toast.makeText(this,getResources().getString(R.string.add_good), Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, getResources().getString(R.string.add_false), Toast.LENGTH_SHORT).show();
            }
        }
    }

    //sửa nhân viên
    private void suaNhanVien() {
        String username = edtUsername.getText().toString();
        String password = edtPassword.getText().toString();
        // thằng radiogroup cũng sử dụng getid để biết là chọn thằng nào
        switch (radioGroupSex.getCheckedRadioButtonId()) {
            case R.id.rbMale:
                sex = "Nam";
                break;
            case R.id.rbFemale:
                sex = "Nữ";
                break;
        }
        String birthday = edtBirthday.getText().toString();
        String CMND = edtCMND.getText().toString();

        if(username == null || username.equals("")) {
            // getResources().getString thì trả về kiểu string
            // còn R.String.ten thì trả về int
            Toast.makeText(this, getResources().getString(R.string.info_input_username), Toast.LENGTH_SHORT).show();
        } else if(password == null || username.equals("")) {
            Toast.makeText(this,getResources().getString(R.string.info_input_password), Toast.LENGTH_SHORT).show();
        } else {
            NhanVien nhanVien = new NhanVien();
            nhanVien.setManv(maNV);
            nhanVien.setTenDN(username);
            nhanVien.setMatKhau(password);
            nhanVien.setGioiTinh(sex);
            nhanVien.setNgaySinh(birthday);
            nhanVien.setCMND(Integer.parseInt(CMND));

            boolean check = nhanVienDAO.updateEmployee(nhanVien);
            Intent intent = new Intent();
            intent.putExtra("checkEditEmployee",check);
            setResult(Activity.RESULT_OK,intent);

        }
    }
    // check xem khi focus
    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        // nếu có thằng view nào tác động thì view sẽ lấy được id của nó
        int id = v.getId();
        switch (id) {
            case R.id.edtBirthday:
                // nếu có focus thì xuất popup ngày sinh
                if(hasFocus) {
                    // gọi ra
                    DatePickerFragment datePickerFragment = new DatePickerFragment();
                    datePickerFragment.show(getSupportFragmentManager(),"Ngày sinh");
                    // chọn xong thì nó sẽ lại hiện lên edit text
                }
                break;
        }
    }


}
