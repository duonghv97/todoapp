package com.example.haduong.todoapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;



import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.haduong.todoapp.fragment.DisplayDinnerTableFragment;
import com.example.haduong.todoapp.fragment.DisplayEmployeeFragment;
import com.example.haduong.todoapp.fragment.DisplayMenuFoodFragment;


public class HomepageActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    // toolbar này là android.v7.widget
    private Toolbar toolbar;
    private TextView txtNameNavigation;
    private FragmentManager fragmentManager;
    // code trong luôn hàm khởi tạo để khi nó khởi tạo nó sẽ có đủ các phương thức
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_homepage);
        initView();
        // cho thằng toolbar vào actionbar và display nó lên
        setSupportActionBar(toolbar);
        //getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);;


        // cho drawerToggle vào trong toolbar để khi sự kiện click theo toolbar luôn
        ActionBarDrawerToggle drawerToggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,
                R.string.mo,R.string.dong){
            // kế thừa 2 phương thức opened và closed
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }
        };
        // nó mới khai báo drawerToggle thôi chưa biết là của drawerLayout nào
        // cho drawerToggle vào layout và đồng bộ
        drawerLayout.setDrawerListener(drawerToggle);
        drawerToggle.syncState(); // đồng bộ

        // cho nó lấy màu mặc định của icon
        navigationView.setItemIconTintList(null);
        // item trong navigation lắng nghe sự kiện được chọn
        navigationView.setNavigationItemSelectedListener(this);
        Intent intent = getIntent();
        String username = intent.getStringExtra("nameLogin");
        txtNameNavigation.setText(username);
        fragmentManager = getSupportFragmentManager();
        // để mặc định khi chạy nó sẽ vào đây
        displayItemHompage();

    }
    private void initView() {
        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        navigationView = (NavigationView) findViewById(R.id.navigationViewHomepage);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
       // thêm layout header vào
        View view = navigationView.inflateHeaderView(R.layout.layout_hearder_navigation_homepage);
        // k có view vấn gọi được nhưng mà bị nullexception
        txtNameNavigation = (TextView) view.findViewById(R.id.txtNameNavigation);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        // giống như menu, để kiểm tra xem thằng nào được click thì sử getId
        int id = menuItem.getItemId();
        switch (id) {
            case R.id.itHomepage:
                displayItemHompage();

                menuItem.setChecked(true); // để biết là thằng home đang được check
                drawerLayout.closeDrawers(); // đóng drawerlayout lại
                break;
            case R.id.itThucdon:
                FragmentTransaction tranDisplayMenuFood = fragmentManager.beginTransaction();
                DisplayMenuFoodFragment displayMenuFoodFragment = new DisplayMenuFoodFragment();
                // add thằng fragment dinner vào Fragment có id là content
                tranDisplayMenuFood.replace(R.id.content, displayMenuFoodFragment);
                tranDisplayMenuFood.commit();//thực hiện

                menuItem.setChecked(true);
                drawerLayout.closeDrawers();
                break;
            case R.id.itNhanvien:
                FragmentTransaction tranEmployee = fragmentManager.beginTransaction();
                DisplayEmployeeFragment displayEmployeeFragment = new DisplayEmployeeFragment();
                tranEmployee.replace(R.id.content,displayEmployeeFragment);
                tranEmployee.commit();

                menuItem.setChecked(true);
                drawerLayout.closeDrawers();
                break;
        }
        return false;
    }
    private void displayItemHompage() {
        // add fragment vao, phải có fragment tranaction
        // tạo ra fragmettran để cho ra
        FragmentTransaction tranDinnerTable = fragmentManager.beginTransaction();
        DisplayDinnerTableFragment displayDinnerTableFragment = new DisplayDinnerTableFragment();
        // add thằng fragment dinner vào Fragment có id là content
        tranDinnerTable.replace(R.id.content, displayDinnerTableFragment);
        tranDinnerTable.commit();//thực hiện
    }
}
