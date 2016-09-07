package com.example.zs.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.zs.bean.UserAddCategoryInfo;
import com.example.zs.dataBase.IncomeCategoryDB;
import com.example.zs.dataBase.PayoutCategoryDB;

import java.util.ArrayList;

/**
 * Created by wuqi on 2016/9/6 0006.
 * PayoutCategoryDB表格的操作接口
 * 支出查询，删除，修改，清空表格操作
 */
public class IncomeCategoryDAO {
    public Context ctx;
    private  SQLiteDatabase db;
    public IncomeCategoryDAO(Context ctx) {
        this.ctx = ctx;
        IncomeCategoryDB incomeCategory = new IncomeCategoryDB(ctx, "incomeCategory.db", null, 1);
        db = incomeCategory.getReadableDatabase();
    }
    /**
     * 插入单个数据到表格
     * @param resource
     * @param content
     */
    public void addIncomeCategoryToDB(int resource,String content){
        ContentValues contentValues = new ContentValues();
        contentValues.put("id_allcatrgory",resource);
        contentValues.put("content",content);
        db.insert("incomeCategoryInfo",null,contentValues);
    }
    public ArrayList<UserAddCategoryInfo> getIncomeCategoryToDB(){
        ArrayList<UserAddCategoryInfo> userAddCategoryInfos = new ArrayList<>();
        Cursor cursor = db.rawQuery("select * from incomeCategoryInfo;", null);
        while (cursor.moveToNext()){
            int resourceID = cursor.getInt(1);
            Log.i("000","resourceID"+resourceID);
            String name = cursor.getString(2);
            UserAddCategoryInfo userAddCategoryInfo = new UserAddCategoryInfo(resourceID, name);
            userAddCategoryInfos.add(userAddCategoryInfo);
        }
        return userAddCategoryInfos;
    }

}
