package com.example.haduong.todoapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.haduong.todoapp.DAO.NhanVienDAO;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    // phuong thuc khoi tao, trong oncreate thì sử dụng setcontentview
    private Button btnYes,btnRegister;
    private EditText edtUsername,edtPassword;
    private CheckBox ckbRemember;
    private LoginButton loginButton;
    private NhanVienDAO nhanVienDAO;
    private ProgressDialog mDialog;
    private ImageView imgAvata;
    private TextView txtEmailFB, txtBirthdayFB,txtFriendsFB;
    // phân biệt là admin hay là employee thì đều phải đăng nhập,
    // nên ta bắt mã quyền của bọn nó tại login
    private SharedPreferences sharedPreferences;
    private CallbackManager callbackManager;

    // nhận dữ facebook trả về
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode,resultCode,data);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_login);
        initView();
//        printKeyHash(); chỉ để lấy key thôi
        // login with facebook
        callbackManager = CallbackManager.Factory.create();
        loginButton.setReadPermissions(Arrays.asList("public_profile","email","user_birthday","user_friends"));
        // xu ly du lieu facebook tra ve
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                mDialog = new ProgressDialog(LoginActivity.this);
                mDialog.setMessage("Retrieving data...");
                mDialog.show();

                String accessToken = loginResult.getAccessToken().getToken();
                GraphRequest graphRequest = GraphRequest.newMeRequest(loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        mDialog.dismiss();
                        getFacebookData(object);
                    }
                });

                // Request Graph API
                Bundle paramaters = new Bundle();
                paramaters.putString("fields","id,email,birthday,friends");
                graphRequest.setParameters(paramaters);
                graphRequest.executeAsync();

            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {

            }
        });

        // neu da ton tai
        if(AccessToken.getCurrentAccessToken() != null) {
            txtEmailFB.setText(AccessToken.getCurrentAccessToken().getUserId());
        }
        // login theo username và password
        nhanVienDAO = new NhanVienDAO(this);
        // tạo ra đối tượng sharepreference, với tên file và kiểu dùng
        sharedPreferences = getSharedPreferences("dataLogin",MODE_PRIVATE);
        // đối tượng shareprefence lấy dữ liệu ra
        edtUsername.setText(sharedPreferences.getString("username",""));
        edtPassword.setText(sharedPreferences.getString("password",""));

        btnYes.setOnClickListener(this); // co sự kiện đnăg kí thì phri đăng kí ngay
        btnRegister.setOnClickListener(this); // this laf new doi tuong loginactivity

    }

    private void getFacebookData(JSONObject object) {
        try{
            URL profile_picture = new URL("https://graph.facebook.com/" + object.getString("id")
                    + "/picture?width=250&height=250");
            Picasso.with(this).load(profile_picture.toString()).into(imgAvata);
            txtEmailFB.setText(object.getString("email"));
            txtBirthdayFB.setText(object.getString("birthday"));
            txtFriendsFB.setText(object.getJSONObject("friends").getJSONObject("summary").getString("total_count"));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void printKeyHash() {
        try{
            PackageInfo info = getPackageManager().getPackageInfo("com.example.haduong.todoapp",
                    PackageManager.GET_SIGNATURES);
            for(Signature signature:info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash",Base64.encodeToString(md.digest(),Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }
    private void initView() {
        btnYes = (Button) findViewById(R.id.btnYesLogin);
        btnRegister = (Button) findViewById(R.id.btnRegister);
        edtUsername = (EditText) findViewById(R.id.edtUsernameLogin);
        edtPassword = (EditText) findViewById(R.id.edtPasswordLogin);
        ckbRemember = (CheckBox) findViewById(R.id.ckbRemember);
        loginButton = (LoginButton) findViewById(R.id.login_button);
        imgAvata = (ImageView) findViewById(R.id.imgAvata);
        txtEmailFB = (TextView) findViewById(R.id.txtEmailFB);
        txtBirthdayFB = (TextView) findViewById(R.id.txtBirthdayFB);
        txtFriendsFB = (TextView) findViewById(R.id.txtFriendsFB);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId(); // id này là cửa thằng khi người dùng click vào thằng nào thì view nó bắt
        switch (id) {
            case R.id.btnYesLogin:
                setBtnYes();
                break;
            case R.id.btnRegister:
                setBtnRegister();
                ;break;
        }

    }
    // hàm thực khi click vào button đồng ý
    private void setBtnYes() {
        String username = edtUsername.getText().toString();
        String password = edtPassword.getText().toString();
        // cho trả về mã nhân viên
        int maNhanVien = nhanVienDAO.checkLogin(username,password);
        int idRole = nhanVienDAO.getIdRole(maNhanVien);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("idRole",idRole);
        editor.commit();
        // xong ở  từng màn hình chỗ nào muốn
        if(maNhanVien != 0) { // thành công
            if(ckbRemember.isChecked()) {
                // tạo đối tượng editor để thao tác với dữ liệu
                // ghi vào file shareprefecens
                editor.putString("username",username);
                editor.putString("password",password);
                editor.putBoolean("checked",true);

                // lấy mã quyền thì get từ share preference
                editor.commit(); // hoàn tất việc ghi vào file
            } else {
                editor.remove("username");
                editor.remove("password");
                editor.remove("checked");
                editor.commit();
            }
           Intent intent = new Intent(LoginActivity.this,HomepageActivity.class);
           intent.putExtra("nameLogin",edtUsername.getText().toString());
           // truyền mã nhân viên từ login activity vào homeactivity.
            // Thằng fragment con nào của home muốn lấy thì get ra
           intent.putExtra("maNhanVien",maNhanVien);
           startActivity(intent);
        } else {
            Toast.makeText(this, "Đăng nhập không thành công!", Toast.LENGTH_SHORT).show();
        }
    }
    // hàm thực thi khi click vào register
    private void setBtnRegister() {
        // check lần đầu tiên thì ta gửi mã sang
        Intent intent = new Intent(LoginActivity.this,RegisterActivity.class);
        intent.putExtra("firstLog",1);
        startActivity(intent);
    }
}
