package com.example.haduong.todoapp.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.haduong.todoapp.DAO.NhanVienDAO;
import com.example.haduong.todoapp.R;
import com.example.haduong.todoapp.RegisterActivity;
import com.example.haduong.todoapp.adapter.EmployeeAdapter;
import com.example.haduong.todoapp.model.NhanVien;

import java.util.ArrayList;

public class DisplayEmployeeFragment extends Fragment {
    private ListView listViewEmployee;
    private EmployeeAdapter employeeAdapter;
    private NhanVienDAO nhanVienDAO;
    private ArrayList<NhanVien> listEmployee;
    public static int REQUEST_CODE_EDIT_EMPLOYEE = 11;
    private int idRole = 0;
    private SharedPreferences sharedPreferences;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // gán layout
        View view = inflater.inflate(R.layout.layout_display_employee,container,false);
        setHasOptionsMenu(true); // khai báo nó có menu
        listViewEmployee = (ListView) view.findViewById(R.id.listViewEmployee);
        nhanVienDAO = new NhanVienDAO(getActivity());
        displayListEmployee();
        sharedPreferences = getActivity().getSharedPreferences("dataLogin",Context.MODE_PRIVATE);
        idRole = sharedPreferences.getInt("idRole",0);
        if(idRole == 1) {
            registerForContextMenu(listViewEmployee);
        }

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        if(idRole == 1) {
            MenuItem menuItem = menu.add(1,R.id.itThemNhanVien,1,getResources().getString(R.string.add_employee));
            menuItem.setIcon(R.drawable.nhanvien);
            // hiển thị nếu còn trống. Tức là cái mình tạo cái menu có 3 trường. Đến thằng nào thì hiển
            // thị thằng đó thôi
            menuItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.itThemNhanVien:
                Intent intent = new Intent(getActivity(),RegisterActivity.class);
                startActivity(intent);
                break;
        }
        return true;
    }

    // tạo context menu cho edit và delete employee


    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        getActivity().getMenuInflater().inflate(R.menu.edit_context_menu,menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        int id = item.getItemId();
        AdapterView.AdapterContextMenuInfo menuInfo = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        int position = menuInfo.position;
//        Log.d("vitri",position + "");
        int maNV = listEmployee.get(position).getManv();
        Log.d("manhanvien",maNV + "");
        switch (id) {
            case R.id.itEdit:
                Intent intentEdit = new Intent(getActivity(),RegisterActivity.class);
                intentEdit.putExtra("maNV",maNV);
                startActivityForResult(intentEdit,REQUEST_CODE_EDIT_EMPLOYEE);
                break;
            case R.id.itDelete:
                boolean checkDelete = nhanVienDAO.deleteEmployee(maNV);
                if(checkDelete) {
                    displayListEmployee();
                    Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.delete_done), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.delete_false), Toast.LENGTH_SHORT).show();
                }
                break;
        }
        return super.onContextItemSelected(item);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_CODE_EDIT_EMPLOYEE && resultCode == Activity.RESULT_OK && data != null) {
            // dữ liệu về sẽ nằm trong data
            boolean check = data.getBooleanExtra("checkEditEmployee",false);
            if(check) {
                displayListEmployee();
                Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.edit_done), Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.edit_false), Toast.LENGTH_SHORT).show();
            }
        }
    }

    // hiển thị danh sách nhân viên
    private void displayListEmployee() {
        listEmployee = nhanVienDAO.getListEmployee();
        employeeAdapter = new EmployeeAdapter(getActivity(),R.layout.layout_custom_display_employee,listEmployee);
        listViewEmployee.setAdapter(employeeAdapter);
        employeeAdapter.notifyDataSetChanged();
    }
}
