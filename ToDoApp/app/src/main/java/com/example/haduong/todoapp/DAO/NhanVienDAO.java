package com.example.haduong.todoapp.DAO;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.haduong.todoapp.database.CreateDatabase;
import com.example.haduong.todoapp.model.NhanVien;

import java.util.ArrayList;

public class NhanVienDAO {
    private SQLiteDatabase database;

    public NhanVienDAO(Context context) {
        // lấy thằng write ra để thực thi
        CreateDatabase createDatabase = new CreateDatabase(context);
        database = createDatabase.open();

    }
    // id trả về kiểu long
    public long addNhanVien(NhanVien nhanVien) {
        ContentValues contentValues = new ContentValues();
        // key và data
        contentValues.put(CreateDatabase.TB_NHANVIEN_TenDN,nhanVien.getTenDN());
        contentValues.put(CreateDatabase.TB_NHANVIEN_MatKhau,nhanVien.getMatKhau());
        contentValues.put(CreateDatabase.TB_NHANVIEN_NgaySinh,nhanVien.getNgaySinh());
        contentValues.put(CreateDatabase.TB_NHANVIEN_GioiTinh,nhanVien.getGioiTinh());
        contentValues.put(CreateDatabase.TB_NHANVIEN_CMND,nhanVien.getCMND());
        contentValues.put(CreateDatabase.TB_NHANVIEN_MaQuyen,nhanVien.getIdRole());
        long check = database.insert(CreateDatabase.TB_NHANVIEN,null,contentValues);
        return check;
    }

    // get role
    public int getIdRole(int maNV) {
        int idRole = 0;
        String sql = "SELECT * FROM " + CreateDatabase.TB_NHANVIEN + " WHERE "
                + CreateDatabase.TB_NHANVIEN_MaNV + " = ?";
        Cursor cursor = database.rawQuery(sql,new String[]{maNV + ""});
        cursor.moveToFirst();
        while(!cursor.isAfterLast()) {
            idRole = cursor.getInt(cursor.getColumnIndex(CreateDatabase.TB_NHANVIEN_MaQuyen));
            cursor.moveToNext();
        }
        return idRole;
    }

    // check Login cho trả về mã nhân viên luôn
    public int checkLogin(String username,String password) {
        int maNhanVien = 0;
        // chú ý nháy đơn ở dối số truyền vào
        String sql = "SELECT * FROM " + CreateDatabase.TB_NHANVIEN + " WHERE " +
                CreateDatabase.TB_NHANVIEN_TenDN + " = ? AND " +
                CreateDatabase.TB_NHANVIEN_MatKhau + " = ?";
        // sử dụng rawQuery và truyền đối số vào luôn
        Cursor cursor = database.rawQuery(sql,new String[]{username,password});
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            maNhanVien = cursor.getInt(cursor.getColumnIndex(CreateDatabase.TB_NHANVIEN_MaNV));
            cursor.moveToNext();
        }
        return maNhanVien;
    }

    public ArrayList<NhanVien> getListEmployee() {
        ArrayList<NhanVien> listEmployee = new ArrayList<>();
        String sql = "SELECT * FROM " + CreateDatabase.TB_NHANVIEN;
        Cursor cursor = database.rawQuery(sql,null);
        cursor.moveToFirst();
        while(!cursor.isAfterLast()) {
            NhanVien nhanVien = new NhanVien();
            nhanVien.setManv(cursor.getInt(cursor.getColumnIndex(CreateDatabase.TB_NHANVIEN_MaNV)));
            nhanVien.setTenDN(cursor.getString(cursor.getColumnIndex(CreateDatabase.TB_NHANVIEN_TenDN)));
            nhanVien.setNgaySinh(cursor.getString(cursor.getColumnIndex(CreateDatabase.TB_NHANVIEN_NgaySinh)));
            nhanVien.setGioiTinh(cursor.getString(cursor.getColumnIndex(CreateDatabase.TB_NHANVIEN_GioiTinh)));
            nhanVien.setCMND(cursor.getInt(cursor.getColumnIndex(CreateDatabase.TB_NHANVIEN_CMND)));
            nhanVien.setMatKhau(cursor.getString(cursor.getColumnIndex(CreateDatabase.TB_NHANVIEN_MatKhau)));
            listEmployee.add(nhanVien);
            cursor.moveToNext();
        }
        return listEmployee;
    }

    public NhanVien getEmployeeById(int maNV) {
        NhanVien nhanVien = new NhanVien();
        String sql = "SELECT * FROM " + CreateDatabase.TB_NHANVIEN + " WHERE "
                + CreateDatabase.TB_NHANVIEN_MaNV + " = ?";
        Cursor cursor = database.rawQuery(sql,new String[]{maNV + ""});
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            nhanVien.setManv(maNV);
            nhanVien.setTenDN(cursor.getString(
                    cursor.getColumnIndex(CreateDatabase.TB_NHANVIEN_TenDN)));
            nhanVien.setMatKhau(cursor.getString(
                    cursor.getColumnIndex(CreateDatabase.TB_NHANVIEN_MatKhau)));
            nhanVien.setCMND(cursor.getInt(
                    cursor.getColumnIndex(CreateDatabase.TB_NHANVIEN_CMND)));
            nhanVien.setGioiTinh(cursor.getString(
                    cursor.getColumnIndex(CreateDatabase.TB_NHANVIEN_NgaySinh)));
            nhanVien.setNgaySinh(cursor.getString(
                    cursor.getColumnIndex(CreateDatabase.TB_NHANVIEN_NgaySinh)));
            cursor.moveToNext();
        }
        return nhanVien;
    }

    public boolean deleteEmployee(int maNV) {

        long check = database.delete(CreateDatabase.TB_NHANVIEN,
                CreateDatabase.TB_NHANVIEN_MaNV + " = ?",new String[]{maNV + ""});
        if(check > 0) {
            return true;
        } else {
            return false;
        }
    }

    public boolean updateEmployee(NhanVien nhanVien) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(CreateDatabase.TB_NHANVIEN_TenDN,nhanVien.getTenDN());
        contentValues.put(CreateDatabase.TB_NHANVIEN_MatKhau,nhanVien.getMatKhau());
        contentValues.put(CreateDatabase.TB_NHANVIEN_CMND,nhanVien.getCMND());
        contentValues.put(CreateDatabase.TB_NHANVIEN_GioiTinh,nhanVien.getGioiTinh());
        contentValues.put(CreateDatabase.TB_NHANVIEN_NgaySinh,nhanVien.getNgaySinh());
        String whereClause = CreateDatabase.TB_NHANVIEN_MaNV + " = ?";
        long check = database.update(CreateDatabase.TB_NHANVIEN,contentValues,whereClause,new String[] {nhanVien.getManv() + ""});
        if(check > 0) {
            return true;
        } else {
            return false;
        }
    }
}
