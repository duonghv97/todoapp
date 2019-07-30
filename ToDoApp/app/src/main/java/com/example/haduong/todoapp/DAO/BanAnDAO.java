package com.example.haduong.todoapp.DAO;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.haduong.todoapp.database.CreateDatabase;
import com.example.haduong.todoapp.model.BanAn;

import java.util.ArrayList;

public class BanAnDAO {
    private SQLiteDatabase database;
    public BanAnDAO(Context context) {
        CreateDatabase createDatabase = new CreateDatabase(context);
        database = createDatabase.open(); // goi đến thằng creatdatabase để kết nối vào
        // và ở DAO thì chỉ thao tác thôi
    }
    public boolean addBanAn(String tenBanAn) {
        ContentValues contentValues = new ContentValues();
        // các thuộc tính ta để static nên nó thuộc về lớp, muộn gọi thì gọi lớp nó ra
        contentValues.put(CreateDatabase.TB_BANAN_TenBan,tenBanAn);
        contentValues.put(CreateDatabase.TB_BANAN_TinhTrang,false);
        long check = database.insert(CreateDatabase.TB_BANAN,null,contentValues);
        if(check != 0) {
            return true;
        } else {
            return false;
        }
    }
    public ArrayList<BanAn> getAllBanAn() {

        ArrayList<BanAn> listBanAn = new ArrayList<>();
        String sql = "SELECT * FROM " + CreateDatabase.TB_BANAN;
        Cursor cursor = database.rawQuery(sql,null);
        // ban đầu con trỏ đang trỏ lung tung, ta cần di chuyển về vị trí đầu tiên
        cursor.moveToFirst();
        // cursor.isAfterLast() : khi đến vị trí cuối cùng
        while(!cursor.isAfterLast()) {
            BanAn banAn = new BanAn();
            // nếu không biết nó là vị trí nào thì sử dụng curson.getColumeIndex(tên cột)
            banAn.setMaBanAn(cursor.getInt(cursor.getColumnIndex(CreateDatabase.TB_BANAN_MaBanAn)));
            banAn.setTenBanAn(cursor.getString(cursor.getColumnIndex(CreateDatabase.TB_BANAN_TenBan)));
            // trả về 1 nếu là true thì sẽ set là true
            // trả về 0 sẽ là false thì set là false thì 0 > 0 --> sai
            banAn.setTinhTrang(cursor.getInt(cursor.getColumnIndex(CreateDatabase.TB_BANAN_TinhTrang)) > 0);
//            banAn.setTinhTrang(cursor.get);
            listBanAn.add(banAn);
            cursor.moveToNext(); // nhảy đến vị trí tiếp theo
        }
        return listBanAn;
    }

    // lấy trạng thái bàn ăn
    public String getStatusTable(int maBan) {
        String status = "";
        String tmp = "";
        String sql = "SELECT * FROM " + CreateDatabase.TB_BANAN +
                " WHERE " + CreateDatabase.TB_BANAN_MaBanAn + " = ?";
        Cursor cursor = database.rawQuery(sql,new String[]{maBan + ""});
        cursor.moveToFirst();
        while(!cursor.isAfterLast()) {
            // lấy trạng thái là false nó cho ra là 0
            tmp = cursor.getString(cursor.getColumnIndex(CreateDatabase.TB_BANAN_TinhTrang));
            cursor.moveToNext();
        }
        if(tmp.equals("0")) {
            status = "false";
        } else {
            status = "true";
        }
        return status; // sẽ là true hoặc false
    }
    // cập nhật tình trạng bàn, update, insert đều sử dụng contentvalues dùng cho có dữ liệu gì đó
    public boolean updateStatusTable(int maBan,String status) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(CreateDatabase.TB_BANAN_TinhTrang,status);
        int check = database.update(CreateDatabase.TB_BANAN,contentValues,
                CreateDatabase.TB_BANAN_MaBanAn + " = ?",new String[]{maBan+""});
        if(check > 0) {
            return true;
        } else {
            return false;
        }
    }

    public boolean deleteDinnerTableByMaBan(int maBan) {
        long check = database.delete(CreateDatabase.TB_BANAN,
                CreateDatabase.TB_BANAN_MaBanAn + " = ?", new String[]{maBan + ""});
        if(check != 0) {
            return true;
        } else {
            return false;
        }

    }
    public boolean updateTableDinner(int maBan,String name) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(CreateDatabase.TB_BANAN_TenBan,name);
        String whereClause = CreateDatabase.TB_BANAN_MaBanAn + " = ?";
        long check = database.update(CreateDatabase.TB_BANAN,contentValues,whereClause,new String[]{maBan + ""});
        if(check != 0) {
            return true;
        } else {
            return false;
        }
    }

    public String getNameTable(int maBan) {
        String tenBan = "";
        String sql = "SELECT * FROM " + CreateDatabase.TB_BANAN + " WHERE "
                + CreateDatabase.TB_BANAN_MaBanAn + " = ?";
        Cursor cursor = database.rawQuery(sql,new String[]{maBan + ""});
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            tenBan = cursor.getString(cursor.getColumnIndex(CreateDatabase.TB_BANAN_TenBan));
            cursor.moveToNext();
        }
        return tenBan;
    }

}
