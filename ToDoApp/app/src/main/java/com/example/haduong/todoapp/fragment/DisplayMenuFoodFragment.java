package com.example.haduong.todoapp.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.example.haduong.todoapp.AddMenuFoodActivity;
import com.example.haduong.todoapp.DAO.LoaiMonAnDAO;
import com.example.haduong.todoapp.HomepageActivity;
import com.example.haduong.todoapp.R;
import com.example.haduong.todoapp.adapter.LoaiMonAnAdapter;
import com.example.haduong.todoapp.model.LoaiMonAn;

import java.util.ArrayList;

// khi click vào thực đơn thì sẽ hiển thị thực đơn thay cho bàn ăn
public class DisplayMenuFoodFragment extends Fragment  {
    // grid view nằm trong fragment nên khi mà ta bắt sự kiện click cho item trong grid view
    // thì vô fragment
    private GridView gvDisplayMenuFood;
    private LoaiMonAnDAO loaiMonAnDAO;
    private ArrayList<LoaiMonAn> listLoaiMonAn;
    private FragmentManager fragmentManager;
    public static int REQUEST_CODE_UPDATE = 119;
    private int idRole = 0;
    private SharedPreferences sharedPreferences;
    private int maBan = 0;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_display_menu_food,container,false);
        setHasOptionsMenu(true); // lấy cái layout từ view kia và cho thành popup
       // getActivity trả về thằng chứa nó, nhưng k biết là của thằng nào nên ta phải ép kiểu mới biết được
        // lấy ra toolbar(trong action bar)

        ((HomepageActivity)getActivity()).getSupportActionBar().setTitle(R.string.menu);
        loaiMonAnDAO = new LoaiMonAnDAO(getActivity());
        gvDisplayMenuFood = (GridView) view.findViewById(R.id.gvDisplayMenuFood);
        sharedPreferences = getActivity().getSharedPreferences("dataLogin",Context.MODE_PRIVATE);
        idRole = sharedPreferences.getInt("idRole",0);
        // gọi fragment manager ra để sử dụng
        fragmentManager = getActivity().getSupportFragmentManager();

        disPlayGVMenuFood();
        // dữ liệu đóng gói trong thằng bundle
        // khi mình bấm vào icon order
        Bundle dataMenuFood = getArguments();
        if(dataMenuFood != null) {
            maBan = dataMenuFood.getInt("maBan");
        }

        // sự kiện khi click vào item thì hiển thị danh sách món ăn của loại đó lên
        gvDisplayMenuFood.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int maLoai = listLoaiMonAn.get(position).getMaLoai();
                // lấy mã loiaj xong chuyển qua fragment listfood
                // chuyển giữa 2 fragment
                DisplayListFoodFragment displayListFoodFragment = new DisplayListFoodFragment();
                // truyền đối số sang fragment khác thì sử lấy fragmetn đó gọi setAgrument

                Bundle bundle = new Bundle();
                bundle.putInt("maLoai",maLoai);
                bundle.putInt("maBan",maBan); // đẩy mã bàn sang cho list food
                // nnhận vào một bundle thì sử dụng bundle để đóng gói
                displayListFoodFragment.setArguments(bundle);
                FragmentTransaction transaction  = fragmentManager.beginTransaction();
                // replace vào vị trí content, thêm addtobacktrack vào chỗ replace để back lại
                transaction.replace(R.id.content,displayListFoodFragment).addToBackStack("typeFood");
                transaction.commit();
            }
        });


        if(idRole == 1) {
            registerForContextMenu(gvDisplayMenuFood); // đăng kí cái context menu vào gv
        }
        return view;
    }

    // sử dụng context menu cho sửa và xóa dữ liệu, layout thì dùng chung được
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        // convert xml to java code
        getActivity().getMenuInflater().inflate(R.menu.edit_context_menu,menu);
    }

    // bắt sự kiện khi chọn vào item
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        int id = item.getItemId();
        // adapter này chứa position
        AdapterView.AdapterContextMenuInfo menuInfo = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        int position = menuInfo.position;
        // loại món ăn sẽ chứa mã loại
        int maLoai = listLoaiMonAn.get(position).getMaLoai();
        switch (id) {
            case R.id.itEdit:
                break;
            case R.id.itDelete:
                boolean check = loaiMonAnDAO.deleteLoaiMonAn(maLoai);
                if(check) {
                    disPlayGVMenuFood();
                    Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.delete_done), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.delete_false), Toast.LENGTH_SHORT).show();
                }
                break;
        }
        return super.onContextItemSelected(item);
    }

    // khi khởi tạo thì cho cái menu đó vào
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        if(idRole == 1) {
            MenuItem itThemBanAn = menu.add(1,R.id.itThemThucDon,1,R.string.add_menu);
            itThemBanAn.setIcon(R.drawable.logologin); // do mỗi thằng item sẽ có icon khác nhau
            // nếu đặt ở bên item_menu thì sẽ coi như fix 1 cái rồi.
            // nếu còn chỗ trống thì gọi tiếp
            itThemBanAn.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.itThemThucDon:
                //getActivity là lấy activity chứa fragment
                Intent intent = new Intent(getActivity(),AddMenuFoodActivity.class);
                startActivityForResult(intent,REQUEST_CODE_UPDATE);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_CODE_UPDATE && resultCode == Activity.RESULT_OK && data != null) {
            disPlayGVMenuFood();
        }
    }

    private void disPlayGVMenuFood() {
        listLoaiMonAn = loaiMonAnDAO.getAllTypeMenuFood();
        LoaiMonAnAdapter loaiMonAnAdapter = new LoaiMonAnAdapter
                (getActivity(),R.layout.layout_custom_display_type_food,listLoaiMonAn);
        gvDisplayMenuFood.setAdapter(loaiMonAnAdapter);
        loaiMonAnAdapter.notifyDataSetChanged();
    }
}
