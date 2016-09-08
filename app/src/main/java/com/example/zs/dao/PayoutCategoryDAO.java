package com.example.zs.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.zs.bean.PayouContentInfo;
import com.example.zs.bean.UserAddCategoryInfo;
import com.example.zs.dataBase.AllCategoryDB;
import com.example.zs.dataBase.PayoutCategoryDB;

import java.util.ArrayList;

/**
 * Created by wuqi on 2016/9/6 0006.
 * PayoutCategoryDB表格的操作接口
 * 支出查询，删除，修改，清空表格操作
 */
public class PayoutCategoryDAO {
    public Context ctx;
    private SQLiteDatabase db;
    public PayoutCategoryDAO(Context ctx) {
        this.ctx = ctx;
        PayoutCategoryDB payoutCategory = new PayoutCategoryDB(ctx, "payoutCategory.db", null, 1);
        db = payoutCategory.getReadableDatabase();
    }
    /**
     * 插入单个数据到表格
     * @param resource
     * @param content
     */
    public void addPayoutCategoryToDB(int resource,String content){
        ContentValues contentValues = new ContentValues();
        contentValues.put("id_allcatrgory",resource);
        contentValues.put("content",content);
        db.insert("payoutCategoryInfo",null,contentValues);
    }
    public ArrayList<UserAddCategoryInfo> getPayoutCategoryToDB(){
        ArrayList<UserAddCategoryInfo> userAddCategoryInfos = new ArrayList<>();
        Cursor cursor = db.rawQuery("select * from payoutCategoryInfo;", null);
        while (cursor.moveToNext()){
            int resourceID = cursor.getInt(1);
            Log.i("000","resourceID"+resourceID);
            String name = cursor.getString(2);
            UserAddCategoryInfo userAddCategoryInfo = new UserAddCategoryInfo(resourceID, name);
            userAddCategoryInfos.add(userAddCategoryInfo);
        }
        return userAddCategoryInfos;
    }

    //张旭东添加取数据方法,用来读取“支出”数据
    public ArrayList<PayouContentInfo> getPayoutCategoryFromDB(){
        ArrayList<PayouContentInfo> PayouContentInfo = new ArrayList<>();
        Cursor cursor = db.rawQuery("select * from payoutCategoryInfo;", null);
        while(cursor.moveToNext()){
            int id = cursor.getInt(0);
            int resourceID= cursor.getInt(1);
            int year = cursor.getInt(2);
            int mouth = cursor.getInt(3);
            int day = cursor.getInt(4);
            String category = cursor.getString(5);
            String money = cursor.getString(6);
            String remarks = cursor.getString(7);
            String photo = cursor.getString(8);
            com.example.zs.bean.PayouContentInfo payouContentInfo =
                    new PayouContentInfo(id, resourceID, category, year, mouth, day, money, remarks, photo);
            PayouContentInfo.add(payouContentInfo);
        }
        return PayouContentInfo;
    }
}
