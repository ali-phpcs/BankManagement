package com.bankmanagement.r.bankmanagement;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;



public class MySQLite extends SQLiteOpenHelper{

    private static final String DB_NAME = "BankM"; //database name
    private static final int VERSION_NUMBER = 1; //DB version

    public static final String USER_TABLE = "user"; //Tables Names

    //Columns Names
    public static final String ID = "id";
    public static final String USER_PHONE = "user_phone";


    //Create table sql query
    private String CREATE_MARKS_TABLE = "CREATE TABLE "+USER_TABLE+"( "+ID + " INTEGER PRIMARY KEY autoincrement, "
            + USER_PHONE + " text not null) ";

    public MySQLite(Context context){
        super(context,DB_NAME,null,VERSION_NUMBER);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_MARKS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + USER_TABLE);
        onCreate(sqLiteDatabase);
    }
}
