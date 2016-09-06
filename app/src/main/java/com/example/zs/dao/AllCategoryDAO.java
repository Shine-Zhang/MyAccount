package com.example.zs.dao;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.zs.bean.MyAllCatoryInfo;
import com.example.zs.dataBase.AllCategoryDB;

import java.util.ArrayList;

/**
 * Created by wuqi on 2016/9/5 0005.
 * allCategoryInfo数据库的查询接口,
 * 因allCategoryInfo为初始化数据，只支持查询，不能修改
 */
public class AllCategoryDAO {
    public Context ctx;
    private final SQLiteDatabase db;

    /**
     * 构造器中得到SQLiteDatabase实例
     * @param ctx
     */
    public AllCategoryDAO(Context ctx) {
        this.ctx = ctx;
        //数据操作的准备工作
        AllCategoryDB initCategory = new AllCategoryDB(ctx, "initCategory", null, 1);
        db = initCategory.getReadableDatabase();
    }
    //查询接口
    public ArrayList<MyAllCatoryInfo> getCateoryList(){
        ArrayList<MyAllCatoryInfo> myAllCatoryInfos = new ArrayList<>();
        Cursor cursor = db.rawQuery("select * from allCategoryInfo", null, null);
        while (cursor.moveToNext()){
            int id = cursor.getInt(0);
            int resourceID = cursor.getInt(1);
            MyAllCatoryInfo myAllCatoryInfo = new MyAllCatoryInfo(id, resourceID);
            myAllCatoryInfos.add(myAllCatoryInfo);
        }
        return myAllCatoryInfos;
    }

}
