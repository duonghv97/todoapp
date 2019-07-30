package com.example.haduong.todoapp.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

// tạo file database kế thừa từ SQLiteHelper
public class CreateDatabase extends SQLiteOpenHelper {
    public static String TB_NHANVIEN = "NHANVIEN";
    public static String TB_MONAN = "MONAN";
    public static String TB_LOAIMONAN = "LOAIMONAN";
    public static String TB_BANAN = "BANAN";
    public static String TB_GOIMON = "GOIMON";
    public static String TB_CHITIETGOIMON = "CHITIETGOIMON";
    public static String TB_ROLE = "ROLE";
    // đặt tên các trường cũng là static
    public static String TB_NHANVIEN_MaNV = "MaNV";
    public static String TB_NHANVIEN_TenDN = "TenDN";
    public static String TB_NHANVIEN_MatKhau = "MatKhau";
    public static String TB_NHANVIEN_GioiTinh = "GioiTinh";
    public static String TB_NHANVIEN_NgaySinh = "NgaySinh";
    public static String TB_NHANVIEN_CMND = "CMND";
    public static String TB_NHANVIEN_MaQuyen = "MaQuyen";

    // table MONAN
    public static String TB_MONAN_MaMon = "MaMon";
    public static String TB_MONAN_GiaTien = "GiaTien";
    public static String TB_MONAN_TenMon = "TenMon";
    public static String TB_MONAN_MaLoai = "MaLoai";
    public static String TB_MONAN_HinhAnh = "HinhAnh";

    public static String TB_LOAIMONAN_MaLoai = "MaLoai";
    public static String TB_LOAIMONAN_TenLoai = "TenLoai";
    // ban an
    public static String TB_BANAN_MaBanAn = "MaBanAn";
    public static String TB_BANAN_TenBan = "TenBan";
    public static String TB_BANAN_TinhTrang = "TinhTrang";
    // goi mon
    public static String TB_GOIMON_MaGoiMon = "MaGoiMon";
    public static String TB_GOIMON_MaNV= "MaNV";
    public static String TB_GOIMON_NgayGoi = "NgayGoi";
    public static String TB_GOIMON_TinhTrang = "TinhTrang";
    public static String TB_GOIMON_MaBan = "MaBan";

    // chi tiet goi mon, chỉ là đặt tên thoi
    public static String TB_CHITIETGOIMON_MaGoiMon = "MaGoiMon";
    public static String TB_CHITIETGOIMON_MaMonAn = "MaMonAn";
    public static String TB_CHITIETGOIMON_SoLuong = "SoLuong"; // string nay co cả nháy rồi

    // bảng quyền
    public static String TB_ROLE_MaQuyen = "MaQuyen";
    public static String TB_ROLE_TenQuyen = "TenQuyen";



    public CreateDatabase(Context context) {
        super(context,"OrderFood",null,1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String tbNHANVIEN = "CREATE TABLE " + TB_NHANVIEN + " ( "
                +TB_NHANVIEN_MaNV + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + TB_NHANVIEN_TenDN + " TEXT,"
                + TB_NHANVIEN_MatKhau + " TEXT,"
                + TB_NHANVIEN_GioiTinh + " TEXT,"
                + TB_NHANVIEN_NgaySinh + " TEXT,"
                + TB_NHANVIEN_CMND + " INTEGER,"
                + TB_NHANVIEN_MaQuyen + " INTEGER )";
        String tbBANAN = "CREATE TABLE " + TB_BANAN + "("
                + TB_BANAN_MaBanAn + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + TB_BANAN_TenBan + " TEXT,"
                + TB_BANAN_TinhTrang + " TEXT )";
        String tbMONAN = "CREATE TABLE " + TB_MONAN + "("
                + TB_MONAN_MaMon + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + TB_MONAN_TenMon + " TEXT,"
                + TB_MONAN_GiaTien + " INTEGER,"
                + TB_MONAN_MaLoai + " INTEGER,"
                + TB_MONAN_HinhAnh + " TEXT) ";  // khong cần set là khoa ngoại, hiểu thế là được
        String tbLOAIMONAN = "CREATE TABLE " + TB_LOAIMONAN + "("
                + TB_LOAIMONAN_MaLoai + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + TB_LOAIMONAN_TenLoai + " TEXT )";

        String tbGOIMON = "CREATE TABLE " + TB_GOIMON + "("
                + TB_GOIMON_MaGoiMon + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + TB_GOIMON_NgayGoi + " TEXT,"
                + TB_GOIMON_TinhTrang + " TEXT,"
                + TB_GOIMON_MaNV + " INTEGER,"
                + TB_GOIMON_MaBan + " INTEGER )";
        // nhớ cách ra
        String tbCHITIETGOIMON = "CREATE TABLE " + TB_CHITIETGOIMON + "("
                + TB_CHITIETGOIMON_MaGoiMon + " INTEGER,"
                + TB_CHITIETGOIMON_MaMonAn + " INTEGER,"
                + TB_CHITIETGOIMON_SoLuong + " INTEGER,"
                + "PRIMARY KEY (" + TB_CHITIETGOIMON_MaGoiMon + "," + TB_CHITIETGOIMON_MaMonAn + "))";

        String tbROLE = "CREATE TABLE " + TB_ROLE + " ("
                + TB_ROLE_MaQuyen + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + TB_ROLE_TenQuyen + " TEXT )";

        // thực hiện
        db.execSQL(tbNHANVIEN);
        db.execSQL(tbBANAN);
        db.execSQL(tbGOIMON);
        db.execSQL(tbLOAIMONAN);
        db.execSQL(tbMONAN);
        db.execSQL(tbCHITIETGOIMON);
        db.execSQL(tbROLE);
        // người đăng nhập vào app lần đầu sẽ là admin các lần sau sẽ là nhân viên
        // tự quy dịnh mã quyền là 1 thì là admin, mã quyền là 2 thì là nhân viên
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
    // trả về sqlitedatabase thì mới thực hiện thêm ,xóa sửa được
    // lấy thằng writeable trong sqlite ra để thực hiện
    public SQLiteDatabase open() {
        return this.getWritableDatabase();
    }
}
