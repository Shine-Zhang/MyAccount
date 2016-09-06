package com.example.zs.dataBase;

import android.content.ContentValues;
import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.zs.myaccount.R;

/**
 * Created by wuqi on 2016/9/5 0005.
 */
public class AllCategoryDB extends SQLiteOpenHelper {
    public AllCategoryDB(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public AllCategoryDB(Context context, String name, SQLiteDatabase.CursorFactory factory, int version, DatabaseErrorHandler errorHandler) {
        super(context, name, factory, version, errorHandler);
    }

    /**
     * 在数据库第一次创建时调用
     * @param sqLiteDatabase newMyAddDB类时传入的实参
     */
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        //创建新表1，存放支持的所有的Category信息，id唯一
        sqLiteDatabase.execSQL("create table allCategoryInfo (id Integer  primary key autoincrement,resourceID Integer);");

        /**
         * 初始化表一allCategoryInfo，录入所有的app支持的图标，绑定唯一的id
         */
        initData(sqLiteDatabase);

    }
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
    private void initData(SQLiteDatabase sqLiteDatabase) {
        int [] resource ={R.drawable.ic_1_default,R.drawable.ic_2_default,
                R.drawable.ic_3_default,R.drawable.ic_4_default,R.drawable.ic_5_default,
                R.drawable.ic_6_default,R.drawable.ic_7_default,R.drawable.ic_8_default,
                R.drawable.ic_9_default,R.drawable.ic_10_default,R.drawable.ic_11_default};
        for (int i=0;i<10;i++){
            ContentValues contentValues = new ContentValues();
            contentValues.put("resourceID",resource[i]);
            sqLiteDatabase.insert("allCategoryInfo",null,contentValues);
        }
    }

}
