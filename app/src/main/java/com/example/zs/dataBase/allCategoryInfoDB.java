
package com.example.zs.dataBase;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by wuqi on 2016/9/5 0005.
 */
public class AllCategoryInfoDB extends SQLiteOpenHelper {
    public AllCategoryInfoDB(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public AllCategoryInfoDB(Context context, String name, SQLiteDatabase.CursorFactory factory, int version, DatabaseErrorHandler errorHandler) {
        super(context, name, factory, version, errorHandler);
    }

    /**
     * 在数据库第一次创建时调用
     * @param sqLiteDatabase newMyAddDB类时传入的实参
     */
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        //创建新表1，存放支持的所有的Category信息，id唯一
        sqLiteDatabase.execSQL("create table allCategoryInfo (id Integer  primary key autoincreament,resource var(20);");

        /**
         * 初始化表一allCategoryInfo，录入所有的app支持的图标，绑定唯一的id
         */


    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}