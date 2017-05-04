package com.bankmanagement.r.bankmanagement;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;


public class SqlData {


    MySQLite mySQLite;
    SQLiteDatabase sqliteDB;


    public SqlData(Context context) {
        mySQLite = new MySQLite(context);
    }

    public void openDB() {
        try {
            sqliteDB = mySQLite.getWritableDatabase();
        } catch (Exception e) {

        }
    }

    public void closeDB() {
        sqliteDB.close();
    }


    public void loginUser(String user_phone) {
        openDB();
        ContentValues list = new ContentValues();
        list.put(MySQLite.USER_PHONE, user_phone);
        sqliteDB.insert(MySQLite.USER_TABLE, null, list);
        closeDB();
    }

    public boolean isLogin() {
        openDB();
        Cursor cursor = sqliteDB.query(MySQLite.USER_TABLE, null, null, null, null, null, null);
        return cursor.getCount() > 0;
    }

    public void logOut() {
        openDB();
        sqliteDB.delete(MySQLite.USER_TABLE, "", null);
        closeDB();
    }

    public String getPhoneNumber()
    {
        openDB();
        Cursor cursor = sqliteDB.query(MySQLite.USER_TABLE, null, null, null, null, null, null);
        cursor.moveToFirst();
        String u = cursor.getString(cursor.getColumnIndex(MySQLite.USER_PHONE));
        cursor.moveToNext();
        cursor.close();
        return u;
    }
}
