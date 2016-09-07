package com.example.zs.dataBase;

import android.content.ContentValues;
import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.zs.myaccount.R;

/**
 * Created by wuqi on 2016/9/6 0006.
 */
public class IncomeCategoryDB extends SQLiteOpenHelper {


    public IncomeCategoryDB(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public IncomeCategoryDB(Context context, String name, SQLiteDatabase.CursorFactory factory, int version, DatabaseErrorHandler errorHandler) {
        super(context, name, factory, version, errorHandler);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("create table incomeCategoryInfo (id Integer primary key autoincrement,id_allcatrgory Integer,content var(5));");
        initData(sqLiteDatabase);
    }
    //初始化一些默认的分类
    private void initData(SQLiteDatabase sqLiteDatabase) {
        int [] resource ={R.drawable.ic_gongzi_default,R.drawable.ic_jianzhi_default,
                R.drawable.ic_linghuaqian_default,R.drawable.ic_hongbao_default,R.drawable.ic_licai_default};
        String [] name = {"工资","兼职","零花钱","红包","理财收益"};
        for (int i=0;i<5;i++){
            ContentValues contentValues = new ContentValues();
            contentValues.put("id_allcatrgory",resource[i]);
            contentValues.put("content",name[i]);
            sqLiteDatabase.insert("incomeCategoryInfo",null,contentValues);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
