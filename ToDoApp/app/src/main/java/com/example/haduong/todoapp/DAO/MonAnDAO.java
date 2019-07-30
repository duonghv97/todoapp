package com.example.haduong.todoapp.DAO;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.haduong.todoapp.database.CreateDatabase;
import com.example.haduong.todoapp.model.MonAn;

import java.util.ArrayList;

public class MonAnDAO {
    private SQLiteDatabase database;
    public MonAnDAO(Context context) {
        CreateDatabase createDatabase = new CreateDatabase(context);
        database = createDatabase.open();
    }

    public boolean themMonAn(MonAn monAn) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(CreateDatabase.TB_MONAN_TenMon,monAn.getTenMonAn());
        contentValues.put(CreateDatabase.TB_MONAN_GiaTien,monAn.getGiaTien());
        contentValues.put(CreateDatabase.TB_MONAN_MaLoai,monAn.getMaLoai());
        contentValues.put(CreateDatabase.TB_MONAN_HinhAnh,monAn.getHinhAnh());
        // hàm insert trả về số nguyên
        long check = database.insert(CreateDatabase.TB_MONAN,null,contentValues);
        if(check > 0) {
            return true;
        } else {
            return false;
        }

    }
    // lấy ra tất cả hình ảnh của món ăn có chung mã loại món ăn
    public ArrayList<MonAn> getListFood(int maLoai) {
        // sử dụng hỏi chấm chèn vào sql cho đồng nhất
        ArrayList<MonAn> listFood = new ArrayList<>();

        String sql = "SELECT * FROM " + CreateDatabase.TB_MONAN + " WHERE "
                + CreateDatabase.TB_MONAN_MaLoai + " = ?";
        Cursor cursor = database.rawQuery(sql,new String[]{maLoai + ""});
        cursor.moveToFirst();
        while(!cursor.isAfterLast()) {
            MonAn monAn = new MonAn();
            // để an toàn thì sử dụng cursor.getcolumnindex để biết vị trí trong sql
            monAn.setMaMonAn(cursor.getInt(cursor.getColumnIndex(CreateDatabase.TB_MONAN_MaMon)));
            monAn.setTenMonAn(cursor.getString(cursor.getColumnIndex(CreateDatabase.TB_MONAN_TenMon)));
            monAn.setGiaTien(cursor.getString(cursor.getColumnIndex(CreateDatabase.TB_MONAN_GiaTien)));
            // cộng thêm nháy để khi không có chuỗi thì nó không phải null
            monAn.setHinhAnh(cursor.getString(cursor.getColumnIndexOrThrow(CreateDatabase.TB_MONAN_HinhAnh))+"");
            monAn.setMaLoai(cursor.getInt(cursor.getColumnIndex(CreateDatabase.TB_MONAN_MaLoai)));
            listFood.add(monAn);
            cursor.moveToNext();
        }
        return listFood;
    }
    // xoa mon an
    public boolean deleteFood(int maMon) {
        long check = database.delete(CreateDatabase.TB_MONAN,
                CreateDatabase.TB_MONAN_MaMon + " = ?",new String[]{maMon + ""});
        if(check != 0) {
            return true;
        } else {
            return false;
        }
    }
}
