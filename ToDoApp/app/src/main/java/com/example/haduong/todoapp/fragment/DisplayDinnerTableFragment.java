package com.example.haduong.todoapp.fragment;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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

import com.example.haduong.todoapp.AddDinnerTableActivity;
import com.example.haduong.todoapp.DAO.BanAnDAO;
import com.example.haduong.todoapp.EditTableDinnerActivity;
import com.example.haduong.todoapp.HomepageActivity;
import com.example.haduong.todoapp.R;
import com.example.haduong.todoapp.adapter.BanAnAdapter;
import com.example.haduong.todoapp.model.BanAn;

import java.util.ArrayList;

public class DisplayDinnerTableFragment extends Fragment {
    public static int REQUEST_CODE_ADD = 111;
    public static int REQUEST_CODE_EDIT = 225;
    private GridView gvDisplayDinnerTable;
    private ArrayList<BanAn> listBanAn;
    private BanAnDAO banAnDAO; // để gọi được phương thức trong CSDL
    private BanAnAdapter banAnAdapter;
    private int idRole = 0;
    private SharedPreferences sharedPreferences;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Fragment thì sử dụng oncreate View tạo giao diện trọng fragment,
        // 3 đối số
        // sử dụng LayoutInflater để chuyển layout xml sang thành view trong java code
        // tham số 1 là layout, trong resource dạng int, tham số 2 là viewgroup, tham số 3 là root
        View view = inflater.inflate(R.layout.layout_display_table,container,false);
        setHasOptionsMenu(true); // để fragment la menu
        // những file mà không liên quan đến layout, mà muốn sử dụng id của layout đó
        // thì phải sử dụng inflate gán vào view để trỏ sang layout đó rồi mới lấy được
        //set title
        ((HomepageActivity) getActivity()).getSupportActionBar().setTitle(R.string.homepage);
        gvDisplayDinnerTable = (GridView) view.findViewById(R.id.gridViewDisplayTable);
        sharedPreferences = getActivity().getSharedPreferences("dataLogin",Context.MODE_PRIVATE);
        idRole = sharedPreferences.getInt("idRole",0);
        // gvDisplayDinnerTable.OnItemClickListener()
        // lấy dữ liệu
        banAnDAO = new BanAnDAO(getActivity());
        displayGVTableDinner();

        // đăng kí context menu với grid view để nó nằm trong gridview
        // khi bấm vào item nào trong gridview thì nó sẽ chạy hiển thị context menu
        // và xử lý sự kiện trong item selected
        // nếu là admin thì mới được xóa và sửa, cho phép đăng kí context menu, nhẹ code
        if(idRole == 1) {
            registerForContextMenu(gvDisplayDinnerTable);
        }
        return view;
    }

    // tạo context menu khi long click vào bàn ăn, context menu tương tự như menu
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        // chuyển đổi từ xml sang java code để hiển thị menu
        getActivity().getMenuInflater().inflate(R.menu.edit_context_menu,menu);
    }

    // khi chọn item trong context menu, long click vào item trong grid view
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        int id = item.getItemId();
        // sử dụng adapter info để lấy ra vị trí của của item(bàn ăn) được chọn
        AdapterView.AdapterContextMenuInfo menuInfo = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        int position = menuInfo.position; // vị trí được chọn
        int maBan = listBanAn.get(position).getMaBanAn();
        switch (id) {
            case R.id.itEdit:
                Intent intent = new Intent(getActivity(),EditTableDinnerActivity.class);
                intent.putExtra("maBan",maBan);
                // gửi mã sang xong bên nhận sẽ set dữ liệu lên trên màn
                startActivityForResult(intent,REQUEST_CODE_EDIT);
                break;
            case R.id.itDelete:
                boolean check = banAnDAO.deleteDinnerTableByMaBan(maBan);
                if(check) {
                    // xoas thanfh công thì hiển thị lại bàn
                    displayGVTableDinner();
                    Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.delete_done), Toast.LENGTH_SHORT).show();
                    
                } else {
                    Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.delete_false), Toast.LENGTH_SHORT).show();
                }
                break;
        }

        return super.onContextItemSelected(item);
    }

    // add thằng fragment này vào thằng to thì cũng phải làm các action của nó
    @Override // sự kiện khi khởi tạo menu rồi
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        // nếu là admin thì mới hiện thêm bàn ăn
        if(idRole == 1) {
            // add chung 1 menu item để sổ ra cho tất cả các item, R.string.themBanAn là tên hiển thị
            MenuItem itThemBanAn = menu.add(1,R.id.itThemBanAn,1,R.string.themBanAn);
            itThemBanAn.setIcon(R.drawable.thembanan); // do mỗi thằng item sẽ có icon khác nhau
            // nếu đặt ở bên item_menu thì sẽ coi như fix 1 cái rồi.
            // nếu còn chỗ trống thì gọi tiếp
            itThemBanAn.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
        }
    }

    // khi click vào item, đối với menu
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.itThemBanAn:
                // click vào thì show popup thêm bàn ăn vào
                // fragment này nằm trong hompageActivity nên không dùng this được,
                // mà gọi getActitivy thì nó sẽ trỏ đến hompage
                Intent intent = new Intent(getActivity(),AddDinnerTableActivity.class);
                // sử forresult thì tạo thêm request_code
                startActivityForResult(intent,REQUEST_CODE_ADD);
                break;
        }
        return true;
    }

    // tắt màn hình kia thì sẽ chạy vào activity result, do sử sụng for result
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_CODE_ADD ) { // thêm bàn ăn
            if(resultCode == Activity.RESULT_OK) {
                Intent intent = data; // data la intent ben kia tra ve , biến mặc định cho là false
                boolean check = intent.getBooleanExtra("resultThemBanAn",false);
                if(check == true) {
                    displayGVTableDinner();
                    Toast.makeText(getActivity(), getResources().getString(R.string.add_good), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getActivity(), getResources().getString(R.string.add_false), Toast.LENGTH_SHORT).show();
                }
            }
        } else if(requestCode == REQUEST_CODE_EDIT){ // sửa bàn ăn
            if(resultCode == Activity.RESULT_OK) {
                Intent intentEdit = data;
                boolean checkEdit = intentEdit.getBooleanExtra("checkEditBanAn",false);
                if(checkEdit) {
                    displayGVTableDinner();
                    Toast.makeText(getActivity(), getResources().getString(R.string.edit_done), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getActivity(), getResources().getString(R.string.edit_false), Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private void displayGVTableDinner() {
        listBanAn = banAnDAO.getAllBanAn();
        // chuẩn bị adapter
        banAnAdapter = new BanAnAdapter(getActivity(),R.layout.layout_custom_display_table,listBanAn);
        // chuyển đổi dữ liệu lên
        gvDisplayDinnerTable.setAdapter(banAnAdapter);
        banAnAdapter.notifyDataSetChanged();
    }


}
