package com.example.zs.dataBase;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by WEIYU on 2016/9/7.
 * 用于创建或打开愿望信息数据库,该数据库包含了两个表，为完成愿望ongoingwish，已完成愿望completewish
 * @author 韦宇
 */
public class WishDB extends SQLiteOpenHelper {

    public WishDB(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public WishDB(Context context, String name, SQLiteDatabase.CursorFactory factory, int version, DatabaseErrorHandler errorHandler) {
        super(context, name, factory, version, errorHandler);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("create table ongoingwish (wishid Integer primary key autoincrement,wishYear Integer,"+
                "wishMonth Integer,wishDay Integer,wishTitle varchar(30),wishDescription varchar(50),wishFund varchar(10),wishphotoUri varchar(50));");
        sqLiteDatabase.execSQL("create table completewish (wishid Integer primary key autoincrement,wishYear Integer,"+
                "wishMonth Integer,wishDay Integer,wishTitle varchar(30),wishDescription varchar(50),wishFund varchar(10),wishphotoUri varchar(50));");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }


}
