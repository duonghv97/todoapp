package com.example.haduong.todoapp.DAO;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.haduong.todoapp.database.CreateDatabase;
import com.example.haduong.todoapp.model.ChiTietGoiMon;
import com.example.haduong.todoapp.model.GoiMon;
import com.example.haduong.todoapp.model.Payment;

import java.util.ArrayList;

public class GoiMonDAO {
    private SQLiteDatabase database;

    public GoiMonDAO(Context context) {
        CreateDatabase createDatabase = new CreateDatabase(context);
        database = createDatabase.open();
    }

    public boolean addGoiMon(GoiMon goiMon) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(CreateDatabase.TB_GOIMON_MaNV,goiMon.getMaNhanVien());
        contentValues.put(CreateDatabase.TB_GOIMON_MaBan,goiMon.getMaBan());
        contentValues.put(CreateDatabase.TB_GOIMON_TinhTrang,goiMon.getStatusPayment());
        contentValues.put(CreateDatabase.TB_GOIMON_NgayGoi,goiMon.getDayOrder());
        long check = database.insert(CreateDatabase.TB_GOIMON,null,contentValues);
        if(check != 0) {
            return true;
        } else {
            return false;
        }

    }
    public int getMaGoiMonByMaBan(int maBan, String status) {
        int maGoiMon = 0;
        String sql = "SELECT * FROM " + CreateDatabase.TB_GOIMON + " WHERE "
                + CreateDatabase.TB_GOIMON_MaBan + " = ? AND "
                + CreateDatabase.TB_GOIMON_TinhTrang + " = ?";
        Cursor cursor = database.rawQuery(sql,new String[]{maBan + "",status});
        cursor.moveToFirst();
        while(!cursor.isAfterLast()) {
            maGoiMon = cursor.getInt(
                    cursor.getColumnIndex(CreateDatabase.TB_GOIMON_MaGoiMon));
            cursor.moveToNext();
        }
        return maGoiMon;
    }

    // kiểm tra món ăn đã tồn tại
    public boolean checkFoodExist(int maGoiMon,int maMonAn) {
        String sql = "SELECT * FROM " + CreateDatabase.TB_CHITIETGOIMON + " WHERE "
                + CreateDatabase.TB_CHITIETGOIMON_MaGoiMon + " = ? AND "
                + CreateDatabase.TB_CHITIETGOIMON_MaMonAn + " = ?";
        Cursor cursor = database.rawQuery(sql, new String[]{maGoiMon + "",maMonAn + ""});
        if(cursor.getCount() > 0) {
            return true; // có
        } else {
            return false; // không có
        }
    }

    // lấy số lượng món ăn theo mã gọi món
    public long getQuantityFoodByMaGoiMon(int maGoiMon,int maMonAn) {
        int soLuong = 0;
        String sql = "SELECT * FROM " + CreateDatabase.TB_CHITIETGOIMON + " WHERE "
                + CreateDatabase.TB_CHITIETGOIMON_MaGoiMon + " = ? AND "
                + CreateDatabase.TB_CHITIETGOIMON_MaMonAn + " = ?";
        Cursor cursor = database.rawQuery(sql, new String[]{maGoiMon + "",maMonAn + ""});
        cursor.moveToFirst();
        while(!cursor.isAfterLast()) {
            soLuong = cursor.getInt(
                    cursor.getColumnIndex(CreateDatabase.TB_CHITIETGOIMON_SoLuong));
            cursor.moveToNext();
        }
        return soLuong;
    }

    public boolean addChiTietGoiMon(ChiTietGoiMon chiTietGoiMon) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(CreateDatabase.TB_CHITIETGOIMON_MaGoiMon,chiTietGoiMon.getMaGoiMon());
        contentValues.put(CreateDatabase.TB_CHITIETGOIMON_MaMonAn,chiTietGoiMon.getMaMonAn());
        contentValues.put(CreateDatabase.TB_CHITIETGOIMON_SoLuong,chiTietGoiMon.getSoLuong());
        long check = database.insert(CreateDatabase.TB_CHITIETGOIMON,null,contentValues);
        if(check > 0) {
            return true;
        } else {
            return false;
        }
    }
    // cập nhật số lượng món ăn
    public boolean updateQuantity(ChiTietGoiMon chiTietGoiMon) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(CreateDatabase.TB_CHITIETGOIMON_SoLuong,chiTietGoiMon.getSoLuong());
        String whereClause = CreateDatabase.TB_CHITIETGOIMON_MaGoiMon + " = ? AND "
                + CreateDatabase.TB_CHITIETGOIMON_MaMonAn + " = ?";
        long check = database.update(CreateDatabase.TB_CHITIETGOIMON,contentValues,whereClause,
                new String[]{chiTietGoiMon.getMaGoiMon() + "",chiTietGoiMon.getMaMonAn() + ""});
        if(check > 0) {
            return true;
        } else {
            return false;
        }
    }

    // lấy danh món ăn bằng mã gọi món
    public ArrayList<Payment> getListFoodByMaGoiMon(int maGoiMon) {
        ArrayList<Payment> listFoodPayment = new ArrayList<>();
        // join 2 bảng lại với nhau
        String sql = "SELECT * FROM " + CreateDatabase.TB_CHITIETGOIMON + " CT,"
                + CreateDatabase.TB_MONAN + " MA WHERE " + " CT." + CreateDatabase.TB_CHITIETGOIMON_MaMonAn
                + " = MA." + CreateDatabase.TB_MONAN_MaMon + " AND CT."
                + CreateDatabase.TB_CHITIETGOIMON_MaGoiMon + " = ?";

        Cursor cursor = database.rawQuery(sql,new String[]{maGoiMon + ""});
        cursor.moveToFirst();
        while(!cursor.isAfterLast()) {
            Payment payment = new Payment();
            // tên và tiền lấy từ bảng món ăn, còn số lượng trong chi tiết
            payment.setTenMonAn(cursor.getString(
                    cursor.getColumnIndex(CreateDatabase.TB_MONAN_TenMon)));
            payment.setGiaTien(cursor.getInt(
                    cursor.getColumnIndex(CreateDatabase.TB_MONAN_GiaTien)));
            payment.setSoLuong(cursor.getInt(
                    cursor.getColumnIndex(CreateDatabase.TB_CHITIETGOIMON_SoLuong)));
            listFoodPayment.add(payment);
            cursor.moveToNext();
        }
        return listFoodPayment;
    }

//    // cập nhật trạng thái gọi món theo mã bàn
    public boolean updateStatusGoiMonByMaBan(int maBan,String statusPayment) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(CreateDatabase.TB_GOIMON_TinhTrang,statusPayment);
        String whereClause = CreateDatabase.TB_GOIMON_MaBan + " = ?";
        int check = database.update(CreateDatabase.TB_GOIMON,contentValues,whereClause,new String[]{maBan+""});
        if(check > 0) {
            return true;
        } else {
            return false;
        }
    }
}
