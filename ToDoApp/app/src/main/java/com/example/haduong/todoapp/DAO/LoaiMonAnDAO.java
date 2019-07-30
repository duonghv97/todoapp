package com.example.haduong.todoapp.DAO;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.haduong.todoapp.database.CreateDatabase;
import com.example.haduong.todoapp.model.LoaiMonAn;

import java.util.ArrayList;

public class LoaiMonAnDAO {
    private SQLiteDatabase database;
    public LoaiMonAnDAO(Context context) {
        CreateDatabase createDatabase = new CreateDatabase(context);
        database = createDatabase.open(); // lấy ra sqllite bằng cách sử dụng hàm open, trong đó kết nối csdl

    }
    public boolean addTypeMenuFood(String tenLoaiMonAn) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(CreateDatabase.TB_LOAIMONAN_TenLoai,tenLoaiMonAn);
        long check = database.insert(CreateDatabase.TB_LOAIMONAN,null,contentValues);
        if(check > 0) {
            return true;
        } else {
            return false;
        }
    }

    public ArrayList<LoaiMonAn > getAllTypeMenuFood() {
        ArrayList<LoaiMonAn> listTypeMenuFood = new ArrayList<>();
        String sql = "SELECT * FROM " + CreateDatabase.TB_LOAIMONAN;
        Cursor cursor = database.rawQuery(sql,null);
        cursor.moveToFirst();
        while(!cursor.isAfterLast()) {
            LoaiMonAn loaiMonAn = new LoaiMonAn();
            loaiMonAn.setMaLoai
                    (cursor.getInt(cursor.getColumnIndex(CreateDatabase.TB_LOAIMONAN_MaLoai)));
            loaiMonAn.setTenLoai
                    (cursor.getString(cursor.getColumnIndex(CreateDatabase.TB_LOAIMONAN_TenLoai)));
            listTypeMenuFood.add(loaiMonAn);
            cursor.moveToNext();
        }
        return listTypeMenuFood;
    }
    // một loại có nhiều món ăn. Nên trong món ăn sẽ có mã loại.
    // Mà ta get được danh sách các loại món ăn thì ra sẽ lấy được mã loại ở bảng loại.
    // Ta đem mã loại này đi tìm trong bảng món ăn, để lấy ra 1 món ăn mới nhất
    // muốn lấy hình ảnh
    public String getImageTypeMenuFood(int maLoai) {
        String hinhAnh = "";
        // lấy giảm dần là do số lớn nhất là món mới nhất lên sắp xếp giảm dần, limit 1 là lấy ra 1
        String sql = "SELECT * FROM " + CreateDatabase.TB_MONAN
                + " WHERE " + CreateDatabase.TB_MONAN_MaLoai + " = '" + maLoai + "' "
                + " AND " + CreateDatabase.TB_MONAN_HinhAnh + " != '' ORDER BY "
                + CreateDatabase.TB_MONAN_MaMon + " LIMIT 1";
        Cursor cursor = database.rawQuery(sql,null);
        cursor.moveToFirst();
        while(!cursor.isAfterLast()) {
            hinhAnh = cursor.getString(cursor.getColumnIndex(CreateDatabase.TB_MONAN_HinhAnh));
            cursor.moveToNext();
        }
        return hinhAnh;
    }

    public boolean deleteLoaiMonAn(int maLoai) {
        long check = database.delete(CreateDatabase.TB_LOAIMONAN,
                CreateDatabase.TB_LOAIMONAN_MaLoai + " = ?",new String[]{maLoai + ""});
        if (check != 0) {
            return true;
        } else {
            return false;
        }
    }

    public boolean updateLoaiMonAn() {
        return false;
    }
}
