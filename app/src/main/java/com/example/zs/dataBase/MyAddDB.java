package com.example.zs.dataBase;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by wuqi on 2016/9/5 0005.
 */
public class MyAddDB extends SQLiteOpenHelper {
    public MyAddDB(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public MyAddDB(Context context, String name, SQLiteDatabase.CursorFactory factory, int version, DatabaseErrorHandler errorHandler) {
        super(context, name, factory, version, errorHandler);
    }

    /**
     * 在数据库第一次创建时调用
     * 创建新表1，存放收入的信息
     * 创建新表2，存放支出的信息
     * @param sqLiteDatabase newMyAddDB类时传入的实参
     */
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("create table incomeInfo (id Integer,year Integer," +
                "mouth Integer,day Integer,content var(5),number Integer,remarks var(20),photo var(20));");
        sqLiteDatabase.execSQL("create table payoutInfo (id Integer,year Integer," +
                "mouth Integer,day Integer,content var(5),number Integer,remarks var(20),photo var(20));");
        sqLiteDatabase.execSQL("create table incomeCategoryInfo (id Integer,content var(5);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
