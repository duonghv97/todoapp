package com.example.haduong.todoapp.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.ContextMenu;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.example.haduong.todoapp.DAO.MonAnDAO;
import com.example.haduong.todoapp.QuantityActivity;
import com.example.haduong.todoapp.R;
import com.example.haduong.todoapp.adapter.ListFoodAdapter;
import com.example.haduong.todoapp.model.MonAn;

import java.util.ArrayList;

public class DisplayListFoodFragment extends Fragment {
    private GridView gvListFood;
    private ArrayList<MonAn> listFood;
    private ListFoodAdapter listFoodAdapter;
    private MonAnDAO monAnDAO;
    private int idRole = 0;
    private SharedPreferences sharedPreferences;
    private int maBan = 0;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate
                (R.layout.layout_display_menu_food,container,false);
        gvListFood = (GridView) view.findViewById(R.id.gvDisplayMenuFood);
        sharedPreferences = getActivity().getSharedPreferences("dataLogin",Context.MODE_PRIVATE);
        idRole = sharedPreferences.getInt("idRole",0);
        monAnDAO = new MonAnDAO(getActivity());
        // bên nhạn chỉ cần sử dụng getArgument là xong

        disPlayFood();
        // sự kiện nhấn vào nut back
        view.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                // nếu biết keycode của nút đó thì dùng k thì sử dụng event
                if(event.getAction() ==  KeyEvent.ACTION_DOWN) {
                    // lấy ra fragment manager, xong gọi popBackStack để quay lại, còn lại là cờ pop_back_stack_inclusive
                    getFragmentManager().popBackStack("typeFood",
                            FragmentManager.POP_BACK_STACK_INCLUSIVE);
                }
                return false;
            }
        });
        if(idRole == 1) {
            registerForContextMenu(gvListFood);
        }

        // nhớ ở oncreateview xong là return view
        return view;
    }
    private void disPlayFood() {
        Bundle bundle = getArguments();
        if(bundle != null) {
            int maLoai = bundle.getInt("maLoai");
            maBan = bundle.getInt("maBan");
//            Log.d("maBan", maBan + "");
            listFood = monAnDAO.getListFood(maLoai);
            listFoodAdapter = new ListFoodAdapter(getActivity(),
                    R.layout.layout_custom_list_food,listFood);
            // khi setAdapter trong listFood có bao nhiêu món ăn thì nó hiển thị lên hết
            // lấy từ getView, lặp lại getview số lần tương ứng với số món ăn trong loại
            gvListFood.setAdapter(listFoodAdapter);
            listFoodAdapter.notifyDataSetChanged();

            // khi click vào item hình ảnh món ăn
            gvListFood.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    if(maBan != 0) {
                        // hiển thị popup nhập số lượng
                        Intent intent = new Intent(getActivity(),QuantityActivity.class);
                        intent.putExtra("maBan",maBan);
                        // click vào thì lấy được position
                        intent.putExtra("maMonAn",listFood.get(position).getMaMonAn());
//                        Log.d("maMonAn",listFood.get(position).getMaMonAn()+"");
                        startActivity(intent);
                    }


                }
            });
        }
    }
    // create context menu
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
        int maMon = listFood.get(position).getMaMonAn();
        switch (id) {
            case R.id.itEdit:
                break;
            case R.id.itDelete:
                boolean checkDeleteFood = monAnDAO.deleteFood(maMon);
                if(checkDeleteFood) {
                    disPlayFood();
                    Toast.makeText(getActivity(),
                            getActivity().getResources().getString(R.string.delete_done), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getActivity(),
                            getActivity().getResources().getString(R.string.delete_false), Toast.LENGTH_SHORT).show();
                }
                break;
        }
        return super.onContextItemSelected(item);
    }
}
