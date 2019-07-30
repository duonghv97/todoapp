package com.example.haduong.todoapp.DAO;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.haduong.todoapp.database.CreateDatabase;
import com.example.haduong.todoapp.model.Role;

import java.util.ArrayList;

public class RoleDAO {
    private SQLiteDatabase database;
    public RoleDAO(Context context) {
        CreateDatabase createDatabase = new CreateDatabase(context);
        database = createDatabase.open();
    }
    public boolean addRole(String name) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(CreateDatabase.TB_ROLE_TenQuyen,name);
        long check = database.insert(CreateDatabase.TB_ROLE,null,contentValues);
        if(check > 0) {
            return true;
        } else {
            return false;
        }
    }

    public ArrayList<Role> getAllRole() {
        ArrayList<Role> listRole = new ArrayList<>();
        String sql = "SELECT * FROM " + CreateDatabase.TB_ROLE;
        Cursor cursor = database.rawQuery(sql,null);
        cursor.moveToFirst();
        while(!cursor.isAfterLast()) {
            Role role = new Role();
            role.setIdRole(cursor.getInt(
                    cursor.getColumnIndex(CreateDatabase.TB_ROLE_MaQuyen)));
            role.setNameRole(cursor.getString(
                    cursor.getColumnIndex(CreateDatabase.TB_ROLE_TenQuyen)));
            listRole.add(role);
            cursor.moveToNext();
        }
        return listRole;
    }
}
