package com.example.zs.dataBase;


import android.content.ContentValues;
import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.zs.myaccount.R;

/**
 * Created by wuqi on 2016/9/7 0007.
 */
public class IncometContentDB extends SQLiteOpenHelper{
    public IncometContentDB(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public IncometContentDB(Context context, String name, SQLiteDatabase.CursorFactory factory, int version, DatabaseErrorHandler errorHandler) {
        super(context, name, factory, version, errorHandler);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("create table incomeContent (id Integer primary key autoincrement,resourceID Integer,year Integer," +
                 "month Integer,day Integer,category var(5),money Integer,remarks var(20),photo var(20));");

    }


    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
